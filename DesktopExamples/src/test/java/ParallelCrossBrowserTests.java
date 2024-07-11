import org.testng.ITestResult;
import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 mvn test -pl DesktopExamples -Dtest=ParallelCrossBrowserTests
 */

public class ParallelCrossBrowserTests {
    private static final String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss").format(LocalDateTime.now());
    // DataProvider method to provide different browser configurations for parallel test execution
    @DataProvider(name = "browsers", parallel = true)
    public static Object[][] getBrowserConfigurations() {
        return new Object[][] {
                {"firefox", "latest", "Windows 10"},
                {"microsoftedge", "latest", "Windows 10"},
                {"chrome", "latest", "macOS 13"},
                {"firefox", "latest", "macOS 13"},
                {"safari", "latest", "macOS 13"},
                {"microsoftedge", "latest", "Windows 11"},
                {"chrome", "latest", "macOS 12"},
                {"firefox", "latest", "macOS 12"},
                {"safari", "latest", "macOS 12"},
                {"microsoftedge", "latest", "Windows 11"},
        };
    }

    // Test method for parallel cross-browser testing
    @Test(dataProvider = "browsers")
    public void loginTest(String browser, String browserVersion, String platformName) throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("browserVersion", browserVersion);
        capabilities.setCapability("browserName", browser);

        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("name", "Parallel Cross Browser - Swag Labs User Login");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss").format(LocalDateTime.now());
        sauceOptions.put("build", "Parallelization-Example-Build-"+timestamp);
        capabilities.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        RemoteWebDriver driver = new RemoteWebDriver(url, capabilities);

        driver.navigate().to("https://www.saucedemo.com");

        By usernameFieldLocator = By.cssSelector("#user-name");
        By passwordFieldLocator = By.cssSelector("#password");
        By submitButtonLocator = By.cssSelector(".btn_action");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameFieldLocator));

        WebElement userNameField = driver.findElement(usernameFieldLocator);
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        WebElement submitButton = driver.findElement(submitButtonLocator);

        JavascriptExecutor js = driver;

        js.executeScript("sauce:context=Entering Username: standard_user");
        userNameField.sendKeys("standard_user");

        js.executeScript("sauce:context=Entering Password: **********");
        passwordField.sendKeys("secret_sauce");

        js.executeScript("sauce:context=Clicking login button");
        submitButton.click();

        js.executeScript("sauce:context=Verifying Catalog Page is Displayed");

        boolean assertionPassed = driver.getCurrentUrl().equalsIgnoreCase("https://www.saucedemo.com/inventory.html");
        try {
            Assert.assertTrue(assertionPassed);
        } finally {
            driver.executeScript("sauce:job-result=" + (assertionPassed ? "passed" : "failed"));
            driver.quit();
        }
    }

}
