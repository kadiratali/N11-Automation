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

    /**
     * WebDriver'ı bir JavascriptExecutor nesnesine dönüştürür ve döndürür.
     * Bu, JavaScript kodlarını çalıştırmak için kullanılır.
     *
     * @return JavascriptExecutor nesnesi.
     */
    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }

    /**
     * Sürücü nesnesini ve varsayılan bekleme süresini (DEFAULT_WAIT) kullanarak
     * sayfa nesnelerini başlatır.
     * Elementlerin yüklenmesini beklemek için WebDriverWait nesnesini başlatır.
     */
    public BasicActions() {
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, DEFAULT_WAIT), this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
    }

    /**
     * Tarayıcıyı belirtilen URL'ye yönlendirir.
     *
     * @param name Yönlendirilecek sayfanın URL adı.
     */
    public void goToUrl(String name) {
        driver.navigate().to(PAGE + name);
    }

    /**
     * Belirtilen elementin görünür olduğunu doğrular ve eğer görünür değilse
     * hata mesajı fırlatır.
     *
     * @param by      Elementin konumu (By sınıfı).
     * @param message Elementin görünür olmaması durumunda fırlatılacak hata mesajı.
     * @param timeout İsteğe bağlı bekleme süresi (saniye cinsinden). Varsayılan DEFAULT_WAIT'tir.
     */
    public void assertVisible(By by, String message, int... timeout) {
        int timeoutFinal = timeout.length == 0 ? DEFAULT_WAIT : timeout[0];
        Assert.assertTrue(isElementVisible(by, timeoutFinal), message);
    }

    /**
     * Belirtilen elementin belirtilen süre içinde görünür olup olmadığını kontrol eder.
     *
     * @param by                Elementin konumu (By sınıfı).
     * @param timeOutInSeconds  Maksimum bekleme süresi (saniye cinsinden).
     * @return Element görünürse true, aksi halde false.
     */
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

    /**
     * Belirtilen elemente tıklar. Eğer tıklama hatası oluşursa (StaleElementReferenceException veya
     * ElementClickInterceptedException), tekrar deneme yapar.
     *
     * @param by Elementin konumu (By sınıfı).
     */
    public void clickElement(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            driver.findElement(by).click();
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            driver.findElement(by).click();
        }
    }

    /**
     * Güncel sınıfın adını kullanarak bir Logger nesnesi oluşturur.
     * Loglama işlemleri için kullanılır.
     *
     * @return Logger nesnesi.
     */
    public static Logger logger() {
        String declaringClass = Thread.currentThread().getStackTrace()[2].getClassName();
        return LoggerFactory.getLogger(declaringClass);
    }

    /**
     * JavaScript kullanarak belirtilen elemente tıklar.
     *
     * @param element Tıklanacak WebElement nesnesi.
     */
    public void clickJs(WebElement element) {
        try {
            getJSExecutor().executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            Assert.fail("Elemente tıklanılamadı Hata " + e);
        }
    }

    /**
     * Belirtilen konuma uyan tüm elementleri bulur ve bir liste olarak döndürür.
     *
     * @param by Elementlerin konumu (By sınıfı).
     * @return Bulunan WebElement'lerin listesi. Eğer element bulunamazsa boş liste döner.
     */
    public List<WebElement> findElements(By by) {
        List<WebElement> elementList = new ArrayList<>();
        try {
            elementList = driver.findElements(by);
        } catch (Exception e) {
            logger().error(by + " Element Listesi Bulunamadı" + e.getMessage());
        }
        return elementList;
    }

    /**
     * Belirtilen aralıkta (başlangıç ve bitiş dahil) rastgele bir tam sayı üretir.
     *
     * @param start Rastgele sayının başlangıç değeri.
     * @param end   Rastgele sayının bitiş değeri.
     * @return Üretilen rastgele sayı.
     */
    public int randomNumber(int start, int end) {
        int randomNum = 0;
        if (start > end) {
            Assert.fail();
        } else {
            randomNum = random.nextInt((end - start) + 1) + start;
        }
        return randomNum;
    }

    /**
     * Belirtilen süre içinde ve dinamik bekleme ayarlarıyla bir element bulur.
     * Eğer element görünür değilse NoSuchElementException veya StaleElementReferenceException hatalarını göz ardı eder.
     *
     * @param by      Elementin konumu (By sınıfı).
     * @param timeout Maksimum bekleme süresi (saniye cinsinden).
     * @return Bulunan WebElement nesnesi.
     */
    public WebElement findElement(By by, int timeout) {
        FluentWait<WebDriver> webDriverFluentWait = waitDynamic(timeout)
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return webDriverFluentWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Belirtilen saniye cinsinden bekleme süresiyle bir WebDriverWait nesnesi oluşturur.
     *
     * @param seconds Bekleme süresi (saniye cinsinden).
     * @return WebDriverWait nesnesi.
     */
    public WebDriverWait waitDynamic(int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    /**
     * Belirtilen elemente kadar sayfayı kaydırır.
     *
     * @param element Sayfanın kaydırılacağı WebElement nesnesi.
     */
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

    /**
     * Belirtilen X ve Y koordinatlarına kadar sayfayı kaydırmak için JavaScript çalıştırır.
     *
     * @param x X koordinatı.
     * @param y Y koordinatı.
     */
    protected void scrollTo(int x, int y) {
        String jsScroll = String.format("window.scrollTo(%d, %d)", x, y);
        executeJs(jsScroll, true);
    }

    /**
     * Belirtilen JavaScript kodunu çalıştırır.
     *
     * @param js   Çalıştırılacak JavaScript kodu.
     * @param wait Kodun tamamlanmasını bekleyip beklemediğini belirten boolean değer.
     * @return JavaScript kodunun döndürdüğü nesne.
     */
    protected Object executeJs(String js, boolean wait) {
        return wait ? getJSExecutor().executeScript(js, "") : getJSExecutor().executeAsyncScript(js, "");
    }

    /**
     * Belirtilen elemente bir metin gönderir. İsteğe bağlı olarak ENTER tuşuna basar.
     *
     * @param by        Metnin gönderileceği elementin konumu (By sınıfı).
     * @param value     Gönderilecek metin.
     * @param pressEnter Metin gönderildikten sonra ENTER'a basılacaksa true, aksi halde false.
     */
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

    /**
     * Sayfanın sonuna kadar kaydırır.
     */
    public void scrollToPageEnd() {
        try {
            executeJs("window.scrollTo(0, document.body.scrollHeight)", true);
            timeUnitMilliSeconds(5);
        } catch (TimeoutException e) {
            Assert.fail("Page Scrolling Failed " + e.getMessage());
        }
    }

    /**
     * Belirtilen milisaniye cinsinden bekleme süresi sağlar.
     *
     * @param milliSeconds Bekleme süresi (milisaniye cinsinden).
     */
    protected void timeUnitMilliSeconds(int milliSeconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        }catch (InterruptedException e){
            logger().error("Time Unit MilliSeconds Error %s ".formatted(e.getMessage()));
            Thread.currentThread().interrupt();
        }
    }

    /**
     * İki değerin eşit olduğunu doğrular.
     *
     * @param actual  Gerçekleşen değer.
     * @param expected Beklenen değer.
     * @param message Eşit olmaması durumunda fırlatılacak hata mesajı.
     * @param <T>     Karşılaştırılan değerlerin tipi.
     */
    public <T> void assertEquals(T actual, T expected, String message){
        Assert.assertEquals(actual, expected, message);
    }

    /**
     * Belirtilen elementin görünür olup olmadığını kontrol eder ve eğer görünürse tıklar.
     * Pop-up'ları veya geçici bildirimleri kapatmak için kullanılır.
     *
     * @param selector Elementin konumu (By sınıfı).
     * @param timeout  Bekleme süresi (saniye cinsinden).
     */
    public void closePopup(By selector, int timeout){
        if (isElementVisible(selector, timeout)){
            clickElement(selector);
        }
    }

    /**
     * Belirtilen elementin görünen metnini alır.
     *
     * @param locator Elementin konumu (By sınıfı).
     * @return Elementin metni.
     */
    public String getText(By locator){
        return findElement(locator, 5).getText();
    }

    /**
     * Belirtilen elementin bir niteliğinin (attribute) değerini alır.
     * Eğer nitelik yoksa boş bir dize döndürür.
     *
     * @param locator   Elementin konumu (By sınıfı).
     * @param attribute Alınacak niteliğin adı.
     * @return Nitelik değeri veya boş dize.
     */
    protected String getAttribute(By locator, String attribute){
        String attr = findElement(locator, 5).getAttribute(attribute);
        return attr == null ? "" : attr;
    }
}