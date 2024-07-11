import com.saucelabs.visual.CheckOptions;
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
import java.util.List;
import java.util.Map;
import com.saucelabs.visual.VisualApi;
import com.saucelabs.visual.DataCenter;


/**
 * mvn test -pl DesktopExamples -Dtest=VisualDesktopTest
 */
public class VisualDesktopTest {
    private static RemoteWebDriver driver;
    private static VisualApi visual;
    private static final String username = System.getenv("SAUCE_USERNAME");
    private static final String accessKey = System.getenv("SAUCE_ACCESS_KEY");
    private static final boolean modifyPage = Boolean.parseBoolean(System.getProperty("modifyPage", "false")); //After running initial baseline test, switch value to "true" in order to demonstrate UI changes

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("browserName", "Chrome");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("name", "Desktop - Swag Labs User Login - Visual Test");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("build", "Visual-Demo-Java-TestNG");
        sauceOptions.put("buildName", "Swag Labs");

        capabilities.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(url, capabilities);
        visual = new VisualApi.Builder(driver, username, accessKey, DataCenter.US_WEST_1)
                .withBuild("Sauce Visual Test")
                .withBranch("main")
                .withProject("Implementation Examples")
                .withCaptureDom(true)
                .build();
    }


    @Test
    public void userLogin() throws InterruptedException {
        driver.navigate().to("https://www.saucedemo.com");

        By usernameFieldLocator = By.cssSelector("#user-name");
        By passwordFieldLocator = By.cssSelector("#password");
        By submitButtonLocator = By.cssSelector(".btn_action");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameFieldLocator));

        WebElement userNameField = driver.findElement(usernameFieldLocator);
        WebElement passwordField = driver.findElement(passwordFieldLocator);
        WebElement submitButton = driver.findElement(submitButtonLocator);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        //Take a visual snapshot and name it "Login Page"
        visual.sauceVisualCheck("Login Page");

        js.executeScript("sauce:context=Entering Username: standard_user");
        userNameField.sendKeys(modifyPage ? "visual_user" : "standard_user");

        js.executeScript("sauce:context=Entering Password: **********");
        passwordField.sendKeys("secret_sauce");

        js.executeScript("sauce:context=Clicking login button");
        submitButton.click();

        js.executeScript("sauce:context=Verifying Catalog Page is Displayed");

        visual.sauceVisualCheck("Catalog");
        Assert.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
        driver.quit();
    }
}
