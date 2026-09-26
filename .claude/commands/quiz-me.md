---
description: Quiz me on the code you (Claude) just wrote, so I can defend it in the interview
argument-hint: <optional file or topic>
---
Look at the current uncommitted changes (`git diff`), focusing on $ARGUMENTS if given. Ask me the 3 questions
an interviewer is most likely to ask about this code, ONE at a time, and wait for my answer to each:
why this approach over an alternative, its complexity or concurrency behaviour, and an edge case it might miss.
After each answer, say briefly what was right, what was missing, and how to phrase it better out loud.
Don't write any code in this command.
