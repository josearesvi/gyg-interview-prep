# Level 2: Debug a buggy Spring Boot API

Covers the doc's **"Debugging: efficiently identifying and fixing issues"** and **"Debug and improve existing code"**.

`bookings-api` is a small booking service (activities, bookings, pricing). It compiles and starts, but
support has filed these tickets. **Only the symptoms are listed.** Finding the cause is the exercise.

| Ticket | Symptom reported by support |
|--------|----------------------------|
| BUG-1 | "A family of 3 was charged **minus €1,197**." Also, prices sometimes show as `99.8` instead of `99.80`. |
| BUG-2 | "**Every** booking fails with *Currency not supported: EUR*, even for EUR activities." |
| BUG-3 | "Last 2 spots show as available, but booking 2 people says *Not enough spots left*." |
| BUG-3b | "A customer cancelled, but the spot never became available again." |
| BUG-4 | "Mobile app crashes opening a deleted activity. The API returns 200 with an **empty body**." |
| BUG-5 | "Searching `city=rome` returns nothing; `city=Rome` works." |
| BUG-6 | "No activities show on the **first and last day** of the season." |
| BUG-7 | "The pending-booking sweeper job crashes with `ConcurrentModificationException`, and when it *doesn't* crash, it sometimes leaves expired holds behind." |

```bash
./mvnw -pl level-2-debugging test            # 13 of 18 tests fail at the start
./mvnw -pl level-2-debugging spring-boot:run # http://localhost:8082/activities
curl 'http://localhost:8082/activities?city=Rome'
curl -X POST localhost:8082/bookings -H 'Content-Type: application/json' \
     -d '{"activityId":1,"customerEmail":"a@b.com","participants":3,"currency":"EUR"}'
```

## Debugging method (say this out loud in the interview)
1. **Reproduce**: run the failing test, or `curl` the endpoint.
2. **Read the failure**: expected vs actual, and the top frame of *your* code in the stack trace.
3. **Hypothesise, then verify**: breakpoint, log line, or a smaller test. Don't guess-and-edit.
4. **Fix the cause, not the symptom**, then check whether the same mistake exists elsewhere.
5. **Leave a regression test.**

> Notice that BUG-2 hides BUG-3 and BUG-3b: one root cause can make many tests red. Always fix the
> upstream failure first.

Reference fixes (each commented with `FIXED`): `solutions/level-2-debugging/`.
