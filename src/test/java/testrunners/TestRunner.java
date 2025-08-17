package testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber testlerini çalıştırmak için kullanılan TestNG test runner sınıfıdır.
 * Bu sınıf, özellik dosyalarını (`features`) ve adım tanımlama dosyalarını (`glue`)
 * belirterek Cucumber'ı yapılandırır.
 * <p>
 * {@link CucumberOptions} anotasyonu, Cucumber'ın nasıl çalışacağını tanımlar:
 * <ul>
 * <li>`tags`: Hangi senaryoların çalıştırılacağını belirler. Genellikle
 * komut satırı argümanları ile dinamik olarak ayarlanır.</li>
 * <li>`features`: Özellik dosyalarının yolunu belirtir.</li>
 * <li>`glue`: Adım tanımlama (step definition) dosyalarının bulunduğu paketi belirtir.</li>
 * <li>`plugin`: Test sonuçlarını Allure raporlama aracıyla entegre etmek için kullanılır.</li>
 * </ul>
 */
@CucumberOptions(
        tags = "${cucumber.filter.tags}",
        features = "${cucumber.features}",
        glue = "gluecode",
        plugin = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)

public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * TestNG'nin senaryoları paralel olarak çalıştırmasına olanak tanıyan veri sağlayıcı metodu.
     * Bu metot, {@link AbstractTestNGCucumberTests#scenarios()} metodunu geçersiz kılarak
     * testlerin aynı anda farklı thread'lerde çalışmasını sağlar.
     *
     * @return Cucumber senaryolarını içeren bir Object dizisi.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}