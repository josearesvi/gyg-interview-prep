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
