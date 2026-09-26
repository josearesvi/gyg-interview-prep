# Navigating a Java/Spring project with Claude Code

The interview allows AI tools, and GetYourGuide says it's testing *"the ability to choose the right tool for the
right scenario."* So the goal isn't "Claude writes it". It's **you steering, with Claude as a fast pair who
can read the whole repo in seconds.** Practise everything below on this repo until it's muscle memory.

> Exact shortcuts can change between versions. Run `/help` in your Claude Code to see the current list.

## 1. Starting and resuming

| Do this | How |
|--------|-----|
| Start in a project | `cd gyg-interview-prep && claude` (always from the **root**, so Claude sees every module) |
| Start with a first question | `claude "give me a map of this repo"` |
| Continue the last conversation | `claude -c` · pick an older one: `claude -r` or `/resume` |
| Clear context between exercises | `/clear` (fresh start, same folder) |
| Shrink a long conversation | `/compact` · see what is using context: `/context` |
| Stop Claude mid-answer | `Esc` · go back and edit an earlier message: `Esc` `Esc` (rewind) |
| Include another folder | `/add-dir ../other-project` |

## 2. Pointing Claude at code: the core navigation skill

- **`@` mentions** put a file or folder straight into context, with tab-completion:
  `explain @level-2-debugging/src/main/java/com/gyg/prep/bookings/booking/BookingService.java`
  `what does @level-4-features/src/test do?`
- **Ask structural questions.** Claude searches for you (Glob/Grep/Read), so you don't need to know file names:
  - "Where is the capacity check for bookings? Give me `file:line`."
  - "List every class that calls `BookingRepository`."
  - "Trace `POST /bookings` from the controller to the database."
  - "Which endpoints exist in level-3? Table: method, path, handler."
- **`!` bash mode** runs a shell command without leaving Claude, and its output lands in the context:
  `! ./mvnw -pl level-2-debugging test -Dtest=PricingServiceTest`
- **Paste a stack trace** (or drag in a screenshot) and ask "what's the first frame in *our* code, and why?"
- **In IntelliJ** (plugin, see `INTELLIJ_CLAUDE.md`): your selection is shared automatically, **⌘⌥K** inserts a
  file/line reference, **⌘Esc** opens Claude, and diffs open in IntelliJ's diff viewer.

## 3. Modes and control

- **`Shift+Tab`** cycles permission modes: normal (asks before edits) → auto-accept edits → **plan mode**.
  **Plan mode** makes Claude research and propose a plan *without editing anything*. It's ideal for "how
  would you refactor this?" and for reading a codebase you don't know.
- `/permissions` shows what Claude may run without asking. This repo pre-allows `./mvnw` and `git diff`
  (`.claude/settings.json`).
- `/model` switches model. `/memory` edits `CLAUDE.md` memory. `/init` generates a `CLAUDE.md` for a new repo.

## 4. Project memory and custom commands (already set up here)

- `CLAUDE.md` (repo root) is read at the start of every session. Here it puts Claude in **coach mode**.
  Open it to see how it's written, then run `/init` on the interviewers' project to generate one there.
- `.claude/commands/*.md` are **your own slash commands**, and `$ARGUMENTS` is what you type after the name:

| Command | Use it for |
|---------|-----------|
| `/tour level-2-debugging` | 5-minute onboarding to a module (layout + one request traced end-to-end) |
| `/hint E3` | One progressive hint, never the answer |
| `/review-mine` | Senior-style review of your `git diff`, then runs the tests |
| `/interviewer level-2` | Timed role-play with feedback at the end |
| `/compare-solution E4` | Only after you've finished: compares yours with `solutions/` |

## 5. Navigation drills (do each one in < 3 minutes)

1. `/tour level-4-features`, then, *without Claude*, open the three files it mentioned in your IDE.
2. Ask: "Which JPA entities exist across all modules and how are they related?" Check one answer yourself.
3. Ask: "Where would I add an `Idempotency-Key` header in level-4? Don't write code, just `file:line`."
4. In level 2, run `! ./mvnw -pl level-2-debugging test`, then ask: "Group these failures by likely root cause."
5. Plan mode (`Shift+Tab`) on level 3: "Propose a 5-step refactoring plan that keeps the tests green at every step."
6. Ask: "Which tests cover `BookingService.cancel`?" (Answer: `BaselineApiTest.cancelsABooking` and Feature 4.)

## 6. How to use AI *in the interview itself*

What reads well to interviewers:
- **Narrate your use of it.** "I'll ask Claude to map the endpoints while I read the entity," or "I'll let it write
  the boilerplate test setup; the assertions I'll write myself."
- **Use it for reading and scaffolding**: navigation, summaries, test boilerplate, Spring annotations you've
  forgotten, stack-trace triage.
- **Own the reasoning.** Choose the algorithm, the edge cases and the trade-offs yourself, out loud.
- **Verify everything.** Read each diff before accepting it, and run the tests. Say "let me check that" and actually check.
- **Know when *not* to use it.** A one-line fix you already see is faster by hand. Doing it by hand shows judgement.

What hurts:
- Pasting the whole task into the prompt and waiting in silence.
- Accepting a large diff you can't explain.
- Letting the tool pick an approach you can't defend when asked "why?"

Practise the same exercise twice: **once without AI** (proving you can) and **once with Claude** (proving you
can steer it). Compare time and quality.

## 7. Getting code into Claude Code

| Situation | How |
|-----------|-----|
| **Your laptop** (most common) | `git clone …` or unzip the folder, then `cd` into it and run `claude`. Claude reads the files on disk, so there's nothing to upload. |
| **Claude Code on the web / app** (claude.ai/code) | The code must be in a **GitHub repo**. Push it, then pick that repo when starting a session. |
| A single file or screenshot | Drag it into the prompt or paste it (images: `Ctrl+V`), or reference it with `@path`. |
| Interviewers' project on the day | They'll share a repo or zip: clone/unzip → `./mvnw test` (or `./gradlew test`) → `claude` → `/init` if you have a minute, or just `/tour`-style questions. |
