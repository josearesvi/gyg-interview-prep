# Walkthrough 2: a handed-over codebase (≈ 50 min)

Scenario: *"Here's our tour-inventory service. Clone it and get it running."* Then the interviewer gives requests one
at a time. It's deliberately **Gradle** (not Maven), has **no README** and **no run configurations**, and it
**doesn't pass its tests out of the box**. That's typical.

## Setup (≈ 3 min, not timed)
1. Prep repo terminal: `./scripts/start-training.sh existing-code`
2. *File → Open…* → the printed folder → *Trust Project*. IntelliJ detects Gradle and imports it (watch the bottom bar).
3. ⌘Esc → `/pair-mode`

## Orient (≈ 8 min): show them you can read a codebase
4. `/onboard`. While Claude works, explore yourself: ⌘1 (project tree) → ⌘O `TourService` → ⌘F12 → ⌘B into the
   repository. Say what you see.
5. Claude reports the tests fail with **`Could not resolve placeholder 'SUPPLIER_API_KEY'`**. That's the setup snag.
   Decide the fix yourself. Options: an env var in the run configuration, a test-only `application-test.yml`,
   or a default. *Which one is right for tests, and which for production?* Say it, then let Claude do it.
   (Good follow-up to notice: without the env var, the app currently **starts anyway** with the literal
   `${SUPPLIER_API_KEY}` as the key. Mention it as a security observation.)
6. Run everything: open `TourApiTest` → ⌃⇧R. Exactly **one** red test should remain.
7. Create a run configuration yourself, since there won't be one on the day: the gutter ▶ next to
   `TourInventoryApplication.main` → *Modify Run Configuration…* → *Environment variables* →
   `SUPPLIER_API_KEY=dev-key` → Run. Try `curl localhost:8085/tours?city=rome` in the terminal.

## Request 1: bug (≈ 10 min)
8. Prep window: `training/existing-code/sealed/REQUEST-1.md` → read it aloud → `/requirements ` + paste.
9. Reproduce: the red test. Put a breakpoint (⌘F8) in `TourService.availability`, then ⌃⇧D on the test, step (F8),
   and evaluate (⌥F8) `d.getStartsAt()`. **You** state the hypothesis ("it converts to a UTC date"); Claude confirms.
10. Delegate the fix, constrained: *"Use the tour's time zone and filter in SQL with a half-open [from, to) range."*
    Say why half-open (DST days are 23 or 25 hours). ⌃R → green → ⌘K.

## Request 2: feature (≈ 10 min)
11. `REQUEST-2.md` → `/requirements`. Answer its questions: is "exactly 4 remaining" included? 400 for 0 or 21?
12. Ask Claude for **the test first**: *"Write a failing MockMvc test for minSeats, including the boundary."* ⌃⇧R → red.
13. Then the implementation → green → commit.

## Request 3: under load (≈ 10 min, mostly talking)
14. `REQUEST-3.md`. Before prompting, explain the overbooking race in `ReservationService.reserve` yourself
    (read-modify-write). Name 3 fixes: an atomic conditional UPDATE, `@Version` + retry, or `SELECT … FOR UPDATE`.
    Pick one and say why.
15. *"Write a concurrency test: 30 threads, capacity 20, expect exactly 20 successes. Not @Transactional."* Watch it
    go **red** against the current code, which proves the bug. Then the fix → green.
16. The N+1 in `listTours`: ask Claude *"How many SQL queries does GET /tours run for 300 tours?"* Verify it
    yourself by adding `spring.jpa.show-sql=true`. Discuss the fix (one grouped query or a join), and implement it if there's time.

## Debrief
- `/review-mine` · `/explain-back` on `ReservationService` · `/wrap-up`
- Compare with `solutions/training/existing-code/` in the prep repo.
