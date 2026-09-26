# Walkthrough 1: from scratch (≈ 50 min, like the real session)

Scenario: *"Here are the requirements, please build it."* You'll get 3 requirement parts, just as an interviewer would
add follow-ups. The black-box acceptance tests play the interviewer's checks.

## Setup (≈ 3 min, not timed)
1. In the prep repo's IntelliJ terminal (⌥F12): `./scripts/start-training.sh from-scratch`
2. *File → Open…* → the printed `~/interview-sim/from-scratch-…` folder → **Open** → *Trust Project*
   (new window). Arrange the cockpit: terminal on the right.
3. ⌘Esc → `/interview-mode`

## Part 1 (≈ 20 min)
4. **The interviewer speaks**: in the *prep repo window* (⌘\`), open `training/from-scratch/sealed/REQUIREMENTS-1.md`.
   Read it **aloud**. Copy all of it (⌘A ⌘C).
5. Back in the training window (⌘\`): `/requirements ` + paste (⌘V) → ↩.
   Read Claude's *questions to ask* and pick 1–2 to "ask the interviewer". The answers are in the text, or say
   "I'll assume X". `TASKS.md` now exists: open it with ⇧⇧ `TASKS`.
6. `/scaffold wishlist-api maven` → Claude generates the project via Spring Initializr and runs the first test.
   Then **⌘⇧I** (load Maven changes) so IntelliJ picks up `wishlist-api` as a module.
7. Say your design: package-by-feature, in-memory store (you'll say what changes for a DB), BigDecimal for money.
8. Delegate in slices, reviewing each diff:
   - *"Add the request/response records with validation. Only those files."*
   - *"Service with an in-memory store: add, list, remove. Plus unit tests for it."* → ⌃⇧R on the test.
   - *"Controller for the three endpoints with the exact status codes from TASKS.md."*
9. Start the app: ⌃⌥R → **App: wishlist-api** (or ⌃⇧R inside `WishlistApiApplication`).
10. ⌃⌥R → **Acceptance: Part 1**. Red? Click the failing test and read expected vs actual, then tell Claude the
    *specific* failure: *"POST with a blank title returns 201, expected 400. Fix only that."*
11. Green → ⌘K commit "Part 1".

## Part 2 (≈ 12 min)
12. Open `sealed/REQUIREMENTS-2.md` → `/requirements ` + paste. Claude appends to `TASKS.md` and flags conflicts.
13. Decide yourself: 409 vs 422 for duplicates? Where does sorting happen? Why BigDecimal for the total?
14. Delegate → review → restart the app (⌃R on the app config, or the ⟳ in the Run window) → **Acceptance: Part 2** → commit.

## Part 3 (≈ 10 min)
15. `sealed/REQUIREMENTS-3.md` → `/requirements`. Unguessable ids: *you* name the approach (SecureRandom, 128 bits).
16. **Acceptance: all parts** → green → commit.
17. The discussion questions at the bottom of part 3: answer them aloud (scaling to 3 instances, DB schema, scraping).

## Debrief (5 min)
- `/quiz-me` on the service class.
- `/review-mine` on the last commit.
- Compare with `solutions/training/from-scratch/wishlist-api` in the prep repo.
