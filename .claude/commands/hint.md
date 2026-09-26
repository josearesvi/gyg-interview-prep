---
description: Get a progressive hint for an exercise, never the full solution
argument-hint: <exercise or test name, e.g. E3 or BUG-2 or Feature3>
---
The user is stuck on: $ARGUMENTS

Look at the relevant exercise, its test and the user's current code (use `git diff` to see what they changed).
Do NOT read `solutions/`. Give exactly ONE hint, the smallest nudge that unblocks them:
1. First time: a question that points to the right idea or edge case.
2. If they say they are still stuck: name the technique or the line to look at.
3. Only if they explicitly ask a third time: pseudo-code, still not Java.
End by asking them to say their next step out loud.
