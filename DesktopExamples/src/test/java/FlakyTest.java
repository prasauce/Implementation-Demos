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
public class FlakyTest {
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
        sauceOptions.put("name", "Flaky Test Example");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("build", "Implementation-Demo-Java-TestNG");

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
        //This example demonstrates when elements on a page change by disappearing/reappearing on each page load, leading to inconsistent test results
        driver.navigate().to("https://the-internet.herokuapp.com/disappearing_elements");

        // Locating web element for Gallery
        By galleryButtonLocator = By.xpath("//*[@id=\"content\"]/div/ul/li[5]/a");

        // Creating WebElements for the username, password, and submit button elements
        WebElement galleryButton = driver.findElement(galleryButtonLocator);

        // Using JavascriptExecutor to add context information to the Sauce Labs test
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("sauce:context=Clicking on Gallery Button");

        galleryButton.click();

        // Asserting that the current URL matches the expected URL
        Assert.assertEquals("https://the-internet.herokuapp.com/gallery/", driver.getCurrentUrl());
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
