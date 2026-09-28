# Walkthrough 3: the GetYourGuide replica, a full 60-minute mock

This is the closest thing to the real day: the same stack, domain, layout and kind of problems as GetYourGuide's own
interview repo (see `docs/RESEARCH_2026.md`). Do it once with me, then again alone against a timer.

## T−24h: you "received the link" (≈ 45 min, the evening before)
1. Prep repo terminal: `./scripts/start-training.sh gyg-replica`, then *File → Open…* the printed folder → Trust.
2. Follow `training/gyg-replica/sealed/REQUEST-0.md`. Run it, `curl` every endpoint, `/pair-mode` + `/onboard`,
   and write your **smell list by layer**. **Fix nothing.**

## T0: the interview (set a 60-minute timer)
| Time | What | How |
|---|---|---|
| 0–5 | Intro | Your 1-minute background. They "share" the project, which you already have open. |
| 5–8 | Orient them | Walk them through *your* smell list in 2 minutes. "I noticed… I'd prioritise… Where would you like to start?" |
| 8–20 | **REQUEST-1** | Reproduce with `curl localhost:8080/activities/58820`, then read the log (⌘⇧F "Exception" in the Run window). Hypothesis → fix. Then *copy `sealed-tests/…/Request1Test.java` into `src/test/java/com/getourguide/interview/interviewer/`* and run it with ⌃⇧R. |
| 20–32 | **REQUEST-2** | Notice the NPE **masks** the empty-result bug. Filter in SQL, case-insensitive, escaped. Run `Request2Test`. |
| 32–44 | **REQUEST-3** | JPQL `GROUP BY` + a constructor expression into a record in `dto/`. Say the field-order trap out loud. Run `Request3Test`. |
| 44–52 | **REQUEST-4** | Count the queries first (`spring.jpa.show-sql=true` or `Request4Test`), then `LEFT JOIN FETCH`, `readOnly`. Discuss pagination and indexes. |
| 52–55 | **REQUEST-5** | Name the weaknesses of the test suite; write or show one `@WebMvcTest`. |
| 55–60 | **REQUEST-6** + your questions | `/wrap-up` gives you the "what I'd do next" answer. The AI questions: frameworks in `docs/MOCK_INTERVIEW.md`. |

Rules while you work: `/requirements` for each request, `/plan-first` before anything touching 2+ files, and small
diffs reviewed in IntelliJ. Say the trade-off every time. **Say "I'd come back to X" and move on** rather than going
down a rabbit hole. The real interviewers care about prioritisation.

## Boot 4 notes (the 2026 repo is on Spring Boot 4)
- Starters were split up: `spring-boot-starter-webmvc`, `spring-boot-starter-flyway` (plain `flyway-core` no
  longer auto-configures), and test starters `spring-boot-starter-webmvc-test` / `…-data-jpa-test`.
- Test annotations moved: `org.springframework.boot.webmvc.test.autoconfigure.{AutoConfigureMockMvc, WebMvcTest}`,
  `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`. `@MockBean` is gone; use `@MockitoBean`.
- JSON is Jackson 3 (`tools.jackson.*`). That's why the recursive `/suppliers` fails with a "nesting depth" error.

## Debrief
`/review-mine`, then `/explain-back` on the service, then `/wrap-up` (present it aloud). Compare with `solutions/training/gyg-replica/`.
Proof that the reference is complete: `./scripts/check-training.sh`.
