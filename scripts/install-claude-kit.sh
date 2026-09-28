#!/usr/bin/env bash
# Installs the pairing toolkit as USER-level Claude Code skills (~/.claude/skills/<name>/SKILL.md), so they work in
# ANY project. Safe to re-run: identical files are skipped; a different existing file is backed up to *.bak.
# It also migrates an earlier install of this kit from ~/.claude/commands (see OLD_COMMANDS below).
#
#   ./scripts/install-claude-kit.sh
set -euo pipefail
SRC="$(cd "$(dirname "$0")/.." && pwd)/claude-kit/skills"
DEST="${HOME:?}/.claude"
mkdir -p "$DEST/skills"

sha() { shasum -a 256 "$1" | cut -d' ' -f1; }

# 1. Install / update skills
for dir in "$SRC"/*/; do
  name="$(basename "$dir")"
  src="$dir/SKILL.md"
  target="$DEST/skills/$name/SKILL.md"
  mkdir -p "$DEST/skills/$name"
  if [[ -f "$target" ]] && cmp -s "$src" "$target"; then
    echo "  = /$name (up to date)"
    continue
  fi
  if [[ -f "$target" ]]; then
    cp "$target" "$target.bak"
    echo "  ~ /$name (updated, previous saved as SKILL.md.bak)"
  else
    echo "  + /$name"
  fi
  cp "$src" "$target"
done

# 2. Migrate the earlier version of this kit (plain commands in ~/.claude/commands).
#    Files still identical to what we shipped are removed; edited ones are kept as *.bak so nothing is lost.
OLD_COMMANDS=(
  "interview-mode.md 495f54ef21ed498c9b2242933dce03482d65ddd08c402162f79a4e4dcdbffb2a"
  "onboard.md 19ab0432cb4f4259eb2a6bb827b44c56575ff663922640897ea2b4aaecd45710"
  "plan-first.md 2612e115047c78040b3128fd2024285e8ea9318c1b7b53b2c4dd071ea94531f6"
  "quiz-me.md cf75f5755bb1f2269a18c0ca3d5549fa1cbc04c6affd85bc929575159c5ad717"
  "requirements.md c2c066cf7c5b73d5498aa23ca784f850fb534ab8fb0700823dd7db2d07da62e9"
  "review-mine.md 6f0a0906e18109538f079374f94247578fa2e8213beab8299b34eeb5c35ce24b"
  "scaffold.md b80216eef1a3a6e1a99f65163dd32015c8ed8ccaf12330b0504129d3dd72c7e7"
)
for entry in "${OLD_COMMANDS[@]}"; do
  file="${entry%% *}"; expected="${entry##* }"
  old="$DEST/commands/$file"
  [[ -f "$old" ]] || continue
  if [[ "$(sha "$old")" == "$expected" ]]; then
    rm -f "$old"
    echo "  - removed old command ~/.claude/commands/$file (replaced by a skill)"
  else
    mv "$old" "$old.bak"
    echo "  ~ moved your edited ~/.claude/commands/$file to $file.bak (replaced by a skill)"
  fi
done

# 3. Personal preference line (added once)
LINE="- I'm on macOS and use IntelliJ IDEA (Claude Code plugin). Give Mac commands and IntelliJ shortcuts."
touch "$DEST/CLAUDE.md"
if ! grep -qF -- "$LINE" "$DEST/CLAUDE.md"; then
  printf '%s\n' "$LINE" >> "$DEST/CLAUDE.md"
  echo "  + preference line added to ~/.claude/CLAUDE.md"
fi

echo
echo "Done. Restart Claude Code (⌘Esc in IntelliJ), then type / to see:"
for dir in "$SRC"/*/; do echo "   /$(basename "$dir")"; done
