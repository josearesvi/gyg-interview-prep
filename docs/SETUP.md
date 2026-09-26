# Setting up your machine

You need **Java 21**, **git**, an **IDE**, and **Claude Code**. Maven comes with the project (`./mvnw`).

## 1. Java 21 (JDK)

**macOS** (Homebrew):
```bash
brew install openjdk@21
sudo ln -sfn "$(brew --prefix)/opt/openjdk@21/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-21.jdk
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc && source ~/.zshrc
```

**Any OS, with several JDK versions** ([SDKMAN!](https://sdkman.io), macOS/Linux/WSL):
```bash
curl -s "https://get.sdkman.io" | bash
sdk install java 21-tem
```

**Ubuntu / Debian / WSL**: `sudo apt install openjdk-21-jdk`

**Windows**: install *Eclipse Temurin 21* from adoptium.net (tick "Set JAVA_HOME"), then use WSL or Git Bash
for the commands. On plain Windows cmd, use `mvnw.cmd` instead of `./mvnw`.

Check it: `java -version` must say **21**.

## 2. Build the project once
```bash
./mvnw -q test-compile          # downloads Maven + Spring Boot deps into ~/.m2 (only the first time)
./mvnw -pl level-4-features test   # should be green: 4 run, 11 skipped
```
Do this **the day before the interview** too, so dependencies are cached and nothing surprises you on the call.

## 3. IDE (recommended: IntelliJ IDEA)
IntelliJ IDEA (Community is free) → *Open* → pick the root `pom.xml` → *Open as Project*. It imports all
four modules. Set *Project SDK* to 21. Learn these shortcuts (macOS / Windows-Linux):

| Action | macOS | Win/Linux |
|--------|-------|-----------|
| Search everywhere | ⇧⇧ | Shift Shift |
| Go to class / file | ⌘O / ⌘⇧O | Ctrl+N / Ctrl+Shift+N |
| Go to definition / implementations | ⌘B / ⌘⌥B | Ctrl+B / Ctrl+Alt+B |
| Find usages | ⌥F7 | Alt+F7 |
| Run test at cursor | ⌃⇧R | Ctrl+Shift+F10 |
| Debug test at cursor | ⌃⇧D | right-click → Debug |
| Rename / extract method | ⇧F6 / ⌘⌥M | Shift+F6 / Ctrl+Alt+M |

VS Code works too: install the "Extension Pack for Java" and "Spring Boot Extension Pack".

## 4. Claude Code
```bash
# native installer (macOS / Linux / WSL)
curl -fsSL https://claude.ai/install.sh | bash
# or, with Node 18+:  npm install -g @anthropic-ai/claude-code
cd gyg-interview-prep && claude     # log in the first time
```
Optional: install the Claude Code plugin for IntelliJ or VS Code (`/ide` inside Claude Code connects them) so
Claude sees the file and selection you have open.

## 5. Interview-day checklist
- [ ] `java -version` → 21. The interviewers' project builds (`./mvnw test` or `./gradlew test`: they may use Gradle)
- [ ] IDE has indexed the project; you can run *and debug* one test
- [ ] Claude Code logged in; you've started it in the project folder once
- [ ] Screen sharing works with the IDE and terminal visible; font size bumped up
- [ ] Notifications off; water nearby
