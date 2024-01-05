import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 mvn test -pl DesktopExamples -Dtest=FailureAnalysisTest
 */
public class FailureAnalysisTest {
    protected RemoteWebDriver driver;

    // Setting up the test environment before each test method
    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        // Creating capabilities for the RemoteWebDriver
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("browserName", "Chrome");

        // Configuring Sauce Labs options
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("name", "Failure Analysis Test");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("build", "Implementation-Demo");

        // Adding Sauce Labs options to capabilities
        capabilities.setCapability("sauce:options", sauceOptions);

        // Setting up the URL for the RemoteWebDriver
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(url, capabilities);
    }

    // Test method for user login
    @Test
    public void userLogin() throws InterruptedException {
        // Navigating to the test application
        driver.navigate().to("https://www.saucedemo.com");

        // Locating web elements for username, password, and submit button
        By usernameFieldLocator = By.cssSelector("#user-name");
        By passwordFieldLocator = By.cssSelector("#password");
        By submitButtonLocator = By.cssSelector(".button_action"); //Incorrect locator which will cause the test to fail on purpose

        // Setting up a WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameFieldLocator));

        // Creating WebElements for the username, password, and submit button elements
        WebElement userNameField = driver.findElement(usernameFieldLocator);
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        WebElement submitButton = driver.findElement(submitButtonLocator);

        // Using JavascriptExecutor to add context information to the Sauce Labs test
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("sauce:context=Entering Username: standard_user");
        userNameField.sendKeys("standard_user");

        js.executeScript("sauce:context=Entering Password: **********");
        passwordField.sendKeys("secret_sauce");

        js.executeScript("sauce:context=Clicking login button");
        submitButton.click();

        js.executeScript("sauce:context=Verifying Catalog Page is Displayed");

        // Asserting that the current URL matches the expected URL
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
    }

    // Teardown method executed after each test method
    @AfterMethod
    public void teardown(ITestResult result) {
        // Retrieving the test result status and updating Sauce Labs job status
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
        driver.quit();
    }
}
