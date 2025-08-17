package gluecode;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.Search;

public class SearchStepDefitions {

    Search searchPage = new Search();
    @When("The user filters for stores starting with the letter {string}")
    public void theUserFiltersForStoresStartingWithTheLetter(String letter) {
        searchPage.filtersForStoresStartingWithTheLetter(letter);
    }

    @Given("The user opens the {string} page")
    public void theUserOpensThePage(String page) {
        searchPage.goToUrl(page);
    }

    @And("Press on a random store from the list")
    public void pressOnARandomStoreFromTheList() {
        searchPage.pressOnARandomStoreFromTheList();
    }

    @Then("Verify that the selected store's page is opened")
    public void verifyThatTheSelectedStoreSPageIsOpened() {
        searchPage.selectedStorePageIsOpened();
    }

    @When("The user searches for the word {string}")
    public void theUserSearchesForTheWord(String word) {
        searchPage.theUserSearchesForTheWord(word);
    }

    @And("Adds the first and the last product to the basket")
    public void addsTheFirstAndTheLastProductToTheBasket() {
        searchPage.addsTheFirstAndTheLastProductToTheBasket();
    }

    @Then("Verify that there are two items in the basket")
    public void verifyThatThereAreTwoItemsInTheBasket() {
        searchPage.verifyThatThereAreTwoItemsInTheBasket();
    }

    @And("Selects the {int} brand from the filters on the product listing page")
    public void selectsTheBrandFromTheFiltersOnTheProductListingPage(int nthBrand) {
        searchPage.selectsTheBrandFromTheFiltersOnTheProductListingPage(nthBrand);
    }

    @And("Sorts the results by {string}")
    public void sortsTheResultsBy(String parameter) {
        searchPage.sortsTheResultsBy(parameter);
    }

    @Then("The user should verify that all listed products have free shipping")
    public void theUserShouldVerifyThatAllListedProductsHaveFreeShipping() {
        searchPage.theUserShouldVerifyThatAllListedProductsHaveFreeShipping();
    }
}
