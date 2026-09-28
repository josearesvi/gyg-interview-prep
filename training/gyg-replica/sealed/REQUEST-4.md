# Interviewer, request 4 (≈ 8 min, mostly discussion)

> "Imagine 10 million activities. What breaks first, and how would you fix it?"

Cover: `findAll()` + filtering in memory, the N+1 on `supplier` (how many SQL queries does `GET /activities` run?
Prove it with `spring.jpa.show-sql=true` or Hibernate statistics), `LEFT JOIN FETCH`, `@Transactional(readOnly = true)`,
pagination, indexes on `title` / `supplier_id`, and `LIKE '%x%'` not using an index.
Hidden test `Request4Test.java`: `GET /activities` must run at most 2 SQL statements.
