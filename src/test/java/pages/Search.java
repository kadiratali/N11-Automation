package pages;

import base.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static constants.HomePageConstants.*;

public class Search extends BasicActions {
    String marketName;
    List<String> marketNames = new ArrayList<>();

    /**
     * Mağazalar sayfasında, belirtilen harfle başlayan mağazaları filtreler.
     *
     * @param letter Mağazaların başlangıç harfi.
     */
    public void filtersForStoresStartingWithTheLetter(String letter) {
        assertVisible(MARKETS_WITHS_START(letter), "All stores could not be displayed in the filter area on the stores page.");
        scrollToElement(findElement(MARKETS_WITHS_START(letter), 10));
        clickJs(findElement(MARKETS_WITHS_START(letter), 5));
        logger().info("Click the letter with start '%s'".formatted(letter));
    }

    /**
     * Listeden rastgele bir mağaza seçer ve tıklar.
     */
    public void pressOnARandomStoreFromTheList() {
        timeUnitMilliSeconds(2000);
        assertVisible(MARKETS_PAGE_MARKET_LIST, "Market Listesi Görüntülenemedi");
        int randomElementFromMarketLst = randomNumber(0, findElements(MARKETS_PAGE_MARKET_LIST).size() - 1);
        scrollToElement(findElements(MARKETS_PAGE_MARKET_LIST).get(randomElementFromMarketLst));
        setMarketName(findElements(MARKETS_PAGE_MARKET_LIST).get(randomElementFromMarketLst).getText());
        clickJs(findElements(MARKETS_PAGE_MARKET_LIST).get(randomElementFromMarketLst));
    }

    /**
     * Seçilen mağaza sayfasının doğru açıldığını doğrular.
     * Arama alanındaki ve sonuç metnindeki mağaza adını kontrol eder.
     */
    public void selectedStorePageIsOpened() {
        assertVisible(SEARCH_AREA, "Sayfa Görüntülenemedi");
        if (isElementVisible(MARKET_RESULT_TXT, 5)){
            assertEquals(getText(MARKET_RESULT_TXT), getMarketName()
                    , "The selected store's page is opened incorrectly.");
        }else{
            Assert.fail(getText(NOT_FOUND_TXT) + " sayfası görüntülendi");
        }
        assertEquals(getAttribute(SEARCH_AREA, "value"), getMarketName(), "The selected store's page is opened incorrectly.");
        logger().info("The selected store's page is opened correctly.");
    }

    /**
     * Arama çubuğuna belirtilen kelimeyi yazar ve arama yapar.
     *
     * @param word Aranacak kelime.
     */
    public void theUserSearchesForTheWord(String word) {
        assertVisible(SEARCH_AREA, " search bar görüntülenemedi.");
        sendKeyElement(SEARCH_AREA, word, true);
        logger().info("Search for '%s'".formatted(word));
    }

    /**
     * Sayfadaki ilk ve son ürünü sepete ekler.
     * Eğer sadece bir ürün varsa, sadece onu ekler.
     */
    public void addsTheFirstAndTheLastProductToTheBasket() {

        assertVisible(PRODUCT_LST, "Products could not be displayed on the page.");


        List<WebElement> products = findElements(PRODUCT_LST);
        if (products.isEmpty()) {
            logger().warn("No products found on the page to add to the basket.");
            return;
        }

        WebElement firstProduct = products.getFirst();
        addProduct(firstProduct, "first");

        if (products.size() > 1) {
            timeUnitMilliSeconds(2000);
            WebElement lastProduct = products.getLast();
            scrollToElement(lastProduct);
            addProduct(lastProduct, "last");
        }
    }

    /**
     * Belirtilen bir ürün elementini sepete ekler.
     * Ürün seçenekleri (renk, hafıza vb.) çıkarsa bunları da seçer.
     *
     * @param productElement Sepete eklenecek ürünün WebElement'i
     * @param positionLog    Loglama için ürünün pozisyonu ("first", "last" vb.)
     */
    private void addProduct(WebElement productElement, String positionLog) {
        String productName = productElement.findElement(PRODUCT_NAME_LST).getText();
        marketNames.add(productName);

        clickJs(productElement.findElement(PRODUCT_BASKET_LST));

        // Ürün seçenekleri (varyant) penceresi açılırsa işle
        if (isElementVisible(PRODUCT_CONTAINER, 5)) {
            handleProductOptions();
            timeUnitMilliSeconds(2000);
            clickJs(findElement(ADD_BASKET_PRODUCT, 5));
        }
        logger().info("Added the {} product: {}", positionLog, productName);
    }

    /**
     * Ürün seçenekleri (renk, beden vb.) formundaki ilk seçenekleri tıklar.
     */
    private void handleProductOptions() {
        List<WebElement> optionGroups = findElements(BASKET_OPTION_LST);

        for (int i = 0; i < optionGroups.size(); i++) {
            String selector = String.format(".sku-container > fieldset:nth-of-type(%d) .skus label", i + 1);
            clickJs(findElements(By.cssSelector(selector)).getFirst());
        }
    }

    /**
     * Sepette iki ürün olduğunu doğrular ve bu ürünlerin daha önce
     * eklenen ürünlerle eşleştiğini kontrol eder.
     */
    public void verifyThatThereAreTwoItemsInTheBasket() {
        assertVisible(HOME_PAGE_BASKET, "Basket could not be displayed on the page.");
        scrollToElement(findElement(HOME_PAGE_BASKET, 5));
        clickElement(HOME_PAGE_BASKET);
        logger().info("Verify that there are two items in the basket.");
        assertVisible(PRODUCT_DESCRIPTION_LST, "Products could not be displayed on the page.");
        List<String> productTexts = findElements(PRODUCT_DESCRIPTION_LST).stream()
                .map(WebElement::getText)
                .toList();

        List<String> basketProducts = new ArrayList<>(productTexts);
        Collections.sort(basketProducts);
        Collections.sort(getMarketNames());
        assertEquals(basketProducts, getMarketNames(), "The basket does not contain the correct products.");
    }

    /**
     * Ürün listeleme sayfasındaki filtrelerden belirli bir markayı seçer.
     *
     * @param nthBrand Seçilecek markanın listedeki konumu (indis).
     */
    public void selectsTheBrandFromTheFiltersOnTheProductListingPage(int nthBrand) {
        assertVisible(PRODUCT_BRAND_APPLE, "");
        scrollToElement(findElement(PRODUCT_BRAND_APPLE, 5));
        clickJs(findElement(PRODUCT_BRAND_APPLE, 5));
        logger().info("Selecting the brand {} from the product listing page.", nthBrand);
    }

    /**
     * Ürün sonuçlarını belirli bir parametreye göre sıralar ve sıralamanın doğru olduğunu doğrular.
     *
     * @param parameter Sıralama için kullanılacak parametre (örneğin "yorum sayısı").
     */
    public void sortsTheResultsBy(String parameter) {
        timeUnitMilliSeconds(2000);
        assertVisible(PRODUCT_SORT_FILTER, "");
        clickElement(PRODUCT_SORT_FILTER);
        assertVisible(PRODUCT_SORT_FILTER_REVIEWS, "");
        clickElement(PRODUCT_SORT_FILTER_REVIEWS);
        timeUnitMilliSeconds(2000);
        List<Integer> reviewList = findElements(PRODUCT_REVIEWS_LST).stream()
                .map(WebElement::getText)
                .map(text -> text.replaceAll("[^0-9]", ""))
                .map(Integer::parseInt)
                .toList();

        for (int i = 0; i < reviewList.size() - 1; i++) {
            int current = reviewList.get(i);
            int next = reviewList.get(i + 1);

            Assert.assertTrue(current >= next,
                    "Liste büyükten küçüğe doğru sıralanmamış. Hata: " + current + " < " + next);
        }

        logger().info("Sorting by {} ", parameter);
    }

    /**
     * Listelenen tüm ürünlerin "Ücretsiz Kargo" etiketine sahip olduğunu doğrular.
     */
    public void theUserShouldVerifyThatAllListedProductsHaveFreeShipping() {
        assertVisible(CARGO_BADGE_TEXT_LST, "Ücretsiz Kargo alanı görüntülenemedi.");
        List<WebElement> productBadges = findElements(CARGO_BADGE_TEXT_LST);

        for (int i = 0; i < productBadges.size(); i++) {
            WebElement productBadge = productBadges.get(i);
            String text = productBadge.getText();

            if (!text.contains("ÜCRETSİZ KARGO")) {
                Assert.fail("Test başarısız oldu! " + (i + 1) + ". üründe beklenmeyen metin bulundu.\n" +
                        "Beklenen: ÜCRETSİZ KARGO metnini içermesi.\n" +
                        "Bulunan: '" + text + "'");
            }
        }

    }

    /**
     * Arama çubuğuna "iphone" kelimesini yazar, arama yapar ve elde edilen
     * tüm çerezleri bir dosyaya kaydeder.
     *
     * @throws IOException Dosya yazma sırasında bir hata oluşursa fırlatılır.
     */
    public void takeSearchCookie() throws IOException {
        assertVisible(SEARCH_AREA, " search bar görüntülenemedi.");
        sendKeyElement(SEARCH_AREA, "iphone", true);
        timeUnitMilliSeconds(5000);
        Set<Cookie> allCookies = DriverFactory.driverPool.get().manage().getCookies();
        FileWriter fileWriter = new FileWriter("n11_cookies.txt");
        for (Cookie cookie : allCookies) {
            fileWriter.write(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        fileWriter.close();

        logger().info("Search cookie taken successfully.");

    }

    // Getter ve Setter Metotları
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public List<String> getMarketNames() {
        return marketNames;
    }

    public void setMarketNames(List<String> marketNames) {
        this.marketNames = marketNames;
    }
}