# Claude Code inside IntelliJ IDEA (macOS)

Source: code.claude.com/docs/en/jetbrains and /setup, checked 2026-09-26.

## 1. Install the Claude Code CLI (once)
```bash
curl -fsSL https://claude.ai/install.sh | bash     # recommended: native install, auto-updates
# or:  brew install --cask claude-code             # Homebrew (update with: brew upgrade claude-code)
claude --version
```

## 2. Install the IntelliJ plugin
IntelliJ → **Settings (⌘,) → Plugins → Marketplace** → search **"Claude Code"** (it's listed as *Claude Code [Beta]*)
→ Install → **restart IntelliJ** (required).

## 3. Connect it
- Open `gyg-interview-prep` in IntelliJ, open the **built-in terminal** (⌥F12), and run `claude`.
  It connects to the IDE automatically.
- Or press **⌘Esc** to launch Claude Code from anywhere in the editor.
- If you started `claude` in an external terminal (iTerm/Terminal.app), run `/ide` to attach it to IntelliJ.

## 4. What you get
| Feature | How it helps in the interview |
|---------|-------------------------------|
| **Diffs open in IntelliJ's diff viewer** | Review every AI change side by side before accepting it, on the shared screen |
| **Selection sharing** | Highlight a method, then ask "why does this throw?" without pasting anything |
| **Diagnostics sharing** | Claude sees IntelliJ's compile errors and warnings |
| **⌘⌥K** inserts a file reference | Adds `@path/File.java#L10-40` for the current file or selection to the prompt |

## 5. One fix to make now
IntelliJ's terminal steals **Esc**, which is the key that interrupts Claude.
**Settings → Tools → Terminal** → untick **"Move focus to the editor with Escape"** (or remove the "Switch focus
to Editor" keybinding) → Apply.

Optional: inside Claude, `/config` → **Diff tool** → `auto` (IDE diffs) or `terminal`.

## 6. Recommended layout for practice (and the interview)
- Editor in the centre, **Claude in the bottom terminal panel** (drag it wider), the Run/Test panel on the right.
- Run tests from the gutter ▶ icons (⌃⇧R) to verify what Claude claims; debug with ⌃⇧D.
- Bump the font size (Settings → Editor → Font, and the terminal font) so it reads well on screen share.
