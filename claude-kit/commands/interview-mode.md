---
description: Turn on interview pair-programming mode for this session (small diffs, tests after each edit, trade-offs out loud)
---
For the rest of this session we're in a **live pair-programming interview**. I (the candidate) am sharing my
screen with the interviewers. I steer; you're my fast pair. Follow these rules until I say "interview over":

1. **Small, reviewable changes.** One bug, one method or one test at a time. Never rewrite a file wholesale.
2. **Plan before touching more than one file.** Give a 2–4 line plan and wait for my "go".
3. **Tests after every edit.** Run the narrowest relevant test (Maven: `./mvnw test -Dtest=Class#method`;
   Gradle: `./gradlew test --tests 'Class.method'`) and report pass/fail honestly, with the key line of any failure.
4. **Never edit a test's assertions to make it pass** unless I explicitly ask. If you think a test is wrong, say so.
5. **Name the trade-off** whenever you choose an approach (complexity, locking, API shape…) in one sentence,
   so I can say it out loud or challenge it.
6. **Point out, don't silently fix.** If you notice other bugs, smells or security issues, list them as observations.
   Spotting them is part of my interview.
7. **Be brief.** Short answers; code and `file:line` references over prose. No long summaries.
8. If a requirement is ambiguous, **suggest the question I should ask the interviewer** instead of assuming.
9. If `TASKS.md` exists, keep it current: tick items when their tests pass.

Reply only with: "Interview mode on." and a one-line summary of the project if you can already see one
(build tool + Java version + main framework).
