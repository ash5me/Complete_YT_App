# MyTube

MyTube is a Spring Boot web application that displays a channel-style video library. The frontend is served from Spring Boot static resources, and video data is provided by the `/api/videos` REST endpoint.

## Technology stack

- Java 25
- Spring Boot 3.5.5
- Maven
- HTML, CSS, and JavaScript
- Selenium WebDriver 4
- TestNG 7
- WebDriverManager

## Prerequisites

Install the following before running the project:

- JDK 25
- Maven
- Google Chrome
- Git

Check your Java and Maven installations:

```bash
java -version
mvn -version
```

## Run the application

From the project root, start the Spring Boot application:

```bash
mvn spring-boot:run
```

Wait until the console reports that `MytubeApplication` has started successfully. The application is configured to use port **8080**.

Open the application:

- Frontend: http://localhost:8080/
- Video API: http://localhost:8080/api/videos

**Important:** Keep the Spring Boot application running while executing the Selenium tests. Starting Chrome through Selenium does not start the Spring Boot server. If the application is not running, Selenium will report `net::ERR_CONNECTION_REFUSED` when it tries to open `http://localhost:8080/`.

## Automated UI tests

The repository includes **29 Selenium + TestNG test cases** based on the manual test case workbook:

`src/test/java/manual_tcs/manual-tc.xlsx`

The automation source files are in the `automation` package:

```text
src/test/java/
└── automation/
    ├── homepage.java
    ├── ManualTCTest.java
    ├── ChannelPage.java
    └── testng.xml
```

- `ManualTCTest.java` contains test cases TC01-TC29.
- `ChannelPage.java` contains reusable page-object methods and locators.
- `testng.xml` runs all manual test groups as one TestNG suite.
- `homepage.java` contains the existing homepage tests.

### Test coverage

| Test group | Coverage |
|---|---|
| Navigation | Page icon reload and hover behavior |
| Theme | Theme toggle and light/dark icons |
| Channel profile | Avatar, channel name, subscriber count, and video count |
| Subscription | Subscribe button behavior and persistence after reload |
| Search | Search input focus and different input types |
| Filter and sort | All, Tutorial, Vlog, and Review categories with newest, oldest, and most-popular sorting |
| Video cards | Thumbnails, titles, view counts, and upload age |

### Run the TestNG suite using Maven

1. Start the application using `mvn spring-boot:run` in a first terminal.
2. Open a second terminal at the project root.
3. Execute:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/java/automation/testng.xml
```

For headless browser execution:

```bash
mvn test -Dheadless=true -Dsurefire.suiteXmlFiles=src/test/java/automation/testng.xml
```

To run against a different application URL:

```bash
mvn test -DbaseUrl=http://localhost:8080/ -Dsurefire.suiteXmlFiles=src/test/java/automation/testng.xml
```

The test code defaults to `http://localhost:8080/`. The `baseUrl` system property can override this value.

### Run from Eclipse

1. Import the repository as an existing Maven project.
2. Use **Maven > Update Project**.
3. Confirm `src/test/java` is configured as a test source folder.
4. Start `MytubeApplication.java` using **Run As > Spring Boot App**.
5. Right-click `src/test/java/automation/testng.xml`.
6. Select **Run As > TestNG Suite**.

If TestNG reports `Cannot find class in classpath: automation.ManualTCTest`, run **Project > Clean**, allow Eclipse to rebuild, and verify that `target/test-classes/automation/ManualTCTest.class` and `ChannelPage.class` exist.

## Notes about the current sample data

- The channel header displays **42 Videos**, while the current backend seed data contains **six** video records.
- The search feature matches text in video titles.
- The thumbnail images are loaded from external image URLs, so thumbnail-image tests require network access.
- The video cards' upload-age text depends on the current date.

## Useful commands

Start the application:

```bash
mvn spring-boot:run
```

Run the complete Selenium/TestNG suite:

```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/java/automation/testng.xml
```

Build the project without running tests:

```bash
mvn -DskipTests package
```
