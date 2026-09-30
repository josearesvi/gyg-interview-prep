# IntelliJ as your single cockpit (macOS)

Goal: code, tests, Claude, git and the interviewer's requirements all in **one IntelliJ window**. No app switching.
Do this setup once (≈ 15 min). Verified against the Claude Code JetBrains docs (code.claude.com/docs/en/jetbrains).

## 1. One-time setup

1. **Claude Code CLI** (IntelliJ terminal, ⌥F12): `curl -fsSL https://claude.ai/install.sh | bash`, then `claude --version`.
2. **Plugin**: ⌘, → *Plugins* → *Marketplace* → search "Claude Code" (*Claude Code [Beta]*) → Install → **Restart IDE**.
3. **Esc must reach Claude** (Esc interrupts Claude): ⌘, → *Tools → Terminal* → untick
   **"Move focus to the editor with Escape"** → Apply.
4. **Claude beside the code**: open the terminal (⌥F12), right-click its tab bar → *Move to → Right Top*.
   Drag it to about 40% width. Tests and Run output stay at the bottom.
5. **Diffs in IntelliJ**: in Claude, `/config` → *Diff tool* → `auto`. Claude's edits then open in IntelliJ's
   side-by-side diff, where you accept or reject them.
6. **Screen-share readability**: ⌘, → *Editor → Font* → size 15–16. *Tools → Terminal* → same font size.
   To zoom on the fly: ⌘⇧A → "Increase Font Size in All Editors".
7. **Toolkit**: in the prep repo's terminal, `./scripts/install-claude-kit.sh` (`/pair-mode`, `/describe-repo`, `/onboard`, `/best-practices-review`, `/assess-notes`, `/requirements`, `/investigate`, `/scaffold`, `/plan-first`, `/review-mine`, `/explain-back` and `/wrap-up`, as skills in every project).
   Restart Claude Code afterwards.

## 2. Hotkeys (default macOS keymap)
★ = learn these first. Practise in `docs/WALKTHROUGH_0.md`.

**Navigate**
| Keys | Action |
|---|---|
| ★ ⇧⇧ | Search Everywhere (classes, files, actions, settings) |
| ★ ⌘O / ⌘⇧O | Go to class / Go to file |
| ★ ⌘E | Recent files (the fastest way to hop between 3–4 files) |
| ★ ⌘B | Go to declaration (⌘-click works too) |
| ★ ⌘⌥F7 / ⌥F7 | Show usages popup / Find usages panel |
| ⌘⌥B | Go to implementation(s) |
| ★ ⌘⇧T | Jump between class and its test (offers to create the test if none) |
| ★ ⌘[ / ⌘] | Back / forward (after jumping around) |
| ⌘F12 | File structure (methods of this class) |
| ⌥F1 then 1 | Reveal this file in the Project tree. Also: the ⊕ icon at the top of the Project panel, or turn on *Always Select Opened File* (Project panel ⋮ menu) and it follows you automatically |
| ⌘1 / ⌥F12 | Project tool window / Terminal (Claude) |
| ⌘⇧F12 | Hide all tool windows (focus on code) |
| ★ ⌘⇧A | Find Action: any command by name, when you forget a shortcut |

**Run, test, debug**
| Keys | Action |
|---|---|
| ★ ⌃⇧R | Run the test/class/method at the caret (also: the ▶ in the gutter) |
| ★ ⌃R | Re-run the last configuration |
| ⌃⌥R | Run… popup (pick a run configuration, e.g. "Acceptance: Part 1") |
| ⌃⇧D / ⌃D | Debug at caret / Debug last |
| ⌘F8 | Toggle breakpoint |
| F8 / F7 / ⌘⌥R | Step over / Step into / Resume |
| ⌥F8 | Evaluate expression (while paused) |
| ⌘⇧I | Load Maven/Gradle changes (after editing a pom or build file) |

**Edit and refactor**
| Keys | Action |
|---|---|
| ★ ⌥↩ | Show intention actions / quick-fix (import, create method, …) |
| ★ ⌘⌥L | Reformat code |
| ⌃⌥O | Optimize imports |
| ★ ⇧F6 | Rename (everywhere) |
| ⌘⌥M / ⌘⌥V | Extract method / Extract variable |
| ⌃T | Refactor This… menu |
| ⌘N | Generate (constructor, getters, test…) |
| ⌘D / ⌘⌫ | Duplicate line / Delete line |
| ⌥↑ / ⌥↓ | Extend / shrink selection (great before ⌘⌥K to send a selection to Claude) |

**Git and review**
| Keys | Action |
|---|---|
| ★ ⌘K | Commit (review each file's diff in the commit window) |
| ⌘T / ⌘⇧K | Update project (pull) / Push |
| ★ ⌘D (in the Commit or Changes view) | Show diff |
| ⌘⌥Z | Rollback (discard changes) |

**Claude Code plugin**
| Keys | Action |
|---|---|
| ★ ⌘Esc | Open or focus Claude Code |
| ★ ⌘⌥K | Insert a reference to the current file or selection (`@path#L10-20`) into the prompt |
| Esc (in Claude) | Interrupt Claude · Esc Esc: rewind to an earlier message |
| ⇧Tab (in Claude) | Cycle modes: normal → auto-accept edits → plan mode |

## 3. Daily layout
```
┌──────── Project (⌘1) ───┬──────────── Editor ─────────────┬──── Claude (terminal, right) ────┐
│ src/…                   │  YourClass.java                 │ > /requirements …                │
│ sealed/REQUIREMENTS-1   │                                 │                                  │
├─────────────────────────┴───── Run / Tests (bottom) ───────┴──────────────────────────────────┤
│ ✓ Part1WishlistTest (8)   ✗ …                                                                   │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘
```
With two projects open (the prep repo holding the requirements, and the training copy), switch windows with **⌘`**.

## 4. Review notes you can see at a glance (`// @review`) → `/assess-notes`
While you read code, mark your own suggestions with a tag that stands out and that Claude can find:
```java
// @review This should be a validation error, not a not-found
```
One-time IntelliJ setup (about 2 minutes):
1. **A keystroke for the tag.** `rv` is a name *you create*; IntelliJ doesn't have it until you do:
   ⌘, → search `live templates` → *Editor → Live Templates* → **+** → *1. Live Template* (group: *Java*).
   Abbreviation `rv`, description `Review note`, template text `// @review $END$`.
   Under the text box click **Define** (next to "No applicable contexts"), tick **Java**, then Apply and OK.
   To use it: on an **empty code line** type `rv`, press **Esc** if a suggestion popup opened, then press **Tab**.
   If that doesn't expand, press **⌘J** and pick `rv`, or simply type `// @review ` by hand (nothing depends on the template).
2. **A different colour and a list of all notes**: ⌘, → *Editor → TODO* → **+** → Pattern `@review\b.*` (no `\b` before the `@`: a word boundary can't sit between a space and `@`), pick an
   icon and colour → Apply. Notes now stand out in the editor, and **⌘6** (TODO tool window) lists every one in the project.
3. Run `/assess-notes` in Claude. It checks each note against best practices **and against how the repo handles the same
   case elsewhere** (a standard, split between approaches, or no precedent), then recommends one approach and says why.

The notes are scratch work: before you commit (⌘K), run *Find in Files* (**⌘⇧F**) for `@review` and delete the ones you've dealt with.

## 5. Add your own right-click options (for example "reveal in Project tree")
⌘, → *Appearance & Behavior → Menus and Toolbars*. Expand **Editor Tab Popup Menu** (right-click on a file's tab) or
**Editor Popup Menu** (right-click in the code). Select where it should go, click **+ → Add Action…**, search for
`Select Opened File` (or `Select In`), choose it, then OK and Apply. Action names can differ slightly between
IntelliJ versions; if you don't find it, search for `select` in that dialog.
