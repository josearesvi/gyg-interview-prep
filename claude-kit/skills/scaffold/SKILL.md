---
name: scaffold
description: Start a new Spring Boot service with Spring Initializr and get to a first green test
argument-hint: <artifact-name> [maven|gradle] [extra dependencies]
disable-model-invocation: true
---
Scaffold a new Spring Boot service. Arguments: $ARGUMENTS
(defaults: Maven, Java 21, the latest stable Spring Boot, group `com.example`, dependencies
`web,validation,data-jpa,h2`, plus any extra ones listed).

Do this one step at a time, saying what each step does:
1. Generate it with Spring Initializr into a folder named after the artifact, in the CURRENT directory:
   `curl -fsSL https://start.spring.io/starter.zip -d type=<maven-project|gradle-project-kotlin> -d language=java -d javaVersion=21 -d groupId=com.example -d artifactId=<name> -d name=<name> -d packageName=com.example.<name_without_dashes> -d dependencies=<deps> -o <name>.zip && unzip -q <name>.zip -d <name> && rm <name>.zip`
   If start.spring.io is unreachable, write the minimal build file, application class and test by hand instead.
2. `cd` into it and run the generated context-load test with the wrapper. It must be green before any feature work.
3. `git init` and commit ("Scaffold from Spring Initializr").
4. Propose a package layout for what's in `TASKS.md` (if it exists). Default: package-by-feature, e.g.
   `wishlist/{WishlistController, WishlistService, WishlistItem, WishlistRepository}` plus `common/ApiExceptionHandler`.
   Wait for my OK.
5. IntelliJ: if the current folder has a `pom.xml` that already lists this project as a module, tell me to press ⌘⇧I
   (Load Maven Changes). Otherwise tell me to open the new folder (File → Open → Trust) and restart Claude Code there.

Don't implement any features in this skill.
