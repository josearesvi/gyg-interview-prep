---
description: Code-review the current changes like a senior teammate, then run the tests
---
Review my uncommitted changes (`git diff` and `git status`) as a senior engineer in a pairing session.
Cover: correctness and edge cases, complexity, naming and readability, tests (missing cases), and framework pitfalls
(Spring/JPA: transactions, lazy loading, validation, N+1). Rank issues by importance and keep it short.
Don't rewrite my code. Point to `file:line` and let me fix it or ask you to.
Then run the relevant tests with the project's wrapper (`./mvnw` or `./gradlew`) and tell me the result.
