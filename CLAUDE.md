# Interview-prep pair: instructions for Claude

This repo is a **practice environment** for a GetYourGuide backend pair-programming interview
(Java 21, Spring Boot 3.5, Maven, JUnit 5, AssertJ, MockMvc, H2). AI tools are **allowed** in that interview:
the user will steer Claude and will not hand-write everything. What's being trained is **steering, verifying
and explaining** AI-written code while thinking aloud. The user works on **macOS** in **IntelliJ IDEA**.

The portable pairing toolkit (`/pair-mode`, `/describe-repo`, `/onboard`, `/best-practices-review`, `/requirements`, `/investigate`, `/scaffold`, `/plan-first`, `/review-mine`, `/explain-back` and `/wrap-up`) lives in `claude-kit/skills/` and is installed into `~/.claude/skills` by
`scripts/install-claude-kit.sh`. Its wording must stay neutral and professional (no mention of interviews), because
the user switches it on in front of other engineers. The rules below mirror `/pair-mode` for this repo.

## Default: pair mode (behave like you would in the real interview)
- Write code when asked, but keep each change **small and reviewable** (one bug, one method, one test at a time).
- Before any change touching more than one file, state a 2–4 line plan and wait for a go-ahead.
- After editing, run the relevant tests (`./mvnw -pl <module> test -Dtest=...`) and report the result honestly.
- Don't silently "fix" things the user didn't ask about. **Mention** them as observations instead, because
  spotting them is part of what the user is practising.
- Name the trade-off whenever you pick an approach (complexity, locking strategy, and so on), so the user can
  challenge it.
- Never read `solutions/` unless the user asks (e.g. `/compare-solution`). The reference answers are for
  checking afterwards, not for generating the work.

## Coach mode
When the user runs `/hint`, `/explain-back`, or says "coach me", don't write the solution. Ask questions and give
the smallest useful nudge.

## Layout
- `level-1-algorithms/`: plain Java exercises (stubs throw UnsupportedOperationException) + tests
- `level-2-debugging/`: `bookings-api` with 8 planted bugs (symptoms in its README)
- `level-3-refactoring/`: `reviews-api` legacy god-controller + characterization tests
- `level-4-features/`: `experiences-api` + `@Disabled` feature tests
- `training/{from-scratch,existing-code,gyg-replica}/project`: interview-day simulations (copied out with
  `scripts/start-training.sh`); `training/*/sealed/` and `training/gyg-replica/sealed-tests/`: the interviewer's
  requirements and hidden tests. Never read them unless the user asks you to reveal the next part
- `solutions/<module>/`, `solutions/training/…`: reference answers (off-limits by default)
- `docs/AI_PAIRING_PLAYBOOK.md`: how the user wants to work with you

## Commands
- `./mvnw -pl <module> test [-Dtest=Class#method]`: run tests
- `./mvnw -pl <module> spring-boot:run`: run an API (ports 8082 / 8083 / 8084)
- `./scripts/check-solutions.sh [module]`: verify the level solutions in a temp copy
- `./scripts/check-training.sh`: verify both training projects (acceptance suite + Gradle)
- `./scripts/start-training.sh <from-scratch|existing-code>`: start a simulation in `~/interview-sim/`
