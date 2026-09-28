---
name: onboard
description: Get oriented in an unfamiliar codebase fast - build, structure, API, data model, one request traced, risks, open questions
---
I'm new to this codebase. Help me get oriented fast, **without changing any code**.

1. **Build**: the build tool (Maven `pom.xml` / Gradle `build.gradle(.kts)`), the wrapper, Java and framework versions.
   Run the tests once with the wrapper in quiet mode. Report whether it builds, how many tests there are and how many
   fail, and any **setup problem** (a missing property or env var, the wrong JDK, a port in use, a missing service).
   Say what it needs; don't fix it yet.
2. **Structure** (a compact tree): modules → packages → key classes, one line each.
3. **API**: a table of endpoints (method, path, handler `file:line`).
4. **Data**: entities or tables, their relations, and where the persistence and migration config lives.
5. **One request traced** end to end (controller → service → repository → DB), with `file:line` for each hop.
6. **Risks**: at most 5 things that look risky or unusual (correctness, data integrity, performance, error handling,
   tests), as observations only.
7. **Open questions**: 3 questions worth asking the people who own this code.

Keep it scannable, under about 40 lines. If there's no CLAUDE.md, mention that `/init` can create one later.
