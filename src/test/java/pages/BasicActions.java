package pages;

import base.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static gluecode.BaseTest.PAGE;

public abstract class BasicActions {

    public static final int DEFAULT_WAIT = 30;
    public static final int IMPLICIT_DEFAULT_WAIT = 10;
    WebDriver driver = DriverFactory.driverPool.get();
    public WebDriverWait wait;
    SecureRandom random = new SecureRandom();

    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    public BasicActions() {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, DEFAULT_WAIT), this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }

    public void goToUrl(String name) {
        driver.navigate().to(PAGE + name);
    }

    public void assertVisible(By by, String message, int... timeout) {
        int timeoutFinal = timeout.length == 0 ? DEFAULT_WAIT : timeout[0];
        Assert.assertTrue(isElementVisible(by, timeoutFinal), message);
    }

    protected boolean isElementVisible(By by, int timeOutInSeconds) {
        boolean isVisible;
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOutInSeconds));
            FluentWait<WebDriver> waitSeconds = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
                    .ignoring(NoSuchElementException.class)
                    .pollingEvery(Duration.ofMillis(300));

            isVisible = waitSeconds.until(ExpectedConditions.visibilityOfElementLocated(by)).isDisplayed();
        } catch (Exception e) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_DEFAULT_WAIT));
            return false;
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_DEFAULT_WAIT));
        return isVisible;
    }

    public void clickElement(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            driver.findElement(by).click();
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            driver.findElement(by).click();
        }
    }

    public static Logger logger() {
        String declaringClass = Thread.currentThread().getStackTrace()[2].getClassName();
        return LoggerFactory.getLogger(declaringClass);
    }

    public void clickJs(WebElement element) {
        try {
            getJSExecutor().executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Assert.fail("Elemente tıklanılamadı Hata " + e);
        }
    }

    public List<WebElement> findElements(By by) {
        List<WebElement> elementList = new ArrayList<>();
        try {
            elementList = driver.findElements(by);
        } catch (Exception e) {
            logger().error(by + " Element Listesi Bulunamadı" + e.getMessage());
        }
        return elementList;
    }

    public int randomNumber(int start, int end) {
        int randomNum = 0;
        if (start > end) {
            Assert.fail();
        } else {
            randomNum = random.nextInt((end - start) + 1) + start;
        }
        return randomNum;
    }

    public WebElement findElement(By by, int timeout) {
        FluentWait<WebDriver> webDriverFluentWait = waitDynamic(timeout)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return webDriverFluentWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebDriverWait waitDynamic(int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public void scrollToElement(WebElement element) {
        try {
            if (element != null) {
                scrollTo(element.getLocation().getX(), element.getLocation().getY());
            }
        } catch (Exception e) {
            String declaringClass = Thread.currentThread().getStackTrace()[2].getClassName();
            Assert.fail("");
        }
    }

    protected void scrollTo(int x, int y) {
        String jsScroll = String.format("window.scrollTo(%d, %d)", x, y);
        executeJs(jsScroll, true);
    }

    protected Object executeJs(String js, boolean wait) {
        return wait ? getJSExecutor().executeScript(js, "") : getJSExecutor().executeAsyncScript(js, "");
    }

    public void sendKeyElement(By by, String value, boolean pressEnter) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            driver.findElement(by).sendKeys(value);
            if (pressEnter) {
                driver.findElement(by).sendKeys(Keys.ENTER);
            }
        } catch (Exception e) {
            driver.findElement(by).sendKeys(value);
            if (pressEnter) {
                driver.findElement(by).sendKeys(Keys.ENTER);
            }
        }
    }

    public void scrollToPageEnd() {
        try {
            executeJs("window.scrollTo(0, document.body.scrollHeight)", true);
            timeUnitMilliSeconds(5);
        } catch (TimeoutException e) {
            Assert.fail("Page Scrolling Failed " + e.getMessage());
        }
    }

    protected void timeUnitMilliSeconds(int milliSeconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        }catch (InterruptedException e){
            logger().error("Time Unit MilliSeconds Error %s ".formatted(e.getMessage()));
            Thread.currentThread().interrupt();
        }
    }

    public <T> void assertEquals(T actual, T expected, String message){
        Assert.assertEquals(actual, expected, message);
    }

    public void closePopup(By selector, int timeout){
       if (isElementVisible(selector, timeout)){
           clickElement(selector);
       }
    }

    public String getText(By locator){
        return findElement(locator, 5).getText();
    }

    protected String getAttribute(By locator, String attribute){
        String attr = findElement(locator, 5).getAttribute(attribute);
        return attr == null ? "" : attr;
    }
}
