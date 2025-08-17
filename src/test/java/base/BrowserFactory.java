package base;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

public enum BrowserFactory {

    CHROME {

        @Override
        public WebDriver createDriver() {return new ChromeDriver(createLocalOptions());}

        @Override
        public ChromeOptions createLocalOptions(){
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.setAcceptInsecureCerts(true);
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--allow-running-insecure-content");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("excludeSwitches", "enable-automation");
            chromeOptions.addArguments("--disable-web-security");
            return chromeOptions;
        }
    };


    public abstract WebDriver createDriver();

    public abstract AbstractDriverOptions<?> createLocalOptions();

}
