---
name: assess-notes
description: Evaluate my inline review notes (comments tagged @review) - check each suggestion against best practices and against how this repository already handles the same case, then recommend one approach with a justification
argument-hint: [path or file:lines] [tag] (default: whole repo, notes tagged @review)
---
Evaluate the inline review notes I've left in the code. Arguments: $ARGUMENTS
**Read only: make no edits.** Cite `file:line` for everything. Never assume how the repo handles something: search.

## Finding the notes
- A note is a comment tagged **`@review`**, e.g. `// @review This should be a validation error, not a not-found`.
  Consecutive `@review` lines form one note. (`# @review` and `<!-- @review -->` also count. If the arguments name a
  different tag, use that one.)
- A note applies to the code that follows it: the next statement, block or method, up to the next blank line or
  closing brace. If a note sits above a method signature, it applies to the whole method.
- If the arguments give a file or `file:lines` range, look only there, and treat **every** comment in that range as
  a note, tagged or not. Otherwise search the whole repository (main and test code).
- If there are no notes, say so, and remind me of the format above.
- Number the notes in file order. If one note makes several claims, split it (2a, 2b…).

## For each note
**A. The note.** Quote it, give `file:line` and a one-line summary of the code it points at. State each claim it
makes in one sentence ("The author claims: …").

**B. Best-practice check.** For each claim, give a verdict:
- ✅ **Agree**, ⚠️ **Partly**, ❌ **Disagree**, or ❓ **Depends on a product decision** (then write the question and
  the default you'd assume).
- One short reason, naming the principle (HTTP semantics and status codes, Spring/JPA/Java idiom, layering,
  testability, security, performance). If a technical premise in the note is wrong, say so plainly and kindly.
- At most one line on anything important the note misses about the same code.

**C. What this repository does elsewhere.** Search for the same **case**, not the same text: the same kind of
operation, error, validation, mapping, query, transaction or log statement, in main and test code. Report:
- **Occurrences**: a count and up to 6 `file:line` examples (then "+N more").
- **Classification**:
  - **Standard**: one approach in roughly 75% or more of the occurrences. Name it.
  - **Split**: two or more approaches. List each with its count and examples, and say which one is newer, more
    complete or better supported by tests.
  - **No precedent**: this is the only place.
- **Already decided by**: framework configuration, a shared handler or base class, or a global setting that
  already determines this.
- **Tests**: which tests pin the current behaviour (a change would break them) and which cases have no test.
- **Fit**: does the note's suggestion follow the repo's approach, diverge from it, or introduce a third way?

**D. Recommendation.** Pick one approach and justify it against these criteria: correct contract and behaviour,
consistency with the repo, blast radius (call sites, API contract, tests to change), effort (S/M/L), and risk.
- If the repo's standard is itself a bad practice, say so: recommend the better approach, and propose a migration
  path (fix here now and list the other places, or adopt the better one going forward).
- If it's a product decision, give the question to ask and a default so work isn't blocked.
- End with how to verify it: the test to write or run.

## At the end
1. **Summary table**: `# | Note (short) | file:line | Verdict | Repo precedent | Recommendation | Effort`.
2. **Suggested order**: which to apply first, and dependencies (a fix for one note that changes another).
3. **Patterns across notes**: problems that recur (duplicated logic, the same convention broken in several places).
   These are good targets for one shared fix or a documented convention.
4. **Where my judgement was strongest, and where it deserves a second look**, in two lines.

Keep each note's evaluation under about 25 lines. Then wait for me to choose. I'll use `/plan-first` for the changes.
