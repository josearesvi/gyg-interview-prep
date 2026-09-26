---
description: Guided tour of a module, to practise navigating an unfamiliar codebase
argument-hint: <module, e.g. level-2-debugging>
---
Give me a guided tour of `$ARGUMENTS`, like a teammate onboarding me in the first 5 minutes of a pairing session:
1. Build setup (pom, dependencies worth knowing) in 2-3 lines.
2. The package layout and the role of each class, as a compact tree.
3. Trace ONE request end-to-end (e.g. POST /bookings): controller → service → repository → DB, citing
   `file:line` for each hop.
4. Where the tests live and how to run a single one.
5. Three questions I should ask the interviewer about this code.
Do not point out the planted bugs or smells; I want to find them myself. Do not open `solutions/`.
