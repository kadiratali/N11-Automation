package gluecode;

import base.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class BaseTest {

    private final static int DEFAULT_WAIT = 10;
    public final static String PAGE = "https://www.n11.com/";

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);

    @Before
    public synchronized void setUp(Scenario scenario){
        DriverFactory.driverPool.set(DriverFactory.createInstance());
        DriverFactory.driverPool.get().manage().window().maximize();
        openPage(PAGE);
        scenario.log("Scenario name: " + scenario.getName());
    }

    @After
    public synchronized void tearDown(Scenario scenario){
        DriverFactory.closeDriver();
        scenario.log("Scenario name: " + scenario.getName());
    }

    private synchronized void openPage(String targetPage){
        DriverFactory.driverPool.get().manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
        DriverFactory.driverPool.get().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        DriverFactory.driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_WAIT));

        DriverFactory.driverPool.get().get(targetPage);
        WebDriverWait wait = new WebDriverWait(DriverFactory.driverPool.get(), Duration.ofSeconds(60));
        wait.until(ExpectedConditions.urlContains(targetPage));
    }

}
