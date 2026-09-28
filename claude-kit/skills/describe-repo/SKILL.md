---
name: describe-repo
description: High-level description of a repository - purpose, entry points, outputs, storage, and how the business layer works - without building or changing anything
argument-hint: [path or module, default: the whole repository]
---
Describe this repository (scope: $ARGUMENTS, default: the whole repo) at a level a new team member can follow in
five minutes. **Read only. Don't build, run or change anything.** Cite `file:line` for every concrete claim.

1. **Purpose**: 2–3 sentences: what problem it solves, and for whom.
2. **Context**: a small text diagram of who calls this system and what it calls, e.g.
   `mobile app → [this service] → DB, payment API`.
3. **Entry points**: everything that can start work, in a table (type, trigger, handler `file:line`):
   HTTP endpoints, scheduled jobs (`@Scheduled`, cron), message/event listeners, `main`/CLI, startup runners
   (`CommandLineRunner`, `@PostConstruct`), and web or UI pages if any.
4. **Outputs**: what leaves the system: response types, published events/messages, emails, files, outbound HTTP calls.
5. **Storage**: databases and tables/entities (with their relations), migrations (Flyway/Liquibase), caches, files,
   and where the configuration for each lives.
6. **Business layer**: how it actually works:
   - the core domain objects and what each represents;
   - the 2–4 key flows as numbered steps (entry point → rules applied → state changes → outputs);
   - the **business rules and invariants** (limits, validations, state transitions, calculations) and **where each
     is enforced** (`file:line`), noting any rule enforced in more than one place or not at all.
7. **External dependencies and configuration**: frameworks and versions, external services, and required config
   (env vars, properties).
8. **Glossary**: domain terms, one line each.
9. **Unsure about**: anything you inferred rather than saw, as questions for the code's owners.

Keep it under about 60 lines, as tables and bullets. For build and run status, point to `/onboard`.
