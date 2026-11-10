package automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class homepage {

	static WebDriver driver;
	public void before_class() {
		System.out.println("Starting Homepage Test..");
	}
	
	@BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
    }
	
	@Test
	public void ValidChannelName() {
		WebElement channelNameTxt = driver.findElement(By.xpath("//*[@class=\"title is-3 mb-1\"]"));
		
		String actualChannelName = channelNameTxt.getText();
		
		String expectedChannelName = "Developer Channel";
		
		Assert.assertEquals(actualChannelName, expectedChannelName,
			    "Channel name does not match!");
	}
	
	@Test
	public void VerifyPageReload() {
	    WebElement myTubeButton = driver.findElement(
	        By.xpath("//*[@class=\"title is-4 ml-2 mb-0\"]")
	    );

	    String initialUrl = driver.getCurrentUrl();

	    myTubeButton.click();

	    new WebDriverWait(driver, Duration.ofSeconds(5))
	    .until(d -> d.getCurrentUrl().equals(initialUrl));

	    Assert.assertEquals(driver.getCurrentUrl(), initialUrl);
	}
	
    @AfterMethod(alwaysRun = true)
    public void exit() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void after_class() {
        System.out.println("Homepage Test Completed.");
    
    }
}
