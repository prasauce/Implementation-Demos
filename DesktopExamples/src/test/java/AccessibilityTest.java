import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 mvn test -pl DesktopExamples -Dtest=AccessibilityTest
 */

public class AccessibilityTest {
    protected RemoteWebDriver driver;

    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("browserName", "Chrome");
        Map<String, Object> sauceOptions = new HashMap<>();
        sauceOptions.put("name", "Accessibility Test - Swag Labs User Login");
        sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.put("build", "Performance-Test-Java-TestNG");
        sauceOptions.put("extendedDebugging", true); //Enables Network Capture
        sauceOptions.put("capturePerformance", true); //Enables Performance Capture feature
        capabilities.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(url, capabilities);
    }

    @Test
    public void accessibilityTest() {
        driver.navigate().to("https://www.saucedemo.com");
        AxeBuilder axeBuilder = new AxeBuilder();
        Results accessibilityResults = axeBuilder.analyze(driver);
        System.out.println("Accessibility results violation size: " + accessibilityResults.getViolations().size());
        Assert.assertEquals(3, accessibilityResults.getViolations().size());
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
        driver.quit();
    }
}
