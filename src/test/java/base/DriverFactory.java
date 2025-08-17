package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static base.Browser.CHROME;

public class DriverFactory {

    public static final InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();
    protected static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);

    public static synchronized WebDriver createInstance(){
        BrowserFactory browserFactory = BrowserFactory.valueOf("CHROME");

        if (driverPool.get() == null){
            switch (browserFactory){
                case CHROME:
                    driverPool.set(BrowserFactory.CHROME.createDriver());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser type: " + browserFactory);
            }

        }
        return driverPool.get();
    }

    public static void closeDriver(){
        if (driverPool.get() != null){
            LOGGER.info("Closing WebDriver session...");
            driverPool.get().manage().deleteAllCookies();
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
