---
description: Start a Spring Boot project from scratch (Spring Initializr via curl) and get to a first green test
argument-hint: <artifact name, e.g. wishlist-api> [maven|gradle] [extra deps]
---
Scaffold a new Spring Boot project for an interview. Arguments: $ARGUMENTS
(defaults: Maven, Java 21, latest stable Spring Boot 3.x, group `com.example`, dependencies
`web,validation,data-jpa,h2`, plus any extra ones I listed).

Steps, one at a time, telling me what each one does:
1. Generate it with Spring Initializr into a folder named after the artifact, in the CURRENT directory:
   `curl -fsSL https://start.spring.io/starter.zip -d type=<maven-project|gradle-project-kotlin> -d language=java -d javaVersion=21 -d groupId=com.example -d artifactId=<name> -d name=<name> -d packageName=com.example.<name_without_dashes> -d dependencies=<deps> -o <name>.zip && unzip -q <name>.zip -d <name> && rm <name>.zip`
   If start.spring.io is unreachable, create the minimal pom/build file, application class and test by hand instead.
2. `cd` into it and run the generated context-load test with the wrapper. It must be green before we write any code.
3. Initialise git and make the first commit ("Scaffold from Spring Initializr").
4. Propose a package layout for the requirements in `TASKS.md` (if present). Default: package-by-feature, e.g.
   `wishlist/{WishlistController, WishlistService, WishlistItem, WishlistRepository}` plus `common/ApiExceptionHandler`.
   Wait for my OK.
5. IntelliJ sync: if the folder I'm in has a `pom.xml` that already lists this project as a module (a training
   aggregator), tell me to press ⌘⇧I (Load Maven Changes). Otherwise tell me to open the new folder in IntelliJ
   (File → Open → the folder → Trust) and relaunch Claude Code there (⌘Esc).

Don't implement any features in this command.
