---
description: Paste the interviewer's requirements; get questions to ask, acceptance criteria and a step plan in TASKS.md
argument-hint: <paste the requirements text>
---
The interviewer just gave me these requirements:

<requirements>
$ARGUMENTS
</requirements>

Do the following, and do NOT write any production code yet:

1. **Restate** the requirement in 2–3 plain sentences (I'll read it back to the interviewer to confirm).
2. **Questions to ask**: list the ambiguities (validation rules, error codes, ordering, limits, persistence,
   concurrency, auth…), max 5, most important first. For each, give the default you'd assume if the answer is "your call".
3. **Acceptance criteria** as a numbered list of concrete, testable statements (HTTP method, path, status code,
   body shape, edge cases).
4. **Step plan**: small steps, each ending in a green test. Mark which steps I should decide or write myself
   (design choices) and which are boilerplate you can take.
5. Write or update **`TASKS.md`** at the repo root:
   - If it doesn't exist: a `## Requirements <n>` section with the criteria as `- [ ]` checkboxes and the steps.
   - If it exists (follow-up requirements): APPEND a new `## Requirements <n+1>` section. Don't rewrite earlier
     sections, and point out anything in the new requirements that changes or conflicts with earlier ones.

Keep the whole answer short enough to read aloud in under a minute. Then wait for me.
