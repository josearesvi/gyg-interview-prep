---
name: review-mine
description: Senior-engineer review of the current changes, ranked by importance, then run the tests
---
Review my uncommitted changes (`git diff` and `git status`), or the last commit if the tree is clean, as a senior
engineer would in a code review. Cover correctness and edge cases, complexity, naming and readability, missing test
cases, and framework pitfalls (Spring/JPA: transactions, lazy loading, validation, N+1, HTTP status codes and the
API contract).

Rank the findings by importance and keep it short. Point to `file:line` and don't rewrite the code: I'll fix it or ask
you to. Then run the relevant tests with the project's wrapper (`./mvnw` or `./gradlew`) and report the result.
