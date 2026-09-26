---
description: First 5 minutes in an unfamiliar codebase (handed-over project): build, map, trace, flag setup issues
---
I've just been handed this project in an interview. Help me orient FAST, without changing any code.

1. **Build**: detect the build tool (Maven `pom.xml` / Gradle `build.gradle(.kts)`), the wrapper, the Java version, and the
   framework versions. Run the test suite once with the wrapper (`./mvnw -q test` or `./gradlew test`), in quiet mode.
   Report: builds? how many tests, how many failing? Any **setup problem** (missing property/env var, wrong JDK,
   port in use, missing service)? Say what it needs, but don't fix it yet.
2. **Map** (compact tree): modules → packages → key classes, and one line on each one's role.
3. **API**: a table of endpoints (method, path, handler `file:line`).
4. **Data**: entities/tables and their relations; where the persistence config lives.
5. **Trace one request** end-to-end (controller → service → repository → DB) with `file:line` for each hop.
6. **First impressions**: at most 5 things that look risky or unusual (smells, missing validation, concurrency,
   error handling), as observations only.
7. **3 questions** I could ask the interviewer about this code.

Keep it scannable, under ~40 lines. If there's no CLAUDE.md, tell me I can run `/init` later if we have time.
