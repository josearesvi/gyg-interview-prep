# Interviewer, request 5 (≈ 5 min)

> "How confident are you in the test suite? What would you change?"

Cover: tests that call controllers directly (no HTTP status, no serialization), the empty `SupplierControllerTest`,
the dead `SupplierController` mock, `@WebMvcTest` vs `@SpringBootTest` vs `@DataJpaTest`, and tests against a
**file** H2 database that keeps state between runs. Write one proper MockMvc test live.
