package testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        tags = "${cucumber.filter.tags}",
        features = "${cucumber.features}",
        glue = "gluecode",
        plugin = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)

public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
