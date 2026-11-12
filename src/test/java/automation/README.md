# Manual test case automation (TC01-TC29)

This package automates the 29 cases in `manual-tc.xlsx` with Java, Selenium 4, and TestNG.

## Prerequisites

- JDK 25 (the repository's pom.xml sets java.version to 25)
- Maven
- Google Chrome
- MyTube running at http://localhost:8080/

The repository already declares Selenium Java, WebDriverManager, and TestNG.

## Run

Start the application from the repository root:

```bash
mvn spring-boot:run
```

In another terminal:

```bash
mvn test
```

For headless execution or a custom application URL:

```bash
mvn test -Dheadless=true -DbaseUrl=http://localhost:8080/
```

To select the explicit suite file:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/java/automation/testng.xml
```

- TC14-TC25 use search term `a`, present in the seed video titles, and verify the ordered results.
- The application search matches title substrings, not category names.
- TC12 verifies input focus/caret styling; Selenium cannot reliably assert the blink phase of a native caret.
- TC26 requires network access to external thumbnail hosts.
- The header displays 42 Videos, while the current backend seed data contains six video records. TC09 checks the header; TC26-TC29 check the six rendered records.

## Eclipse / RemoteTestNG troubleshooting

If RemoteTestNG reports `Cannot find class in classpath: automation.ManualTCTest`:

1. Confirm these files exist in the local project:
   - `src/test/java/automation/ManualTCTest.java`
   - `src/test/java/automation/ChannelPage.java`
   - `src/test/java/automation/testng.xml`
2. Confirm the first line of both Java files is `package automation;`.
3. In Eclipse, use **Maven > Update Project** (Alt+F5), select the project, and update it.
4. Use **Project > Clean**, then allow Eclipse to rebuild.
5. Confirm `target/test-classes/manual_tcs/ManualTCTest.class` and `ChannelPage.class` are generated.
6. Right-click `src/test/java/automation/testng.xml` and choose **Run As > TestNG Suite**.

The project's `.classpath` already declares `src/test/java` as a test source folder. If it is missing from the Eclipse build path locally, add it through **Project > Properties > Java Build Path > Source > Add Folder**.

The suite uses `parallel="none"`, which is the supported non-parallel value in current TestNG versions.
