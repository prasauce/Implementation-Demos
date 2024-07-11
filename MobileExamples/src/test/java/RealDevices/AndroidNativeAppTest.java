package RealDevices;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 mvn test -pl MobileExamples -Dtest=AndroidNativeAppTest
 */

public class AndroidNativeAppTest {
    private AndroidDriver driver;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        // Set the desired capabilities for Android
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:app", "storage:filename=mda-2.0.1-22.apk");
        caps.setCapability("appium:deviceName", "Google.*");
        caps.setCapability("appium:platformVersion", "12");
        caps.setCapability("appium:deviceOrientation", "portrait");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:noReset",true); //NoReset

        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("build", "Implementation-Demo");
        sauceOptions.setCapability("name", "Android Real Device - Add Item To Cart");
        //sauceOptions.setCapability("tunnelName","<TUNNEL-NAME>");
        //sauceOptions.setCapability("tunnelOwner", "<TUNNEL-OWNER>");
        caps.setCapability("sauce:options", sauceOptions);
        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new AndroidDriver(url, caps);
    }

    @Test
    public void addToCart() {
        JavascriptExecutor js = driver;

        By backpack = AppiumBy.accessibilityId("Sauce Labs Backpack");
        js.executeScript("sauce:context=Clicking on Backpack");
        waitAndClick(backpack);

        js.executeScript("sauce:context=Scrolling down");
        driver.executeScript("mobile: shell", ImmutableMap.of(
                "command", "input",
                "args", ImmutableList.of("swipe", "500 1000 500 300 1000")
        ));

        By addToCartButton = AppiumBy.accessibilityId("Tap to add product to cart");
        js.executeScript("sauce:context=Clicking on Add To Cart button");
        waitAndClick(addToCartButton);

        By cartButton = AppiumBy.xpath("//android.widget.ImageView[@content-desc=\"Displays number of items in your cart\"]");
        js.executeScript("sauce:context=Clicking on Cart");
        waitAndClick(cartButton);

        By totalNumber = AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemsTV");
        String numberOfCartItems = driver.findElement(totalNumber).getText();

        boolean isItemInCart = numberOfCartItems.equals("1 Items");
        js.executeScript("sauce:context=Verifying Item is Present in Cart");

        Assert.assertTrue(isItemInCart);
    }

    private void waitAndClick(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }


    @AfterMethod
    public void teardown(ITestResult result) {
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
        String testResultURL = driver.getCapabilities().getCapability("testobject_test_report_url").toString();
        System.out.println("Sauce Labs Test URL: " + testResultURL);
        driver.quit();
    }
}
