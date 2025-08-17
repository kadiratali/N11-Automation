package constants;

import org.openqa.selenium.By;

public class HomePageConstants {

    private HomePageConstants() {

    }

    public static By MARKETS_WITHS_START(String markets) {
        return By.cssSelector("[title='" + markets + "']");
    }

    public static final By MARKETS_PAGE_MARKET_LIST = By.cssSelector(".tabPanel:nth-of-type(4) ul li a");
    public static final By SEARCH_AREA = By.cssSelector("#searchData");
    public static final By MARKET_RESULT_TXT = By.cssSelector(".resultText h1");
    public static final By PRODUCT_LST = By.cssSelector("#listingUl li");
    public static final By PRODUCT_NAME_LST = By.cssSelector(".productName");
    public static final By PRODUCT_BASKET_LST = By.cssSelector(".btnBasket");
    public static final By ADD_BASKET_PRODUCT = By.cssSelector("#js-addBasketSku");

    public static final By BASKET_OPTION_LST = By.cssSelector(".sku-container > fieldset .skus");

    public static final By PRODUCT_CONTAINER = By.cssSelector(".product-container");
    public static final By HOME_PAGE_BASKET = By.cssSelector(".myBasketHolder [title='Sepetim']");
    public static final By PRODUCT_DESCRIPTION_LST = By.cssSelector(".prodDescription");

    public static final By PRODUCT_BRAND_APPLE = By.cssSelector("[for='brand-m-Apple']");
    public static final By PRODUCT_SORT_FILTER = By.cssSelector(".selected-item");
    public static final By PRODUCT_SORT_FILTER_REVIEWS = By.cssSelector("[data-value='REVIEWS']");
    public static final By PRODUCT_REVIEWS_LST = By.cssSelector(".ratingCont .ratingText");
    public static final By CARGO_BADGE_TEXT_LST = By.cssSelector(".cargoBadgeText");
    public static final By NOT_FOUND_TXT = By.cssSelector(".notFound h1");
}
