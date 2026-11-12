package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Page object for the current MyTube channel grid page. */
public class ChannelPage {
    private static final By VIDEO_CARDS = By.cssSelector("#video-grid .card.video-card");
    private static final By VIDEO_TITLES = By.cssSelector("#video-grid .video-title");
    private static final By VIDEO_CATEGORIES = By.cssSelector("#video-grid .tag");
    private static final By THUMBNAILS = By.cssSelector("#video-grid .card-image img");
    private static final By NO_RESULTS = By.xpath("//*[@id='video-grid']//p[normalize-space()='No videos found.']");
    private static final By LOAD_ERROR = By.xpath("//*[@id='video-grid']//*[contains(normalize-space(.), 'Unable to load videos.')]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ChannelPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void open(String baseUrl) {
        driver.get(baseUrl);
        wait.until(d -> "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
        waitForGrid();
    }

    public void waitForGrid() {
        wait.until(d -> !d.findElements(VIDEO_CARDS).isEmpty()
                || !d.findElements(NO_RESULTS).isEmpty()
                || !d.findElements(LOAD_ERROR).isEmpty());
    }

    public String navigationType() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "const e = performance.getEntriesByType('navigation'); return e.length ? e[0].type : '';"
        );
    }

    public WebElement pageIcon() {
        return driver.findElement(By.id("yt-logo"));
    }

    public void clickPageIcon() {
        pageIcon().click();
    }

    public String pageIconBackground() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return getComputedStyle(arguments[0]).backgroundColor;", pageIcon());
    }

    public String pageIconCursor() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return getComputedStyle(arguments[0]).cursor;", pageIcon());
    }

    public boolean isPageIconHovered() {
        return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(
                "return arguments[0].matches(':hover');", pageIcon()));
    }

    @SuppressWarnings("unchecked")
    public boolean isFontAwesomeIconRendered(String selector) {
        WebElement icon = driver.findElement(By.cssSelector(selector));
        if (!icon.isDisplayed()) return false;
        Map<String, Object> style = (Map<String, Object>) ((JavascriptExecutor) driver).executeScript(
                "const s = getComputedStyle(arguments[0], '::before'); " +
                        "return {content:s.content, fontFamily:s.fontFamily, fontSize:s.fontSize};", icon);
        String content = String.valueOf(style.get("content"));
        String fontFamily = String.valueOf(style.get("fontFamily"));
        return !"none".equals(content) && !"normal".equals(content)
                && fontFamily.toLowerCase().contains("font awesome");
    }

    public void clickThemeToggle() {
        driver.findElement(By.cssSelector("label.theme-toggle[for='dark-mode-toggle']")).click();
    }

    public boolean isThemeToggleSelected() {
        return driver.findElement(By.id("dark-mode-toggle")).isSelected();
    }

    public String currentTheme() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return document.documentElement.dataset.theme;");
    }

    public WebElement channelAvatar() {
        return driver.findElement(By.cssSelector("img[alt='Channel Avatar']"));
    }

    public boolean isImageLoaded(WebElement image) {
        return Boolean.TRUE.equals(((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && arguments[0].naturalWidth > 0;", image));
    }

    public long imageNaturalWidth(WebElement image) {
        Number width = (Number) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].naturalWidth;", image);
        return width.longValue();
    }

    public String channelName() {
        return driver.findElement(By.cssSelector(".hero h1.title")).getText().trim();
    }

    public String channelStats() {
        return driver.findElement(By.cssSelector(".hero p.has-text-grey")).getText().trim();
    }

    public String subscribeButtonText() {
        return driver.findElement(By.id("subscribe-btn")).getText().trim();
    }

    public void clickSubscribe() {
        driver.findElement(By.id("subscribe-btn")).click();
    }

    public WebElement searchInput() {
        return driver.findElement(By.id("search-input"));
    }

    public void search(String term) {
        searchInput().clear();
        searchInput().sendKeys(term);
        waitForGrid();
    }

    public void pressEnterInSearch() {
        searchInput().sendKeys(Keys.ENTER);
    }

    public void selectCategory(String value) {
        new Select(driver.findElement(By.id("filter-select"))).selectByValue(value);
    }

    public void selectSort(String value) {
        new Select(driver.findElement(By.id("sort-select"))).selectByValue(value);
    }

    public List<WebElement> videoThumbnails() {
        return driver.findElements(THUMBNAILS);
    }

    public List<String> videoTitles() {
        return driver.findElements(VIDEO_TITLES).stream()
                .map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    public List<String> videoCategories() {
        return driver.findElements(VIDEO_CATEGORIES).stream()
                .map(e -> e.getText().trim()).collect(Collectors.toList());
    }

    public List<WebElement> videoCards() {
        return driver.findElements(VIDEO_CARDS);
    }

    public boolean hasVideoCards() {
        return !videoCards().isEmpty();
    }

    public boolean hasNoResultsMessage() {
        return !driver.findElements(NO_RESULTS).isEmpty();
    }

    public boolean hasLoadError() {
        return !driver.findElements(LOAD_ERROR).isEmpty();
    }

    public Map<String, String> videoMetadataByTitle() {
        Map<String, String> metadata = new LinkedHashMap<>();
        for (WebElement card : videoCards()) {
            String title = card.findElement(By.cssSelector(".video-title")).getText().trim();
            String meta = card.findElement(By.cssSelector(".card-content > p.is-size-7.has-text-grey.mb-2"))
                    .getText().trim();
            metadata.put(title, meta);
        }
        return metadata;
    }
}
