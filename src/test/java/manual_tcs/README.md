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
mvn test -Dsurefire.suiteXmlFiles=src/test/java/manual_tcs/testng.xml
```

- TC14-TC25 use search term `a`, present in the seed video titles, and verify the ordered results.
- The application search matches title substrings, not category names.
- TC12 verifies input focus/caret styling; Selenium cannot reliably assert the blink phase of a native caret.
- TC26 requires network access to external thumbnail hosts.
- The header displays 42 Videos, while the current backend seed data contains six video records. TC09 checks the header; TC26-TC29 check the six rendered records.
