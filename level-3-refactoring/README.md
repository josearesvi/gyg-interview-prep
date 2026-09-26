# Level 3: Refactor legacy code safely

The doc warns: *"We will share a simple project that **wasn't made with best practices in mind**."* This level
is that project. `ReviewController.java` is a 180-line god class that does HTTP, validation, SQL, business rules
and caching in one place.

## Smells to find (try to list them yourself before you read on)
<details><summary>Show the list</summary>

- SQL built by string concatenation → **SQL injection** and crashes on `It's great`
- `select max(id)` to get the new id → race condition under concurrent inserts
- `static HashMap` cache: never invalidated (stale ratings), not thread-safe, unbounded
- Field injection (`@Autowired` on a field), with no constructor
- `Map<String, Object>` in and out, so there's no visible API contract or typed validation
- Nested if/else "arrow code" for validation instead of Bean Validation (`@Valid`)
- Business rules (flagging "scam"/"fraud", averages) inside the controller
- `catch (Exception e)` everywhere → returns `e.getMessage()` to clients (**information leak**), `printStackTrace`
- `System.out.println` instead of a logger
- Averages computed in Java instead of SQL `AVG()`
</details>

## The task
1. Run the tests: **9 green, 3 skipped**. The green ones are *characterization tests*: they pin down what the API
   does today. They must stay green after **every** step.
2. Refactor in small, safe steps: extract a repository with bind parameters → a service → typed DTOs with
   `@Valid` → a `@RestControllerAdvice` → constructor injection.
3. Remove `@Disabled` from the three tests in `KnownBugs` one at a time as your refactor makes each fix easy.
4. Add at least one fast **unit** test that needs no Spring context.

```bash
./mvnw -pl level-3-refactoring test
./mvnw -pl level-3-refactoring spring-boot:run   # http://localhost:8083/activities/1/reviews
curl "localhost:8083/reviews/search?q=zzz'%20OR%20flagged%20=%20true%20OR%20comment%20like%20'"  # the injection, live
```

Interview tip: say *"First I'll make sure there are tests, then refactor in small steps, running them each
time."* That sentence alone signals seniority.

Reference refactor: `solutions/level-3-refactoring/`.
