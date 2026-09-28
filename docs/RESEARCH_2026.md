# What we know about the GetYourGuide backend coding interview (researched 2026-09-27)

Confidence: **Confirmed** = GetYourGuide's own material · **Reported** = a candidate report with a date ·
**Likely** = one candidate's prep material, not independently confirmed.

## Format
- **Confirmed** (the prep doc you received): 60 min = 5 intro, 50 live pair programming with 1–2 engineers,
  5 Q&A. *"A simple project that wasn't made with best practices in mind."* Java, Spring Boot. Copilot, Claude Code
  and other AI tools are allowed: *"the ability to choose the right tool for the right scenario."*
- **Reported** (Glassdoor, April 2026): recruiter call → **HackerRank test, 7 days to complete** ("Event
  Registration System": `addEvent`, `registerParticipant`, `cancelRegistration`, `findRegisteredEvents`) →
  technical interview with 2 interviewers. **The GitHub link to the codebase arrives 24 hours before.** They ask
  questions about the code and ask you to change it.
- **Reported** (same): questions about **your AI experience at work, tasks where engineers should not use AI,
  and mentoring colleagues on AI use**.

## The codebase
GetYourGuide's repo `getyourguide/swe-be-coding-interview` is now private (404). Two public copies exist:
- `Sandip1509/swe-be-coding-interview` (Feb 2025): the original starter. **Confirmed structure**: JDK 21, Spring Boot 3.4,
  **Gradle**, Lombok, Flyway, H2 *file* DB, package `com.getourguide.interview`, layers
  `controller/dto/entity/error/repository/service`, `GetYourGuideApplication`, and an IntelliJ-first README
  ("run the main method… leverage its debug tooling").
  - Domain: `Activity` and `Supplier`, with ~17 Berlin activities and 5 suppliers.
  - Endpoints: `/activities`, `/activities/{id}`, `/activities/search/{q}`, `/suppliers`, `/suppliers/search/{q}`.
- `raghava219/swe-be-coding-interview-solution` (15 Mar 2026): a candidate's solution for "Senior SWE – Java". It uses
  **Maven, Spring Boot 4**, adds `/suppliers/stats`, and includes a `cheat-sheet.html` of "possible questions" (**Likely**).

`training/gyg-replica/` is our clean-room replica of both versions: the same stack (Maven, Boot 4), domain, layout and
every issue below, but our own code and data.

## Issues planted in the real repo (and in our replica)
| Layer | Issue | The question you'll get |
|---|---|---|
| Service | Mapping copy-pasted 3×, `forEach` + `add` | "How do you remove the duplication? What's wrong with forEach here?" |
| Service | `findAll()` then filter in Java, even for one id | "What happens with 10 million rows?" |
| Service | `searchActivities` fills one list and returns another (always `[]`) | "What does this method actually return?" |
| Service | Null supplier handled in 1 of 3 methods, and the seed data has an orphaned `supplier_id` | "When does this crash?" |
| Service | Injects `SupplierController` | "What's wrong with a service depending on a controller?" |
| Service | No `@Transactional(readOnly = true)`, an N+1 on supplier | "How many queries for 100 activities?" |
| Service | Checked `ActivityNotFoundException` | "Why can't you throw it inside a lambda?" |
| Repository | `getSupplierStats()` is `SELECT *` into `List<Object[]>`; the DTO is in the wrong package | "Does this return statistics? What about the DTO field order in a JPQL constructor?" |
| Controller | `EntityManager` + a native query in the controller | "Why not here?" |
| Controller | Supplier search: concatenated fields, case-sensitive, first match only, falls back to ALL | "What does it return when nothing matches?" |
| Controller | `@Controller` instead of `@RestController`, a dead `@PathVariable` null check | "When is a path variable null?" |
| Error | A catch-all handler always returns 500 | "What happens to a 404?" |
| Entity | `@Data` on entities, bidirectional relations | "Why is @Data dangerous on JPA entities?" |
| Entity | `@NotFound(IGNORE)`, `GenerationType.AUTO`, primitives | "What does IGNORE do? AUTO vs IDENTITY? double vs Double?" |
| Tests | Controllers called directly, an empty test class, a dead mock | "@WebMvcTest vs @SpringBootTest vs @DataJpaTest?" |

In our replica, on Boot 4 / Jackson 3, `/suppliers` even returns **HTTP 200 with truncated JSON**. The recursion hits
Jackson's nesting limit after the status has already been sent.

## Sources
- GetYourGuide prep doc (the PDF you received)
- Glassdoor, GetYourGuide Senior Software Engineer interview (April 2026): https://www.glassdoor.com/Interview/GetYourGuide-Senior-Software-Engineer-Interview-Questions-EI_IE695237.0,12_KO13,37.htm
- https://github.com/Sandip1509/swe-be-coding-interview
- https://github.com/raghava219/swe-be-coding-interview-solution (see its `cheat-sheet.html`)
- Official (private now): https://github.com/getyourguide/swe-be-coding-interview
- Industry context: https://www.techinterview.org/post/3233475398/live-coding-interview-ai-allowed-2026/
