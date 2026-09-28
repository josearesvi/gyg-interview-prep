# Mock interview: 60 minutes, with the real format

Do this at least twice: once with a friend or with `/interviewer mock`, and once recording yourself.
**Use Claude Code throughout, the way you will on the day** (see `docs/AI_PAIRING_PLAYBOOK.md`), and narrate every prompt.
The best dress rehearsal is a **training project** (`./scripts/start-training.sh …`, see WALKTHROUGH_1/2), timed at 50 min.
The table below is an alternative built from the levels. Reset first: `git stash -u`.

| Time | Phase | What you do |
|------|-------|-------------|
| 0:00–0:05 | **Intro** | 1-minute background (practise it: current role → a proud backend project → why GetYourGuide). They share the project. |
| 0:05–0:10 | **Orient** | Build it (`./mvnw test`), skim the layout (`/tour level-2-debugging` or your IDE), ask 2 clarifying questions. |
| 0:10–0:25 | **Debug** | Level 2: fix BUG-2, BUG-3/3b and BUG-1, each with the failing test as proof. Think aloud. |
| 0:25–0:45 | **Build** | Level 4, Feature 4 (cancellation policy), then start Feature 3 (overbooking) and *discuss* the lock options. |
| 0:45–0:55 | **Algorithm / extension** | "Top 3 activities per city" (reuse E1) or Level 1 E5 (BFS), talking complexity first. |
| 0:55–1:00 | **Q&A** | Your questions (below). |

## Scoring yourself (what the doc says they look for)
- [ ] **Think aloud**: no silence longer than ~30s without saying what you're checking
- [ ] **Weigh options**: named at least 2 approaches and why you picked one
- [ ] **Start simple**, then improve (the brute-force version was said out loud first)
- [ ] **Edge cases** raised *before* the tests forced them (empty, null, duplicates, boundaries, concurrency)
- [ ] **Tests**: ran them often and added one of your own
- [ ] **Asked questions** when requirements were ambiguous
- [ ] **Took feedback**: changed course when hinted
- [ ] **AI used deliberately**: narrated, verified, didn't blindly accept
- [ ] **User impact** mentioned (double charges, overbooked tours, stale ratings misleading travellers)

## The AI questions (reported by candidates in 2026): have these ready
Answer each in ≤ 90 s with a **concrete story**: situation → what you did with AI → how you verified → the outcome.

1. **"How do you use AI day to day?"** One real example. Mention *how you verify*: tests, reading the diff, and a
   second prompt asking "what could break this?". Mention where it saved time (boilerplate, navigating a codebase,
   first drafts of tests) and where you took over.
2. **"When should engineers NOT use AI?"** Pick 3 and justify them:
   - security- or money-critical logic you can't fully verify quickly (auth, payments, pricing, PII handling);
   - when you don't understand the problem yet, because AI makes you fast in the wrong direction;
   - secrets or proprietary data in prompts;
   - a one-line fix you can see: typing is faster than prompting;
   - learning a core skill as a junior, where the struggle is the point.
3. **"How would you mentor a colleague who ships AI code they don't understand?"** No blame. Pair on one PR and ask
   them to walk through it line by line. Agree a rule ("you own every line you commit; if you can't explain it, it
   doesn't merge"). Ask for tests first, and review prompts as well as diffs.
   Point them to where AI shines for them (exploring unfamiliar code, generating test cases).
4. **"What would you do next with this codebase?"** A prioritised list: correctness → data integrity (FK + cleanup
   migration for orphans) → performance (N+1, pagination, indexes) → design → tests.

## Questions to ask them (pick 2–3)
- How does the team split work between search/discovery, booking and supplier tooling? Which one is this role?
- What does the path from PR to production look like, and how do you test booking flows before release?
- How do you handle inventory and availability consistency with suppliers' own systems?
- What does a good first 90 days look like for this role?
- How do engineers here use AI tools day to day?

## Phrases worth having ready
- "Before coding, let me restate the problem and list the edge cases I see…"
- "The simplest thing that works is X, which is O(n log n). If n gets large we can do Y. Shall I start simple?"
- "Let me reproduce it with the failing test first, so we know when it's fixed."
- "I'll refactor in small steps and rerun the characterization tests after each one."
- "Trade-off: a pessimistic lock is simple and correct but serialises bookings per activity; for a flash sale
  I'd consider an atomic conditional update."
