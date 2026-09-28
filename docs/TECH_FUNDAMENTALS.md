# Tech fundamentals: REST contracts, Spring, JPA, concurrency

The newer exercises (the replica, Training A/B, Level 4) expect you to reason about **API contracts and persistence**,
not just make tests green. Each section gives what to know, how to say it in one line, and where it shows up in our
exercises. The **self-check** questions are what we use with `/explain-back` in the guided pass.

---

## 1. REST and HTTP contracts

### Resources and methods
| Method | Use | Safe? | Idempotent? | Typical success |
|---|---|---|---|---|
| GET | read | yes | yes | 200 |
| POST | create / trigger an action | no | **no** | 201 + `Location` (create), 200/202 (action) |
| PUT | replace the whole resource at a known URI | no | yes | 200 / 204 |
| PATCH | partial update | no | not guaranteed | 200 / 204 |
| DELETE | remove | no | yes | 204 (a repeat can be 404 or 204; pick one and document it) |

- *Safe* = no server state change. *Idempotent* = doing it twice has the same effect as doing it once. That's why
  a **retried POST can double-book** (Level 4, Feature 2 → an `Idempotency-Key` makes POST retry-safe).
- Nouns, plural, hierarchical: `/travellers/{id}/wishlist/{itemId}` (Training A). Actions that aren't CRUD:
  `POST /bookings/{id}/cancel` (Level 4) or `PATCH /bookings/{id} {"status":"CANCELLED"}`. Know the trade-off:
  an explicit action is clearer and easier to authorise; a PATCH is more "RESTful".
- **Search and filter belong in query parameters**: `GET /activities?q=berlin&page=0&size=20&sort=price,asc`.
  `/activities/search/{q}` (the replica) is a smell: a path identifies a resource, special characters
  (`/`, `%`) break routing, and you can't add filters without new paths.

### Status codes you must use precisely
| Code | Meaning | In our exercises |
|---|---|---|
| 200 OK | success with a body | GET endpoints |
| 201 Created | resource created (+ `Location` header) | POST /bookings, POST wishlist item |
| 204 No Content | success, no body | DELETE wishlist item |
| 400 Bad Request | malformed or invalid input (validation, wrong type) | `/activities/abc`, `participants: 0`, `sort=hack` |
| 401 / 403 | not authenticated / authenticated but not allowed | supplier API key (Training B) |
| 404 Not Found | resource doesn't exist (or isn't yours; don't leak existence) | unknown activity, deleting someone else's item |
| 409 Conflict | valid request that conflicts with the current state | sold out, duplicate wishlist item, too late to cancel |
| 422 Unprocessable | well-formed but semantically rejected | wishlist limit, idempotency key reused with a different body |
| 500 | *our* bug | never deliberately; the replica's catch-all turns 404s into 500s |

One-liner for 409 vs 422: *409 = conflicts with the current state of the resource; 422 = the request itself is
semantically invalid.* Teams differ, so **be consistent and document it**.

### Error contract
Return one consistent error shape. Spring supports **RFC 9457 Problem Details** out of the box (`ProblemDetail`,
`spring.mvc.problemdetails.enabled=true`):
```json
{ "type": "about:blank", "title": "Not Found", "status": 404, "detail": "Activity 999 not found", "instance": "/activities/999" }
```
Never leak stack traces, SQL or `e.getMessage()` of unexpected exceptions (Level 3 leaks them).

### DTO vs entity
Never serialise JPA entities: lazy proxies, bidirectional recursion (the replica's `/suppliers` → truncated JSON),
accidental exposure of internal fields, and your API becomes coupled to your schema. **DTOs are the contract; entities
are storage.**

### Pagination and sorting
- Offset (`page`, `size`): simple, but deep pages are slow (`OFFSET 1000000`) and results shift while paging.
- Keyset (`?after=<lastId>`): stable and fast, but no "jump to page 50".
- Cap `size` (Level 4 caps it at 100), **whitelist sort fields**, and add a tie-breaker (`id`) for a stable order.
- Return your own page DTO (`content, page, size, totalElements`), not Spring's `PageImpl`.

### Evolving a contract without breaking clients
Adding fields: OK (clients must ignore unknown fields). Removing or renaming fields, changing types, or changing a
status code: **breaking**. Options: additive changes only, deprecation windows, versioning (`/v2/…`, or media-type
versioning). Consumer-driven contract tests (e.g. Pact / Spring Cloud Contract) catch breaks in CI.

**Self-check**
1. Why is POST not idempotent, and how do you make a booking POST safe to retry?
2. 409 or 422 for "wishlist is full"? Defend it.
3. What's wrong with `GET /activities/search/{q}`? Propose the contract you'd use instead.
4. Why return a DTO instead of the `Supplier` entity? Name three concrete failure modes.
5. A client needs a new field `durationMinutes`. Is adding it breaking? Is renaming `price` → `amount`?

---

## 2. Spring MVC

- `@RestController` = `@Controller` + `@ResponseBody` on every method. With plain `@Controller`, a returned
  `String` is treated as a **view name** (the replica works only because it returns `ResponseEntity`).
- Binding: `@PathVariable`, `@RequestParam(required = false, defaultValue = …)`, `@RequestBody`, `@RequestHeader`.
  A path variable is never null; a type mismatch gives `MethodArgumentTypeMismatchException` → map it to 400.
- Validation: `@Valid @RequestBody Dto` with `@NotBlank`/`@Min`/… → `MethodArgumentNotValidException` (400).
  Constraints directly on `@RequestParam` use Spring's built-in method validation (Training B's `minSeats`).
- Errors: one `@RestControllerAdvice` with **specific** `@ExceptionHandler`s, plus a logged fallback for 500.
- Test slices:
  | Annotation | Loads | Use for |
  |---|---|---|
  | `@WebMvcTest(X.class)` + `@MockitoBean` | web layer only | status codes, JSON, validation, advice: fast |
  | `@DataJpaTest` | JPA + an in-memory DB (+ Flyway) | queries, mappings, constraints |
  | `@SpringBootTest` + `@AutoConfigureMockMvc` | everything | end-to-end slices, wiring |
  Calling `controller.method()` directly (the replica's original tests) skips routing, serialisation and advice.
- Boot 4: `@MockBean` → `@MockitoBean`; test annotations moved to `org.springframework.boot.webmvc.test…` and
  `…data.jpa.test…`; the starters are modular (`spring-boot-starter-webmvc`, `…-flyway`); Jackson 3.

**Self-check**
1. What happens if a `@Controller` method returns `"ok"`?
2. Which exception does `/activities/abc` raise, and which status should the client get?
3. When would you choose `@WebMvcTest` over `@SpringBootTest`? What does each prove?

---

## 3. JPA and Hibernate

- **Lazy vs eager**: `@ManyToOne` defaults to EAGER and `@OneToMany` to LAZY. Prefer LAZY everywhere and fetch
  explicitly per use case.
- **N+1**: 1 query for the list + 1 per row for the association. Fix it with `JOIN FETCH`, `@EntityGraph`, or a DTO
  projection. **Prove it**: `spring.jpa.show-sql=true` or Hibernate statistics (the replica's `Request4Test`).
- **Open-in-view** (on by default): lazy loading during JSON serialisation hides N+1s and holds DB connections
  for the whole request. Turn it off and load what you need in the service.
- `@Transactional(readOnly = true)` on reads: no dirty checking or flush, and it can be routed to a replica.
- **`@Data` on entities** is dangerous: `equals`/`hashCode`/`toString` touch lazy collections
  (LazyInitializationException, StackOverflowError), and a mutable hashCode breaks `HashSet`s.
- `@NotFound(IGNORE)`: a missing FK target silently becomes `null`, and Hibernate forces eager loading. The real fix is
  data cleanup plus a foreign key.
- IDs: `IDENTITY` (a DB auto-increment; disables JDBC insert batching) vs `SEQUENCE` (batch-friendly) vs `AUTO`
  (provider picks; surprises).
- Primitive (`double`) vs wrapper (`Double`) fields: a primitive can't represent a SQL NULL.
- JPQL vs native SQL: JPQL is portable and type-checked against entities; use native for DB-specific features.
- **Constructor expressions**: `select new com.x.dto.Stats(s.name, count(a), sum(a.price), avg(a.rating)) …`.
  Argument order and types must match the constructor (`count`→Long, `sum(int)`→Long, `avg`→Double), and swapped
  same-typed args give **silently wrong numbers**.
- Search: `lower(x) like :pattern`, and escape `%` and `_` from user input. `LIKE '%x%'` can't use a B-tree index;
  at scale use full-text search (Postgres `tsvector`, Elasticsearch).

**Self-check**
1. How many queries does `GET /activities` run in the original replica, and why? How do you prove it?
2. Why is `findAll()` followed by `.filter(id)` wrong even for 17 rows?
3. What exactly goes wrong when a `@Data` entity with a `@OneToMany` is serialised or put in a `HashSet`?
4. What does `readOnly = true` change?

---

## 4. Concurrency and consistency

- **Check-then-act** races: `if (seats available) insert booking` run by two requests at once → overbooking
  (Level 4 Feature 3, Training B Request 3).
- The fixes:
  | Fix | How | Trade-off |
  |---|---|---|
  | Atomic conditional UPDATE | `UPDATE … SET reserved = reserved + :n WHERE id = :id AND reserved + :n <= capacity` → check the rows affected | fastest, no locks held; the logic lives in SQL |
  | Pessimistic lock | `@Lock(PESSIMISTIC_WRITE)` → `SELECT … FOR UPDATE` | simple and correct; serialises writers, so a hot row becomes a bottleneck |
  | Optimistic lock | `@Version` + retry on `OptimisticLockException` | great with low contention; retries storm under high contention |
  | DB constraint | `UNIQUE(traveller_id, activity_id)` | the last line of defence, even if the app has bugs |
- **Idempotency keys**: store `key → (request hash, response)`. Same key and same body → replay the response; same key
  and a different body → 422. The key is the PK, so concurrent retries lose on the unique constraint. Expire old keys.
- Multi-instance: in-memory state (maps, caches) is per instance. Anything shared goes to the DB or a cache like Redis.

**Self-check**
1. Two users book the last seat at the same millisecond. Walk through what happens with each of the fixes above.
2. Why is the idempotency key the primary key rather than just an indexed column?
3. Your service runs on 3 instances. Which parts of the wishlist solution break?

---

## 5. Money, time and IDs (small things that interviewers love)
- Money: `BigDecimal` (or integer cents), never `double`. `0.1 + 0.2 ≠ 0.3`. Round once, at the end (`HALF_UP` or
  `HALF_EVEN`, and say which). Store the currency explicitly (the replica stores `"$"`, which is a symbol, not ISO 4217).
- Time: store an `Instant` (UTC). Convert to the **activity's local zone** for calendar days (Training B's bug). Use
  half-open ranges `[start, end)` for days, which are DST-safe. Inject a `Clock` so tests can control "now".
- Public IDs must not be guessable if they grant access (the share links in Training A): use 128 random bits, not a
  sequence.
