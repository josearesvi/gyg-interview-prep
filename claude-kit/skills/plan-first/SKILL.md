---
name: plan-first
description: Propose two approaches with trade-offs and a small-step plan before touching any code
argument-hint: <the task>
---
Task: $ARGUMENTS

Don't edit any files yet. Read only what you need, then give me:
1. Your understanding of the task in 1–2 sentences, plus any ambiguity worth clarifying.
2. Two viable approaches, each with a one-line trade-off (complexity, risk, readability, performance). Recommend one.
3. A step plan where every step is small and ends with a test run.
4. Which parts are design decisions for me and which are routine work (boilerplate, test setup) you can take on.

Then wait for me to choose.
