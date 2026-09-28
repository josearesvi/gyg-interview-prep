---
name: requirements
description: Turn new requirements from the team into clarifying questions, acceptance criteria, a step plan and a TASKS.md checklist
argument-hint: <paste the requirements>
---
New requirements from the team:

<requirements>
$ARGUMENTS
</requirements>

Don't write production code yet. Give me:

1. **Restatement**: 2–3 plain sentences I can read back to confirm we understood it the same way.
2. **Clarifying questions**: at most 5, most important first (validation rules, status codes, ordering, limits,
   persistence, concurrency, security…). For each, the default we'd go with if the answer is "your call".
3. **Acceptance criteria**: a numbered list of concrete, testable statements (method, path, status code, body shape,
   edge cases).
4. **Step plan**: small steps, each ending in a green test. Mark which steps are design decisions for me and which
   are routine work you can take on.
5. **`TASKS.md`** at the repo root:
   - If it doesn't exist: a `## Requirements 1` section with the criteria as `- [ ]` checkboxes, plus the steps.
   - If it exists: APPEND `## Requirements <n+1>`. Don't rewrite earlier sections, and point out anything that
     changes or conflicts with them.

Keep it short enough to read aloud in under a minute. Then wait.
