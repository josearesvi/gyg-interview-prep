#!/usr/bin/env bash
# Copies the repo to a temp dir, overlays solutions/<module>/ onto each module, deletes the files listed in
# solutions/<module>/DELETE, and runs the whole test suite. Proves every exercise is solvable.
# Your working tree is never touched.
#
#   ./scripts/check-solutions.sh                      # all modules
#   ./scripts/check-solutions.sh level-2-debugging    # one module
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

( cd "$ROOT" && tar --exclude ./target --exclude "*/target" --exclude ./.git --exclude ./solutions -cf - . ) | ( cd "$WORK" && tar -xf - )
for sol in "$ROOT"/solutions/level-*; do
  mod="$(basename "$sol")"
  if [[ -f "$sol/DELETE" ]]; then
    while read -r f; do [[ -n "$f" && "$f" != \#* ]] && rm -f "${WORK:?}/${mod:?}/${f:?}"; done < "$sol/DELETE"
  fi
  ( cd "$sol" && tar --exclude ./DELETE -cf - . ) | ( cd "$WORK/$mod" && tar -xf - )
done

cd "$WORK"
if [[ $# -gt 0 ]]; then
  ./mvnw -q -B test -pl "$1"
else
  ./mvnw -q -B test
fi
echo "All solution checks passed."
