# Walkthrough 0: the loop and the hotkeys, on Exercise 1 (≈ 20 min)

Goal: learn the **Understand → Plan → Delegate → Verify → Explain** loop and the ★ hotkeys on a tiny problem,
without leaving IntelliJ. Say every step **out loud**, as you would on the call.

**Before you start:** the prep repo is cloned and open in IntelliJ, the cockpit is set up
(`docs/INTELLIJ_COCKPIT.md`), and the kit is installed.

| # | Do | Keys / text | Say out loud |
|---|----|-------------|--------------|
| 1 | Open the exercise | ⇧⇧ → type `E1Top` → ↩ | "Let me read the task first." |
| 2 | Jump to its test | ⌘⇧T | "The tests tell me the exact contract." |
| 3 | Run it: expect red | ⌃⇧R (caret in the test class) | "4 failing, all UnsupportedOperationException. Expected." |
| 4 | Scan the tests' structure | ⌘F12 | "Edge cases: k ≤ 0, k > n, ties, don't mutate the input." |
| 5 | Back to the class | ⌘⇧T (or ⌘[ ) | |
| 6 | Start Claude in interview mode | ⌘Esc → `/interview-mode` | "I'll use Claude as my pair; I'll steer." |
| 7 | Plan, with **you** choosing | `/plan-first E1 top-k activities` | "Two options: sort-and-slice, O(n log n), or a size-k min-heap, O(n log k). I'll take the heap; say why." |
| 8 | Delegate one small step | Select the `topK` method (⌥↑ repeatedly) → ⌘⌥K → type: *"Implement this with a min-heap of size k and a named comparator constant. Don't touch other files."* | "I'm giving it the constraints; it types." |
| 9 | Review the diff in IntelliJ | The diff opens → read it → accept | "Comparator order: rating desc, reviews desc, id asc. Matches the tests. Input list isn't mutated." |
| 10 | Tidy | ⌘⌥L (reformat), ⌃⌥O (imports) | |
| 11 | Verify | ⌃R (re-runs the last test) | "All green." |
| 12 | Check the claim | ⌘B on `PriorityQueue` if unsure; ask Claude *"Is this really O(n log k)? Where's the log k?"* | "Each offer/poll is log k, n times." |
| 13 | Defend it | `/quiz-me` | Answer its 3 questions aloud. |
| 14 | Commit | ⌘K → look at the diff (⌘D) → message "E1: top-k with min-heap" → Commit | |
| 15 | Compare | `/compare-solution E1` | "One thing I'll remember is…" |

Then repeat **without Claude** on E2 (binary search), by hand, in ≤ 12 minutes. Some interviewers ask you to write one
piece yourself.

Speed drill: redo steps 1–5 and 11 using only the keyboard until it takes under 30 seconds.
