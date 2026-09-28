# Before the interview: you received the GitHub link 24 hours ago

Per candidate reports (Glassdoor, April 2026), GetYourGuide sends the repository link about 24 hours before the
technical round. Use that time like this (≈ 45 min, not more):

1. Clone it and open it in IntelliJ. Run `GetYourGuideApplication` (the gutter ▶) and the tests (⌃⇧R on `src/test`).
2. `curl` every endpoint (`/activities`, `/activities/{id}`, `/activities/search/{q}`, `/suppliers`,
   `/suppliers/search/{q}`, `/suppliers/stats`). Note what looks wrong: status codes, bodies, log errors.
3. `/onboard` in Claude Code. Then read every class yourself (there are only ~10).
4. Write a **private list of smells and bugs, by layer** (entity / repository / service / controller / error / tests).
   Rank them: *correctness → data/security → performance → design → tests*.
5. Prepare 3 questions for the interviewers (e.g. "Is the seed data representative, e.g. can supplier_id point to a
   missing supplier?", "Is the API contract of /activities/search fixed?").

**Do NOT fix anything in advance.** They want to watch you reason and change code live. Pre-fixed code wastes the
session and looks like you didn't listen. Your list is your cheat sheet, so keep it next to you.
