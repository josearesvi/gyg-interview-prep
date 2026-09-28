# Solving with AI: the playbook

GetYourGuide allows Copilot / Claude Code and says it's evaluating *"the ability to choose the right tool for
the right scenario."* With AI in the loop, **typing speed stops being the signal**. What they will watch is:

| They watch | So you must |
|-----------|-------------|
| Problem-solving | Decompose the task and pick the approach **before** prompting |
| Communication | Narrate what you ask the AI and why, then summarise what it did |
| Code quality | Read every diff; reject or trim what you wouldn't write yourself |
| Testing / debugging | Make the AI prove things with tests; reproduce before fixing |
| Ownership | Explain any line when asked "why?" |

## The loop (use it for every task)

1. **Understand** (you, 1–3 min): read the ticket and the test, restate them out loud, list the edge cases.
   Ask the interviewer clarifying questions. When requirements are handed to you, `/requirements <paste>` drafts the
   questions and a `TASKS.md` checklist, but *you* ask the questions.
2. **Plan** (you + AI): `/plan-first <task>` or Shift+Tab plan mode. Choose the approach **yourself** and say why.
3. **Delegate small** (AI): one step at a time, with constraints in the prompt (see templates).
4. **Verify** (you): read the diff in IntelliJ's diff viewer and run the tests. Say "this looks right because…"
   or "I don't like X, change it to Y."
5. **Explain** (you): summarise the change and its trade-off in 2 sentences. Check yourself with `/explain-back`, and
   close a session with `/wrap-up`.

Rule of thumb: **the AI types, you decide.** If you can't explain a line, you don't accept it.

## Presenting your toolkit (≈ 20 seconds, at the start)
Switching the toolkit on openly is a strength: it shows you've thought about *how* to work with AI, not just that you
use it. Say something like:

> "I use Claude Code with a small set of skills I keep for pair programming. `/pair-mode` sets the working
> agreement: small diffs, a plan before multi-file changes, tests after every edit, and it flags issues instead of
> silently fixing them. For a codebase I don't know I use `/describe-repo`, `/onboard` and `/best-practices-review`.
> For a bug I use `/investigate`, which gives hypotheses and how to confirm each one before any fix, and for new work
> I use `/requirements`. I finish with `/review-mine` and `/wrap-up`. I'll drive; it types. Stop me any time."

Then type `/pair-mode`. The skills never mention interviews, so what appears on screen is just your workflow.

| Moment | Skill |
|---|---|
| Start | `/pair-mode` |
| Handed a codebase | `/describe-repo` → `/onboard` → `/best-practices-review quick` |
| Something is broken (bug report, failing behaviour) | `/investigate <symptom>` |
| Given requirements (and follow-ups) | `/requirements <paste>` |
| Starting from nothing | `/scaffold <name>` |
| Before each non-trivial change | `/plan-first <task>` |
| After a change | `/review-mine` |
| Before you move on (optional, on your own) | `/explain-back` |
| Last 3–5 minutes | `/wrap-up` |

## Prompt templates that read well on a shared screen

- *Navigate:* "Trace `POST /bookings` from controller to DB and give me `file:line` for each hop. Don't change anything."
- *Reproduce:* "Run `PricingServiceTest` and tell me the expected vs actual for each failure."
- *Hypothesise:* "My hypothesis is the `!=` on the currency strings. Confirm or refute it from the code, no edits."
- *Constrained change:* "In `BookingService.book` only, change the capacity check so filling exactly to capacity is
  allowed. Don't touch other files. Then run `BookingControllerTest`."
- *Tests first:* "Write a failing test for 'cancelled bookings free their spots'. Don't fix the code yet."
- *Algorithm:* "Implement `topK` with a min-heap of size k, O(n log k). Keep the comparator as a named constant.
  No streams." (You picked the algorithm; the AI types it.)
- *Review:* "`/review-mine`" or "What edge case does this diff miss?"

## Per-level drills with AI

| Level | What you own (say it out loud) | What you delegate | Drill |
|------|---------------------------------|-------------------|-------|
| 1 Algorithms | The algorithm, its complexity, the edge cases | Typing the implementation | Before prompting, state the approach and Big-O. Afterwards, check the AI really did O(n log k), not sort-then-slice. Then `/explain-back`. **Also do E2 and E5 once by hand**: some interviewers still ask you to write one piece yourself. |
| 2 Debugging | Reproducing each bug, the hypothesis, grouping failures by root cause | Reading the code for suspects, applying the one-line fix | Never type "fix all the tests". Take one ticket at a time: reproduce → hypothesis → confirm → fix → rerun. Notice that BUG-2 masks BUG-3. |
| 3 Refactoring | The step order, keeping tests green, what counts as "done" | Mechanical extraction (repository, DTOs, advice) | Plan mode first: "5 safe steps". Run the tests after **each** step. Enable the `KnownBugs` tests one by one. Reject any step that changes behaviour and refactors at the same time. |
| 4 Features | API design, the locking strategy, the idempotency semantics, the time boundary | Boilerplate, test scaffolding, JPA annotations | For Feature 3, **you** pick pessimistic vs optimistic vs atomic update and defend it; the AI implements it. Ask the AI "how could this still overbook?" and judge its answer. |

## Red flags (interviewers notice these)
- One giant prompt with the whole task, then silence while it runs.
- Accepting a 200-line diff after a 5-second glance.
- "The AI says it's fine" instead of "the test proves it".
- The AI "fixing" the test instead of the code. Watch for edited assertions in the diff!
- Not knowing what your own code does when asked.

## Green flags
- "I'll let Claude map the endpoints while I read the entity."
- "It suggested X; I prefer Y because… let me tell it."
- "Before I accept this, let me run the tests and check the edge case with an empty list."
- Switching to hand-typing for a 1-line fix because it's faster, and saying so.

## Timing in a 50-minute coding block
Understand 10% · Plan 10% · Delegate + verify 60% · Explain / trade-offs 20%.
If the AI goes wrong twice on the same step, **stop prompting**: read the code yourself and do it by hand. That
recovery is itself a strong signal.
