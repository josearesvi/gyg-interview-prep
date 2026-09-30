---
name: investigate
description: Test-first investigation of a problem - ranked hypotheses, then a failing test (red) before any production code, then the minimal fix (green), then non-functional hardening, each step with its own test and a stop for my go-ahead
argument-hint: <what is going wrong - paste the bug report or symptom>
---
Something is going wrong:

<symptom>
$ARGUMENTS
</symptom>

Work **test-first**, in phases, and **stop at the end of each phase** until I say go.

## Ground rules
- **Phase 1 is read-only.** No file changes of any kind.
- **No production code is written or changed until a failing test exists and I've seen it fail for the right reason.**
  Test code comes first, every time. That includes each non-functional improvement you propose.
- Keep each phase small. Show the diff for what you changed, run the narrowest test with the project's wrapper
  (`./mvnw test -Dtest=Class#method` or `./gradlew test --tests 'Class.method'`), and show the result.
- If something can't be tested at a sensible level, say so and propose how to pin the behaviour down first
  (a characterization test, or a manual check) before changing any code.

## Phase 1: Investigate (read-only)
1. **Restate**: the observed behaviour vs the expected behaviour, and who is affected (one or two lines each).
   If the expected behaviour is ambiguous, give the question to ask and the default you'd assume.
2. **Where it can happen**: trace from the entry point (endpoint, job, listener) through each layer to the suspects,
   with `file:line` for each hop.
3. **Hypotheses**, ranked by likelihood. For each one: what would cause the symptom, the evidence for and against,
   **how to confirm it quickly** (a test, a log line, a breakpoint and the variable to watch, a SQL query, a `curl`), and
   whether it could **mask** another hypothesis.

**STOP.** Ask which hypothesis to pursue. Wait for me: I may add facts, re-rank or reject hypotheses first.

## Phase 2: RED (starts when I pick a hypothesis): write the failing test, and only the test
1. Propose the test(s) before writing them: name, level (unit, `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`,
   concurrency), the exact assertion (status code, body, exception), and why it **fails on the current code**.
   Follow the conventions of the existing tests in this repo (find them and match their style and location).
2. Write the test code **only**. Don't change production code. If the test can't compile without a new production
   signature, add only the empty signature that makes it compile, and say you did.
3. Run the test. Show the failing output and say whether it fails **for the right reason** (the asserted
   behaviour differs from the expected one), not because of a typo, a setup problem or a compile error. If it fails for
   the wrong reason, fix the test and run it again.

**STOP.** Say "Red confirmed" and wait for my "go green".

## Phase 3: GREEN (after "go green"): the smallest change that passes
1. Make the **smallest** production change that turns that test green. No refactoring and no extra behaviour.
2. Run the new test, then the surrounding test class or module to catch regressions. Report both results.
3. Say what you changed and which layer owns the fix.

**STOP.** Say "Green" and wait. I'll say "go harden", "go refactor" or "done".

## Phase 4: HARDEN (after "go harden"): non-functional items, each test-first
Go through this checklist. For each item, say "applies: …" or "n/a", and for each that applies, name the **test that
would pin it**. Then wait for me to choose which to do, and run each chosen item as its own red → green loop.
- error semantics: status codes, error body contract, messages that don't leak internals;
- transient vs permanent failures: what may be retried (with backoff and idempotency) and what must fail fast;
- idempotency and concurrency: races, locking or atomic updates, duplicate requests or messages;
- data integrity: constraints, bad existing data, whether a migration or backfill is needed;
- performance: query count, N+1, pagination, indexes;
- observability: what to log (with context and a correlation id), metrics or alerts that would have caught this;
- security: input validation, authz, injection;
- API compatibility: does the fix change the contract for existing clients?
- rollout: a feature flag or safe deploy order, and rollback.

## Phase 5: REFACTOR and close (after "go refactor", with everything green)
Propose refactorings separately from behaviour changes, each one small, re-running the tests after every step.
End with **prevention**: one or two lines on what would stop this class of bug from recurring (a test, a constraint,
a lint rule, a review checklist item).
