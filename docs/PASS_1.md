# Pass 1: every exercise, driven through the pairing toolkit

Goal: on interview day, using the toolkit is **muscle memory** and visibly deliberate. Every session below runs the
same loop. You make the decisions out loud; Claude reads and types.

```
Orient   /describe-repo → /onboard → /best-practices-review quick     (on any codebase you didn't write)
Frame    /requirements <paste>   for new behaviour
         /investigate <symptom>  for something broken
Decide   /plan-first <task>      → YOU choose the approach and say why
Build    one small step at a time → review the diff in IntelliJ → the narrowest test goes green
Check    /review-mine → /explain-back
Close    /wrap-up                → present it aloud in under 2 minutes
```
Every session starts with `/pair-mode` and the 20-second intro (`docs/AI_PAIRING_PLAYBOOK.md`).
Keep Claude in **default mode** (press ⇧Tab until "auto mode" is off) so every edit shows up as a diff for you to accept.
Tech background for each session: `docs/TECH_FUNDAMENTALS.md`.

E1–E3 are already done. Pass 1 starts with the replica, as you chose, and ends with it again as a timed dress rehearsal.

---

## Session 1: the GetYourGuide replica, guided and untimed (≈ 90 min)
`./scripts/start-training.sh gyg-replica` → open it in a **new IntelliJ window** → `docs/WALKTHROUGH_3.md`.

| Step | Skill flow | What you own out loud |
|---|---|---|
| T−24h prep | Run the app, `curl` all 6 endpoints → `/describe-repo` → `/onboard` → `/best-practices-review full` → write *your* ranked smell list | "Correctness first: 500s on real data. Then contract. Then performance. Then design." |
| REQUEST-1 | `/investigate` + the ticket → a repro test → fix → the sealed `Request1Test` | 404 vs 500, why the null is really bad data (an FK and a cleanup migration), DTO vs entity for `/suppliers` |
| REQUEST-2 | `/investigate` → notice the NPE **masks** the empty-list bug → filter in SQL | Case-insensitive `LIKE` + escaping, and why `/search/{q}` should be `?q=` |
| REQUEST-3 | `/requirements` → `/plan-first` → a JPQL `GROUP BY` into a record | The field-order trap in constructor expressions; where DTOs belong |
| REQUEST-4 | `/investigate "slow at 10M rows"` → count the queries → `JOIN FETCH` | Proof before fix (Hibernate stats), pagination, indexes |
| REQUEST-5 | `/best-practices-review src/test quick` → one `@WebMvcTest` | The test pyramid and slices; dead mocks |
| Close | `/review-mine` → `/explain-back` → `/wrap-up` → the AI questions | "What I'd do next, in order" |

## Session 2: algorithms sprint (≈ 30 min), Level 1 E4–E7
| Exercise | Skill flow | What you own |
|---|---|---|
| E4 tree | `/plan-first` (recursive vs iterative) → delegate one method → the deep-tree test → `/explain-back` | Stack depth, and why the explicit `Deque` |
| E5 BFS | **By hand, no AI**, ≤ 12 min | Proof you can; BFS vs DFS, undirected edges |
| E6 two-sum | Delegate in one constrained prompt, then `/review-mine` | O(n) with one pass; why cents instead of doubles |
| E7 events | `/requirements` (paste the Javadoc; it's a spec, like the HackerRank round) → `/plan-first` → tests → `/review-mine` | Two indexes (event→participants, participant→events), idempotent cancel |

## Session 3: debugging by symptom (≈ 45 min), Level 2 `bookings-api`
`/describe-repo level-2-debugging` → `/onboard` → **one `/investigate` per ticket**, in this order:
BUG-2 (it masks BUG-3 and 3b) → BUG-3/3b → BUG-1 (money) → BUG-4/5/6 (contract) → BUG-7 (the ConcurrentModificationException).
Each one: the hypothesis out loud → a repro (the failing test) → the smallest fix → the test goes green. Finish with
`/best-practices-review level-2-debugging quick` (what's still wrong) → `/wrap-up`.

## Session 4: safe refactor (≈ 40 min), Level 3 `reviews-api`
`/best-practices-review level-3-refactoring full`. It should surface the SQL injection, `max(id)`, the static cache,
the leaked errors, field injection and `System.out`. Then `/plan-first "refactor in 5 safe steps, tests green after each"`.
Do one step per diff, with the characterization tests green every time. Enable the `KnownBugs` tests one by one.
`/review-mine` → `/wrap-up`.
Own out loud: *the order of steps*, and "never change behaviour and structure in the same step".

## Session 5: features with contracts (≈ 50 min), Level 4 `experiences-api`
For each feature: `/requirements` (paste the test class's Javadoc) → `/plan-first` → remove `@Disabled` → build → green →
`/review-mine` → `/explain-back` (use the TECH_FUNDAMENTALS self-checks). Order: **F4 → F1 → F2 → F3**.
- F1: the pagination contract, the sort whitelist, the size cap, your own page DTO.
- F2: idempotency semantics (same key and same body → replay; a different body → 422), and where keys live and for how long.
- F3: **you** choose the lock strategy (atomic UPDATE vs pessimistic vs optimistic) and defend it; Claude implements it.

## Session 6: from scratch (≈ 50 min), Training A
`./scripts/start-training.sh from-scratch` → `docs/WALKTHROUGH_1.md`.
`/requirements` with part 1 → `/scaffold wishlist-api` → build in slices → the acceptance tests for Part 1. Then parts 2 and 3,
each via `/requirements` (watch it flag the conflicts). Then `/best-practices-review wishlist-api quick` **on your own code**
→ `/wrap-up` + the part-3 scaling discussion.

## Session 7: handed-over Gradle code (≈ 50 min), Training B
`./scripts/start-training.sh existing-code` → `docs/WALKTHROUGH_2.md`.
`/describe-repo` → `/onboard` (the setup snag; mention the literal-placeholder security issue) → REQUEST-1 `/investigate`
(the timezone bug) → REQUEST-2 `/requirements` (test first) → REQUEST-3 `/investigate` (the overbooking race + N+1) →
`/best-practices-review quick` → `/wrap-up`.

## Session 8: the replica, timed dress rehearsal (60 min)
A **fresh** copy (`start-training.sh gyg-replica` again), the day-of timetable in `docs/WALKTHROUGH_3.md`, and a real
timer. Record yourself. Score it against the checklist in `docs/MOCK_INTERVIEW.md`. Things to watch:
- Did you name every trade-off?
- Did you show a failing test before each fix?
- Did you use `/wrap-up` in the last 5 minutes?

---

After Pass 1 → **Pass 2 (solo)**: a fresh, unseen set (new domains, new bugs, new sealed requests).
