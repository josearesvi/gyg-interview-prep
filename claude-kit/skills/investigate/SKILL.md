---
name: investigate
description: Turn a problem report into ranked hypotheses with code locations and ways to confirm each, then a fix plan that meets functional and non-functional standards
argument-hint: <what is going wrong - paste the bug report or symptom>
---
Something is going wrong:

<symptom>
$ARGUMENTS
</symptom>

Investigate before changing anything. **No edits until I choose a hypothesis.**

1. **Restate**: the observed behaviour vs the expected behaviour, and who is affected (in one or two lines each).
   If the expected behaviour is ambiguous, give the question to ask and the default you'd assume.
2. **Where it can happen**: trace from the entry point (endpoint, job, listener) through each layer to the
   suspects, with `file:line` for each hop.
3. **Hypotheses**, ranked by likelihood. For each one:
   - what would cause the symptom, and the evidence for and against it;
   - **how to confirm it quickly**: a failing test, a specific log line, a debugger breakpoint and the variable to
     watch, a SQL query, or a `curl`;
   - whether one hypothesis could **mask** another (e.g. a crash hiding a wrong result).
4. **Reproduce first**: propose the smallest test that fails *for the right reason* on the current code, and at which
   level (unit, `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`, concurrency test).
5. **Fix plan**: small steps, each ending with a test run. Mark the design decisions I should make.
6. **Non-functional checklist**: for each item, say "applies: …" or "n/a":
   - error semantics: status codes, error body contract, messages that don't leak internals;
   - transient vs permanent failures: what may be retried (with backoff and idempotency) and what must fail fast;
   - idempotency and concurrency: races, locking or atomic updates, duplicate requests or messages;
   - data integrity: constraints, bad existing data, and whether a migration or backfill is needed;
   - performance: query count, N+1, pagination, indexes;
   - observability: what to log (with context and a correlation id), metrics or alerts that would have caught this;
   - security: input validation, authz, injection;
   - API compatibility: will the fix change the contract for existing clients?
   - tests: which levels to add so this can't regress;
   - rollout: a feature flag or safe deploy order, and rollback.
7. **Prevention**: one or two lines on what would stop this class of bug from recurring (a test, a constraint, a
   lint rule, a review checklist item).

Then wait for me to pick a hypothesis and a first step.
