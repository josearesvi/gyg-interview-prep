---
name: explain-back
description: Before shipping, check that I can explain the change - three reviewer-style questions, one at a time
argument-hint: [file or topic to focus on]
---
Before this change ships, check that I can explain and defend it. Look at the current changes (`git diff`), or the
last commit if the tree is clean, focusing on $ARGUMENTS if given.

Ask me the 3 questions a thoughtful code reviewer would most likely ask, **one at a time**, and wait for my answer to each:
1. Why this approach over the obvious alternative?
2. Its complexity, performance or concurrency behaviour.
3. An edge case or failure mode it might not handle.

After each answer, say in a sentence or two what was right, what was missing, and a crisper way to say it.
Don't write any code in this skill.
