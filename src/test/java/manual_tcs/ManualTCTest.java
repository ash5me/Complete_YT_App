package manual_tcs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Selenium + TestNG automation for TC01-TC29 in manual-tc.xlsx. */
public class ManualTCTest {
    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8080/");
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);
    private static final Set<String> EXPECTED_VIDEO_TITLES = Set.of(
            "Beach day vlog", "Millionaire yatch vlog", "Skyscraper everyday fit",
            "Life of an farmer", "Solo traveller guide", "Dark forest alone in the dark?"
    );

    private WebDriver driver;
    private WebDriverWait wait;
    private ChannelPage page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000", "--disable-notifications",
                "--disable-popup-blocking", "--disable-dev-shm-usage", "--no-sandbox");
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        page = new ChannelPage(driver, wait);
        page.open(BASE_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test(description = "TC01 - MyTube page icon reloads the page", groups = {"navigation", "smoke"})
    public void tc01_pageIconReloadsPage() {
        Assert.assertEquals(page.navigationType(), "navigate");
        page.clickPageIcon();
        wait.until(d -> "reload".equals(page.navigationType()));
        Assert.assertEquals(page.channelName(), "Developer Channel");
        Assert.assertEquals(page.navigationType(), "reload");
    }

    @Test(description = "TC02 - Page icon has a hover highlight", groups = {"navigation"})
    public void tc02_pageIconHighlightsOnHover() {
        String originalBackground = page.pageIconBackground();
        new Actions(driver).moveToElement(page.pageIcon()).perform();
        wait.until(d -> page.isPageIconHovered()
                && !originalBackground.equals(page.pageIconBackground()));
        Assert.assertTrue(page.isPageIconHovered());
        Assert.assertEquals(page.pageIconCursor(), "pointer");
        Assert.assertNotEquals(page.pageIconBackground(), originalBackground,
                "The page icon background should change on hover.");
    }

    @Test(description = "TC03 - Bulb icon renders on the theme toggle", groups = {"theme"})
    public void tc03_bulbIconIsRendered() {
        Assert.assertTrue(page.isFontAwesomeIconRendered(".theme-toggle-icon .fa-lightbulb"));
    }

    @Test(description = "TC04 - Moon icon renders on the theme toggle", groups = {"theme"})
    public void tc04_moonIconIsRendered() {
        page.clickThemeToggle();
        Assert.assertTrue(page.isThemeToggleSelected());
        Assert.assertTrue(page.isFontAwesomeIconRendered(".theme-toggle-icon .fa-moon"));
    }

    @Test(description = "TC05 - Theme toggle changes the site theme", groups = {"theme", "smoke"})
    public void tc05_themeToggleChangesSiteTheme() {
        String themeBefore = page.currentTheme();
        boolean toggleBefore = page.isThemeToggleSelected();
        page.clickThemeToggle();
        wait.until(d -> !page.currentTheme().equals(themeBefore));
        Assert.assertNotEquals(page.currentTheme(), themeBefore);
        Assert.assertEquals(page.isThemeToggleSelected(), !toggleBefore);
    }

    @Test(description = "TC06 - Channel profile picture renders", groups = {"channel-profile"})
    public void tc06_profilePictureIsRendered() {
        wait.until(d -> page.isImageLoaded(page.channelAvatar()));
        Assert.assertTrue(page.channelAvatar().isDisplayed());
        Assert.assertTrue(page.imageNaturalWidth(page.channelAvatar()) > 0);
    }

    @Test(description = "TC07 - Channel name is displayed", groups = {"channel-profile", "smoke"})
    public void tc07_channelNameIsDisplayed() {
        Assert.assertEquals(page.channelName(), "Developer Channel");
    }

    @Test(description = "TC08 - Subscriber count is displayed", groups = {"channel-profile"})
    public void tc08_subscriberCountIsDisplayed() {
        String stats = page.channelStats();
        Assert.assertTrue(stats.contains("1.2M subscribers"), "Actual header text: " + stats);
    }

    @Test(description = "TC09 - Total channel video count is displayed", groups = {"channel-profile"})
    public void tc09_totalVideoCountIsDisplayed() {
        String stats = page.channelStats();
        Assert.assertTrue(stats.contains("42 Videos"), "Actual header text: " + stats);
    }

    @Test(description = "TC10 - Subscribe button changes status", groups = {"subscription"})
    public void tc10_subscribeButtonChangesStatus() {
        String statusBefore = page.subscribeButtonText();
        page.clickSubscribe();
        wait.until(d -> !page.subscribeButtonText().equals(statusBefore));
        Assert.assertNotEquals(page.subscribeButtonText(), statusBefore);
        Assert.assertTrue(List.of("Subscribe", "Subscribed").contains(page.subscribeButtonText()));
    }

    @Test(description = "TC11 - Subscribe status persists after reload", groups = {"subscription"})
    public void tc11_subscribeStatusPersistsAfterReload() {
        page.clickSubscribe();
        String statusBeforeReload = page.subscribeButtonText();
        driver.navigate().refresh();
        page.waitForGrid();
        wait.until(d -> page.subscribeButtonText().equals(statusBeforeReload));
        Assert.assertEquals(page.subscribeButtonText(), statusBeforeReload);
    }

    @Test(description = "TC12 - Search box receives keyboard focus", groups = {"search"})
    public void tc12_searchBoxReceivesFocus() {
        page.searchInput().click();
        Boolean focused = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "return document.activeElement === arguments[0];", page.searchInput());
        String caretColor = (String) ((JavascriptExecutor) driver).executeScript(
                "return getComputedStyle(arguments[0]).caretColor;", page.searchInput());
        Assert.assertTrue(Boolean.TRUE.equals(focused));
        Assert.assertNotEquals(caretColor, "rgba(0, 0, 0, 0)");
    }

    @Test(description = "TC13 - Search handles string, number, and special characters", groups = {"search"})
    public void tc13_searchHandlesDifferentInputTypes() {
        for (String term : Arrays.asList("tutorial", "2024", "@#$%&*")) {
            page.search(term);
            page.pressEnterInSearch();
            page.waitForGrid();
            Assert.assertFalse(page.hasLoadError(), "Search caused a load error for input: " + term);
            Assert.assertTrue(page.hasVideoCards() || page.hasNoResultsMessage(),
                    "Expected matches or a no-results message for: " + term);
        }
    }

    @Test(description = "TC14 - All Categories + Newest First", groups = {"filter-sort"})
    public void tc14_allCategoriesNewestFirst() {
        assertSearchFilterSort("All", "newest", Arrays.asList(
                "Dark forest alone in the dark?", "Solo traveller guide", "Skyscraper everyday fit",
                "Millionaire yatch vlog", "Beach day vlog", "Life of an farmer"));
    }

    @Test(description = "TC15 - All Categories + Oldest First", groups = {"filter-sort"})
    public void tc15_allCategoriesOldestFirst() {
        assertSearchFilterSort("All", "oldest", Arrays.asList(
                "Life of an farmer", "Beach day vlog", "Millionaire yatch vlog",
                "Skyscraper everyday fit", "Solo traveller guide", "Dark forest alone in the dark?"));
    }

    @Test(description = "TC16 - All Categories + Most Popular", groups = {"filter-sort"})
    public void tc16_allCategoriesMostPopular() {
        assertSearchFilterSort("All", "views-high", Arrays.asList(
                "Skyscraper everyday fit", "Beach day vlog", "Solo traveller guide",
                "Millionaire yatch vlog", "Dark forest alone in the dark?", "Life of an farmer"));
    }

    @Test(description = "TC17 - Tutorial + Newest First", groups = {"filter-sort"})
    public void tc17_tutorialNewestFirst() {
        assertSearchFilterSort("Tutorial", "newest", Arrays.asList("Beach day vlog", "Life of an farmer"));
    }

    @Test(description = "TC18 - Tutorial + Oldest First", groups = {"filter-sort"})
    public void tc18_tutorialOldestFirst() {
        assertSearchFilterSort("Tutorial", "oldest", Arrays.asList("Life of an farmer", "Beach day vlog"));
    }

    @Test(description = "TC19 - Tutorial + Most Popular", groups = {"filter-sort"})
    public void tc19_tutorialMostPopular() {
        assertSearchFilterSort("Tutorial", "views-high", Arrays.asList("Beach day vlog", "Life of an farmer"));
    }

    @Test(description = "TC20 - Vlogs + Newest First", groups = {"filter-sort"})
    public void tc20_vlogsNewestFirst() {
        assertSearchFilterSort("Vlog", "newest", Arrays.asList("Solo traveller guide", "Millionaire yatch vlog"));
    }

    @Test(description = "TC21 - Vlogs + Oldest First", groups = {"filter-sort"})
    public void tc21_vlogsOldestFirst() {
        assertSearchFilterSort("Vlog", "oldest", Arrays.asList("Millionaire yatch vlog", "Solo traveller guide"));
    }

    @Test(description = "TC22 - Vlogs + Most Popular", groups = {"filter-sort"})
    public void tc22_vlogsMostPopular() {
        assertSearchFilterSort("Vlog", "views-high", Arrays.asList("Solo traveller guide", "Millionaire yatch vlog"));
    }

    @Test(description = "TC23 - Reviews + Newest First", groups = {"filter-sort"})
    public void tc23_reviewsNewestFirst() {
        assertSearchFilterSort("Review", "newest", Arrays.asList(
                "Dark forest alone in the dark?", "Skyscraper everyday fit"));
    }

    @Test(description = "TC24 - Reviews + Oldest First", groups = {"filter-sort"})
    public void tc24_reviewsOldestFirst() {
        assertSearchFilterSort("Review", "oldest", Arrays.asList(
                "Skyscraper everyday fit", "Dark forest alone in the dark?"));
    }

    @Test(description = "TC25 - Reviews + Most Popular", groups = {"filter-sort"})
    public void tc25_reviewsMostPopular() {
        assertSearchFilterSort("Review", "views-high", Arrays.asList(
                "Skyscraper everyday fit", "Dark forest alone in the dark?"));
    }

    @Test(description = "TC26 - Video thumbnails render", groups = {"video-cards"})
    public void tc26_videoThumbnailsAreRendered() {
        List<WebElement> thumbnails = page.videoThumbnails();
        Assert.assertEquals(thumbnails.size(), 6,
                "The current repository dataset contains six video thumbnails.");
        wait.until(d -> page.videoThumbnails().stream().allMatch(page::isImageLoaded));
        for (WebElement thumbnail : page.videoThumbnails()) {
            Assert.assertTrue(thumbnail.isDisplayed());
            Assert.assertTrue(page.imageNaturalWidth(thumbnail) > 0, "A video thumbnail is broken.");
        }
    }

    @Test(description = "TC27 - Video titles are displayed correctly", groups = {"video-cards"})
    public void tc27_videoTitlesAreDisplayed() {
        List<String> actualTitles = page.videoTitles();
        Assert.assertEquals(actualTitles.size(), EXPECTED_VIDEO_TITLES.size());
        Assert.assertEquals(actualTitles.stream().filter(String::isBlank).count(), 0L);
        Assert.assertEquals(Set.copyOf(actualTitles), EXPECTED_VIDEO_TITLES);
    }

    @Test(description = "TC28 - View counts are displayed for all videos", groups = {"video-cards"})
    public void tc28_videoViewCountsAreDisplayed() {
        Map<String, String> expectedViews = Map.of(
                "Beach day vlog", "154.0K views",
                "Millionaire yatch vlog", "89.0K views",
                "Skyscraper everyday fit", "320.0K views",
                "Life of an farmer", "45.0K views",
                "Solo traveller guide", "112.0K views",
                "Dark forest alone in the dark?", "67.0K views"
        );
        Map<String, String> actualViews = page.videoMetadataByTitle().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().split("\\s+•\\s+")[0]));
        Assert.assertEquals(actualViews.keySet(), EXPECTED_VIDEO_TITLES);
        for (Map.Entry<String, String> expected : expectedViews.entrySet()) {
            Assert.assertEquals(actualViews.get(expected.getKey()), expected.getValue(),
                    "Incorrect view count for " + expected.getKey());
        }
        actualViews.values().forEach(value -> Assert.assertTrue(
                value.matches("\\d+(\\.\\d+)?[KM]? views"), "Invalid view count: " + value));
    }

    @Test(description = "TC29 - Time since upload is displayed", groups = {"video-cards"})
    public void tc29_timeSinceUploadIsDisplayed() {
        Map<String, String> metadata = page.videoMetadataByTitle();
        Assert.assertEquals(metadata.keySet(), EXPECTED_VIDEO_TITLES);
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            String[] parts = entry.getValue().split("\\s+•\\s+");
            Assert.assertEquals(parts.length, 2, "Expected view count and upload age for " + entry.getKey());
            Assert.assertTrue(parts[1].matches("Today|\\d+ days ago|\\d+ months ago|\\d+ years ago"),
                    "Unexpected upload age for " + entry.getKey() + ": " + parts[1]);
        }
        String expectedBeachAge = expectedTimeAgo(LocalDate.of(2023, 10, 1));
        String beachMetadata = metadata.get("Beach day vlog");
        Assert.assertTrue(beachMetadata.endsWith(" • " + expectedBeachAge),
                "Expected age " + expectedBeachAge + "; actual: " + beachMetadata);
    }

    private void assertSearchFilterSort(String category, String sort, List<String> expectedTitles) {
        // The app searches title text. "a" occurs in at least one title in each category.
        page.search("a");
        page.selectCategory(category);
        page.selectSort(sort);
        page.waitForGrid();
        Assert.assertEquals(page.videoTitles(), expectedTitles,
                "Incorrect result order for category=" + category + ", sort=" + sort);
        if (!"All".equals(category)) {
            Assert.assertTrue(page.videoCategories().stream().allMatch(category::equals),
                    "Every result should belong to " + category);
        }
    }

    private String expectedTimeAgo(LocalDate uploadDate) {
        long seconds = Duration.between(uploadDate.atStartOfDay(ZoneOffset.UTC).toInstant(), Instant.now()).getSeconds();
        double interval = seconds / 31_536_000.0;
        if (interval > 1) return (long) Math.floor(interval) + " years ago";
        interval = seconds / 2_592_000.0;
        if (interval > 1) return (long) Math.floor(interval) + " months ago";
        interval = seconds / 86_400.0;
        if (interval > 1) return (long) Math.floor(interval) + " days ago";
        return "Today";
    }
}
