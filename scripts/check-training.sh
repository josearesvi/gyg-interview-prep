#!/usr/bin/env bash
# Verifies the two training projects in temp copies (your working tree is never touched):
#  - from-scratch: builds the reference wishlist-api, boots it, runs the black-box acceptance suite against it
#  - existing-code: as shipped the tests fail (setup snag + bug); with the reference overlay everything is green
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
PORT="${PORT:-18080}"
WORK="$(mktemp -d)"
APP_PID=""
cleanup() { [[ -n "$APP_PID" ]] && kill "$APP_PID" 2>/dev/null || true; rm -rf "$WORK"; }
trap cleanup EXIT

echo "== from-scratch"
cp -R "$ROOT/training/from-scratch/project" "$WORK/fs"
cp -R "$ROOT/solutions/training/from-scratch/wishlist-api" "$WORK/fs/"
( cd "$WORK/fs/wishlist-api" && ./mvnw -q -B package )
java -jar "$WORK"/fs/wishlist-api/target/wishlist-api-*.jar --server.port="$PORT" > "$WORK/app.log" 2>&1 &
APP_PID=$!
for _ in $(seq 1 60); do curl -s "localhost:$PORT/" > /dev/null 2>&1 && break; sleep 1; done
( cd "$WORK/fs" && ./mvnw -q -B -f acceptance/pom.xml test -Dacceptance -DBASE_URL="http://localhost:$PORT" )
kill "$APP_PID"; APP_PID=""
echo "   acceptance suite green against the reference app"

echo "== existing-code"
cp -R "$ROOT/training/existing-code/project" "$WORK/ec"
if ( cd "$WORK/ec" && ./gradlew -q test > /dev/null 2>&1 ); then
  echo "   ERROR: shipped project should have failing tests" >&2; exit 1
fi
echo "   as shipped: failing, as intended"
( cd "$ROOT/solutions/training/existing-code" && tar -cf - . ) | ( cd "$WORK/ec" && tar -xf - )
( cd "$WORK/ec" && ./gradlew -q test )
echo "   with reference solution: green"
echo "All training checks passed."
