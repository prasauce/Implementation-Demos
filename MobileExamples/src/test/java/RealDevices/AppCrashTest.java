package RealDevices;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
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
import java.util.Arrays;

/**
 mvn test -pl MobileExamples -Dtest=AppCrashTest
 */

public class AppCrashTest {
    private AndroidDriver driver;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        // Set the desired capabilities for Android
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:app", "storage:filename=mda-2.0.1-22.apk");  // The filename of the mobile app
        caps.setCapability("appium:deviceName", "Google.*");
        caps.setCapability("appium:platformVersion", "13");
        caps.setCapability("appium:deviceOrientation", "portrait");
        caps.setCapability("appium:automationName", "UiAutomator2");
        MutableCapabilities sauceOptions = new MutableCapabilities();
        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceOptions.setCapability("build", "Implementation-Demo");
        sauceOptions.setCapability("name", "Android Real Device - App Crash Example");
        caps.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
        driver = new AndroidDriver(url, caps);
    }

    @Test
    public void addToCart() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        By backpack = AppiumBy.accessibilityId("Sauce Labs Backpack");
        js.executeScript("sauce:context=Clicking on Backpack");
        waitAndClick(backpack);

        js.executeScript("sauce:context=Scrolling down");
        scrollDown();

        By addToCartButton = AppiumBy.accessibilityId("Tap to add product to cart");
        js.executeScript("sauce:context=Clicking on Add To Cart button");
        waitAndClick(addToCartButton);

        driver.navigate().back();

        By brokenItem = AppiumBy.accessibilityId("Sauce Labs Bike Light");
        js.executeScript("sauce:context=Clicking on Broken Item");
        waitAndClick(brokenItem);

        js.executeScript("sauce:context=Clicking on Add To Cart button");
        waitAndClick(addToCartButton);

        By cartButton = AppiumBy.accessibilityId("Displays number of items in your cart");
        js.executeScript("sauce:context=Clicking on Cart");
        waitAndClick(cartButton);

        By checkoutButton = AppiumBy.accessibilityId("Proceed To Checkout button");
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));

        By totalNumber = AppiumBy.id("com.saucelabs.mydemoapp.android:id/itemsTV");
        String numberOfCartItems = driver.findElement(totalNumber).getText();

        boolean isItemInCart = numberOfCartItems.equals("2 Items");
        js.executeScript("sauce:context=Verifying Items are Present in Cart");

        Assert.assertTrue(isItemInCart);
    }


    private void scrollDown() {
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.height * 0.70);
        int endY = (int) (size.height * 0.30);
        int centerX = size.width / 2;
        //Toe of Pointer Input
        PointerInput finger = new PointerInput (PointerInput.Kind.TOUCH,"finger");
        //Creating Sequence object to add actions
        Sequence swipe = new Sequence(finger, 1);
        //Move finger into starting position
        swipe.addAction(finger.createPointerMove(Duration.ofSeconds(0),PointerInput.Origin.viewport(),centerX,(int)startY));
        //Finger comes down into contact with screen
        swipe.addAction(finger.createPointerDown(0));
        //Finger moves to end position
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(700),
                PointerInput.Origin.viewport(),centerX, (int)endY));
        //Get up Finger from Screen
        swipe.addAction(finger.createPointerUp(0));
        //Perform the actions
        driver.perform(Arrays.asList(swipe));

    }

    private void waitAndClick(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }


    @AfterMethod
    public void teardown(ITestResult result) {
        String status = result.isSuccess() ? "passed" : "failed";
        driver.executeScript("sauce:job-result=" + status);
        driver.quit();
    }
}
