#!/usr/bin/env bash
# Installs the interview Claude kit into your USER-level Claude Code config (~/.claude), so the commands work in
# ANY project, including the one the interviewers hand you. Safe to re-run: identical files are skipped, and any
# different existing file is backed up to <name>.bak before being replaced.
#
#   ./scripts/install-claude-kit.sh
set -euo pipefail
SRC="$(cd "$(dirname "$0")/.." && pwd)/claude-kit"
DEST="${HOME}/.claude"
mkdir -p "$DEST/commands"

for f in "$SRC"/commands/*.md; do
  name="$(basename "$f")"
  target="$DEST/commands/$name"
  if [[ -f "$target" ]] && cmp -s "$f" "$target"; then
    echo "  = $name (up to date)"
    continue
  fi
  if [[ -f "$target" ]]; then
    cp "$target" "$target.bak"
    echo "  ~ $name (updated, previous saved as $name.bak)"
  else
    echo "  + $name"
  fi
  cp "$f" "$target"
done

LINE="- I'm on macOS and use IntelliJ IDEA (Claude Code plugin). Give Mac commands and IntelliJ shortcuts."
touch "$DEST/CLAUDE.md"
if ! grep -qF -- "$LINE" "$DEST/CLAUDE.md"; then
  printf '%s\n' "$LINE" >> "$DEST/CLAUDE.md"
  echo "  + preference line added to ~/.claude/CLAUDE.md"
fi

echo
echo "Done. In any project, start Claude Code (⌘Esc in IntelliJ) and type / to see:"
for f in "$SRC"/commands/*.md; do echo "   /$(basename "$f" .md)"; done
