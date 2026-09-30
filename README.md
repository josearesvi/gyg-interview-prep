# GetYourGuide backend interview prep (Java 21 · Spring Boot 3.5)

A practice environment that mirrors the **60-minute live pair-programming interview**: *"a simple project that
wasn't made with best practices in mind"*, where you **solve**, **debug**, **refactor** and **build**, in
**Java + Spring Boot**, with AI tools allowed.

## Two kinds of practice

**Levels** build skills, one at a time:

| Level | Folder | You practise | Starts as |
|-------|--------|-------------|-----------|
| 1 | [`level-1-algorithms`](level-1-algorithms/README.md) | Sorting, binary search, recursion, trees, graphs (BFS), hashmaps, the HackerRank-style E7 | 42 red tests |
| 2 | [`level-2-debugging`](level-2-debugging/README.md) | Finding 8 planted bugs from symptom-only tickets | 13 of 18 red |
| 3 | [`level-3-refactoring`](level-3-refactoring/README.md) | Safely refactoring a legacy god-controller (SQL injection, stale cache, …) | 9 green + 3 disabled |
| 4 | [`level-4-features`](level-4-features/README.md) | Pagination, idempotency, concurrency-safe booking, cancellation policy | 4 green + 11 disabled |

**Training projects** simulate interview day, with requirements handed to you in parts:

| Training | You get | Build | Checked by | Guide |
|----------|---------|-------|------------|-------|
| A: from scratch | Requirements (3 parts) → build a "wishlist" API with `/scaffold` | Maven | 17 black-box HTTP acceptance tests | [WALKTHROUGH_1](docs/WALKTHROUGH_1.md) |
| B: handed-over code | A "tour-inventory" service with a setup snag, then 3 requests (bug, feature, under-load) | **Gradle** | its own tests + the ones you add | [WALKTHROUGH_2](docs/WALKTHROUGH_2.md) |
| **C: GetYourGuide replica** | A clean-room copy of **GetYourGuide's real interview repo** (activities & suppliers, Boot 4, ~20 planted issues) + the 24h-before prep | Maven | 12 sealed "interviewer" tests | [WALKTHROUGH_3](docs/WALKTHROUGH_3.md) |

**Start here: [PASS_1](docs/PASS_1.md)**, the guided pass through every exercise using the pairing toolkit.
[WALKTHROUGH_0](docs/WALKTHROUGH_0.md) teaches the loop and the IntelliJ hotkeys on Exercise 1.
What we know about the real interview, with sources: [RESEARCH_2026](docs/RESEARCH_2026.md).
The technical depth the newer exercises expect (REST contracts, Spring, JPA, concurrency): [TECH_FUNDAMENTALS](docs/TECH_FUNDAMENTALS.md).

`solutions/` has a verified reference answer for everything. `./scripts/check-solutions.sh` and
`./scripts/check-training.sh` prove them in temp copies, so your working tree is never touched.

## Quick start

```bash
# prerequisites: Java 21 and git (see docs/SETUP.md). Maven is NOT needed: ./mvnw downloads it.
# IntelliJ: File > New > Project from Version Control > git@github.com:josearesvi/gyg-interview-prep.git
./mvnw -q test-compile                  # first run downloads dependencies (~1-2 min)
./mvnw -pl level-1-algorithms test      # expect red: now go and make it green
./scripts/install-claude-kit.sh         # once: the portable Claude commands
# ⌘Esc in IntelliJ starts Claude Code at the project root
```

## Using Claude Code here
- Read **[docs/CLAUDE_CODE_GUIDE.md](docs/CLAUDE_CODE_GUIDE.md)**: navigating a codebase, drills, and what to do live.
- **IntelliJ as the cockpit** (Claude plugin, layout, ★ hotkeys): [docs/INTELLIJ_COCKPIT.md](docs/INTELLIJ_COCKPIT.md)
- **Install your pairing toolkit once**: `./scripts/install-claude-kit.sh`. It installs `/pair-mode`, `/describe-repo`, `/onboard`, `/best-practices-review`, `/assess-notes`, `/requirements`, `/investigate`, `/scaffold`, `/plan-first`, `/review-mine`, `/explain-back` and `/wrap-up` as
  user-level skills in `~/.claude/skills`, so they work in any project, including the one you're handed. They're
  worded as a professional pairing workflow you can openly switch on (see "Presenting your toolkit" in
  `docs/AI_PAIRING_PLAYBOOK.md`).
- **AI is allowed in the interview**, so practise *steering* it: **[docs/AI_PAIRING_PLAYBOOK.md](docs/AI_PAIRING_PLAYBOOK.md)**.
- `CLAUDE.md` puts Claude in **pair mode**: small reviewable changes, tests run after each one, trade-offs
  named, `solutions/` off-limits unless you ask.
- Practice-only commands in `.claude/commands/`: `/tour`, `/hint`, `/interviewer`, `/compare-solution`.

## Useful commands

| Command | What it does |
|---------|--------------|
| `./mvnw test` | Everything. Stops at level 1 while it is red; add `-fae` to keep going |
| `./mvnw -pl level-2-debugging test` | One module |
| `./mvnw -pl level-2-debugging test -Dtest=PricingServiceTest` | One test class |
| `./mvnw -pl level-2-debugging test -Dtest='PricingServiceTest#totalIsExactToTheCent'` | One test method |
| `./mvnw -pl level-4-features spring-boot:run` | Run an API (ports 8082/8083/8084); H2 console at `/h2-console` |
| `./scripts/check-solutions.sh [module]` | Prove the reference solutions pass |
| `git stash` / `git checkout -- <module>` | Reset a level to practise it again |
