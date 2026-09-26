# Level 4: Build features on a Spring Boot API

Covers the doc's **"Build new features with clean, testable code"**, **"Add functionality"** and *"Think beyond
the code: show us how your solution could impact real users."*

`experiences-api` is reasonably clean. Each feature has a test class that ships `@Disabled`. Remove the
annotation, watch it go red, then make it green without breaking `BaselineApiTest`.

| # | Feature | Test class | What it really tests |
|---|---------|-----------|----------------------|
| 1 | Search: city filter + pagination + sorting | `Feature1PaginationTest` | Spring Data `Pageable`, DB-side filtering, input whitelisting, API design |
| 2 | `Idempotency-Key` on POST /bookings | `Feature2IdempotencyTest` | Retries and double charges, a real-world reliability pattern |
| 3 | No overbooking under concurrency | `Feature3NoOverbookingTest` | Race conditions, transactions, locking strategies |
| 4 | Free cancellation up to 24h before start | `Feature4CancellationPolicyTest` | Domain rules, time handling, `Clock` injection |

Suggested order: 4 → 1 → 2 → 3 (easiest to hardest).

```bash
./mvnw -pl level-4-features test
./mvnw -pl level-4-features test -Dtest=Feature3NoOverbookingTest
./mvnw -pl level-4-features spring-boot:run      # http://localhost:8084/activities
```

## Discussion questions an interviewer may ask
- F1: Why not return Spring's `Page` directly? What happens at `page=100000` (deep offset)? What about keyset pagination?
- F2: Where do you store the keys, for how long, and what happens when two retries arrive at the same millisecond?
- F3: Pessimistic lock vs `@Version` optimistic locking vs an atomic `UPDATE ... WHERE booked + n <= capacity`.
  Which scales for a flash sale of 10,000 tickets?
- F4: Whose timezone is "24 hours before"? How do you test the exact boundary?

## Stretch ideas (no tests: design them in conversation)
- "Top 3 activities per city" endpoint: reuse `E1_TopRatedActivities` from level 1.
- Emit a `BookingConfirmed` event for email/analytics. When does it fire, before or after commit?
- Rate-limit POST /bookings per customer.

Reference implementation: `solutions/level-4-features/`.
