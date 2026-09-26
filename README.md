# GetYourGuide backend interview prep (Java 21 · Spring Boot 3.5)

A practice environment that mirrors the **60-minute live pair-programming interview**: *"a simple project that
wasn't made with best practices in mind"*, where you **solve**, **debug**, **refactor** and **build**, in
**Java + Spring Boot**, with AI tools allowed.

## Levels

| Level | Folder | You practise | Starts as |
|-------|--------|-------------|-----------|
| 1 | [`level-1-algorithms`](level-1-algorithms/README.md) | Sorting, binary search, recursion, trees, graphs (BFS), hashmaps | 34 red tests |
| 2 | [`level-2-debugging`](level-2-debugging/README.md) | Finding 8 planted bugs from symptom-only tickets | 13 of 18 red |
| 3 | [`level-3-refactoring`](level-3-refactoring/README.md) | Safely refactoring a legacy god-controller (SQL injection, stale cache, …) | 9 green + 3 disabled |
| 4 | [`level-4-features`](level-4-features/README.md) | Pagination, idempotency, concurrency-safe booking, cancellation policy | 4 green + 11 disabled |
| - | [`docs/MOCK_INTERVIEW.md`](docs/MOCK_INTERVIEW.md) | A timed 60-minute dress rehearsal | |

`solutions/` holds a verified reference answer for every level. `./scripts/check-solutions.sh` applies them to a
temp copy and runs every test, so your working tree is never touched.

## Quick start

```bash
# prerequisites: Java 21 and git (see docs/SETUP.md). Maven is NOT needed: ./mvnw downloads it.
git clone <this repo> && cd gyg-interview-prep
./mvnw -q test-compile                  # first run downloads dependencies (~1-2 min)
./mvnw -pl level-1-algorithms test      # expect red: now go and make it green
claude                                  # start Claude Code at the project root
```

## Using Claude Code here
- Read **[docs/CLAUDE_CODE_GUIDE.md](docs/CLAUDE_CODE_GUIDE.md)**: navigating a codebase, drills, and what to do live.
- `CLAUDE.md` puts Claude in **coach mode**: it gives hints, not answers, and it won't open `solutions/` unless you ask.
- Custom slash commands in `.claude/commands/`:
  `/hint`, `/tour`, `/interviewer`, `/review-mine`, `/compare-solution`.

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
