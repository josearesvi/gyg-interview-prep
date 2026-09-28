---
name: best-practices-review
description: Read-only best-practices audit of a codebase or module - API/HTTP contract, error handling, transient vs permanent failures, event-driven patterns, coupling, logging, persistence, concurrency, security, config, test coverage - with severity, evidence and fixes
argument-hint: [path or module] [quick|full]
---
Audit this code for best practices. Arguments: $ARGUMENTS. The scope is a path or module if one is given,
otherwise the whole repository. Mode `quick` = the 10 most important findings; `full` (the default) = everything.
**Read only: make no edits.** Every finding needs evidence (`file:line` plus a short quote or description).

## Checklist (look for each of these; skip categories that don't apply and say so)
1. **API and HTTP contract**: wrong status codes (200 on create instead of 201 with `Location`, 500 for not-found,
   200 with an error body, 404 vs 400 on bad input), entities returned instead of DTOs, search or filters in the path
   instead of query params, no pagination or size limits, an inconsistent error shape (vs RFC 9457 `ProblemDetail`),
   missing validation, non-idempotent PUT/DELETE, breaking-change risks.
2. **Error handling**: catch-all handlers mapping everything to 500, swallowed exceptions, `catch (Exception)` +
   `printStackTrace`, `e.getMessage()` leaked to clients, checked exceptions misused (including in lambdas),
   unhandled paths (`Optional.get()`, `list.get(0)`, possible NPEs on nullable relations).
3. **Transient vs non-transient failures**: are they told apart? Retry **only** transient failures (timeouts,
   connection resets, 503/429, deadlocks, lock timeouts, optimistic-lock conflicts), with bounded retries, backoff
   and jitter, and only for idempotent operations or ones with an idempotency key. **Never** retry 4xx or validation
   or business-rule failures. Look for a circuit breaker or bulkhead on remote calls, timeouts set on every client,
   and dead-letter handling for messages that will never succeed.
4. **Event-driven patterns**: dual writes (DB save + publish without a transactional outbox), events published inside
   a transaction that may roll back (prefer `@TransactionalEventListener(phase = AFTER_COMMIT)` or an outbox),
   consumers that aren't idempotent or don't deduplicate, hidden ordering assumptions, exceptions swallowed in
   listeners, no DLQ or poison-message handling, blocking or slow work on listener threads, and events carrying
   entities or too much or too little data.
5. **Coupling and layering**: services depending on controllers, the web layer using `EntityManager` or SQL, entities
   leaking across layers, circular dependencies, god classes, static mutable state, field injection instead of
   constructor injection, duplicated logic.
6. **Logging and observability**: `System.out`, log-and-rethrow (duplicate logs), wrong levels, messages without
   context (ids, inputs), no correlation/request id, PII or secrets in logs, string concatenation instead of `{}`
   placeholders, exceptions logged without the stack trace, no metrics or health checks for key flows.
7. **Persistence**: N+1 queries, `findAll()` + filtering in memory, missing `@Transactional` or its `readOnly`,
   open-in-view left on, `@Data` on entities, `@NotFound(IGNORE)`, questionable ID generation, primitives on
   nullable columns, missing constraints, foreign keys or indexes, native SQL where JPQL would do.
8. **Concurrency**: check-then-act races (read → check → write), non-thread-safe shared collections, missing
   locking or `@Version`, time-of-check/time-of-use on capacity or balances.
9. **Security**: SQL or JPQL built by string concatenation, hard-coded secrets, secrets compared with `equals`
   (not constant-time), missing authn/authz checks, guessable public ids, missing input limits.
10. **Configuration**: hard-coded values that belong in config, secrets with silent defaults (instead of failing at
    startup), environment-specific values in code.
11. **Tests and coverage**:
    - Map each production class to its tests. List the classes with **no** tests and the public methods never
      exercised.
    - Flag tests without meaningful assertions, controllers called directly instead of through MockMvc, dead
      mocks, time-dependent or order-dependent tests, tests sharing a persistent database (state leaking between runs),
      and the wrong test slice (`@SpringBootTest` where
      `@WebMvcTest`/`@DataJpaTest` would do).
    - If this is a Maven project and running the tests is acceptable, offer to measure line coverage with JaCoCo
      without editing the pom:
      `./mvnw -q org.jacoco:jacoco-maven-plugin:0.8.13:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.13:report`
      (the report is in `target/site/jacoco/index.html`). Otherwise label coverage as **estimated**.
12. **Code health**: dead code, copy-paste, misleading names, magic numbers, overly long methods.

## Severity
- **Critical**: data loss or corruption, a security hole, wrong money, or a crash on common input.
- **High**: a wrong API contract, a likely production incident, or a serious performance cliff.
- **Medium**: maintainability or robustness problems that will bite later.
- **Low**: style and minor hygiene.

## Output
1. A findings table, sorted by severity: `# | Severity | Category | file:line | Evidence | Why it matters | Fix | Effort (S/M/L)`.
2. **Fix these 5 first**, with one line each on why.
3. **Good practices already present**: be specific. A fair review names what's done well.
4. **Not checked / needs runtime data**: anything you couldn't verify statically.
