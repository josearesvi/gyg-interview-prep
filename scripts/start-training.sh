#!/usr/bin/env bash
# Copies a training project OUT of this repo into a fresh git repo, the way you'd receive it on interview day.
# (Outside this repo on purpose: Claude Code also reads CLAUDE.md from parent folders, and the practice
# CLAUDE.md must not leak into the simulation.)
#
#   ./scripts/start-training.sh from-scratch
#   ./scripts/start-training.sh existing-code [target-parent-dir]   (default: ~/interview-sim)
#   ./scripts/start-training.sh gyg-replica      <- closest to the real GetYourGuide repo
set -euo pipefail
NAME="${1:-}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SRC="$ROOT/training/$NAME/project"
if [[ -z "$NAME" || ! -d "$SRC" ]]; then
  echo "usage: $0 <from-scratch|existing-code|gyg-replica> [target-parent-dir]" >&2
  exit 1
fi
DEST="${2:-$HOME/interview-sim}/$NAME-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$(dirname "$DEST")"
cp -R "$SRC" "$DEST"
cd "$DEST"
git init -q
git add -A
git -c user.name="${GIT_AUTHOR_NAME:-Interview Prep}" -c user.email="${GIT_AUTHOR_EMAIL:-prep@example.com}" \
  commit -q -m "Initial import"

cat <<MSG

Ready: $DEST

Next (all inside IntelliJ):
  1. IntelliJ: File > Open... (or "Open" on the Welcome screen) > $DEST > Open > Trust Project
  2. ⌘Esc to start Claude Code, then type:  /pair-mode
  3. Follow docs/WALKTHROUGH_$(case "$NAME" in from-scratch) echo 1;; existing-code) echo 2;; *) echo 3;; esac).md in the prep repo.
     The interviewer's requirements are in the prep repo: training/$NAME/sealed/ (open them only when told).
MSG
