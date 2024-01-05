package RealDevices;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 mvn test -pl MobileExamples -Dtest=ParallelRealDeviceTests
 */

public class ParallelRealDeviceTests {
    @DataProvider(name = "devices", parallel = true)
    public static Object[][] getBrowserConfigurations() {
        return new Object[][] {
                {"Android", "Google Pixel.*", "^1[0-3].*"},
                {"Android", "Samsung Galaxy.*", "^1[0-3].*"},
                {"Android", "OnePlus.*", "^1[0-3].*"},
                {"Android", "Xiaomi.*", "^1[0-3].*"}
        };
    }

    @Test(dataProvider = "devices")
    public void loginTest(String platformName, String deviceName, String platformVersion) throws MalformedURLException {
        // Set the desired capabilities for Android
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", platformName);
        caps.setCapability("appium:app", "storage:filename=mda-2.0.0-21.apk");  // The filename of the mobile app
        caps.setCapability("appium:deviceName", deviceName);
        caps.setCapability("appium:platformVersion", platformVersion);
        caps.setCapability("appium:deviceOrientation", "portrait");
        caps.setCapability("appium:automationName", "UiAutomator2");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("build", "Implementation-Demo");
        sauceOptions.setCapability("name", "Parallel Real Devices - Select Item From Catalog");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        AndroidDriver driver = new AndroidDriver(url, caps);

        JavascriptExecutor js = driver;

        By backpack = AppiumBy.accessibilityId("Sauce Labs Backpack");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(backpack)).click();
        js.executeScript("sauce:context=Clicking on Backpack");

        By productItem = AppiumBy.id("com.saucelabs.mydemoapp.android:id/productTV");
        String productItemName = driver.findElement(productItem).getText();

        boolean assertionPassed = productItemName.equals("Sauce Labs Backpack");

        try {
            Assert.assertTrue(assertionPassed);
        } finally {
            driver.executeScript("sauce:job-result=" + (assertionPassed ? "passed" : "failed"));
            driver.quit();
        }
    }
}
