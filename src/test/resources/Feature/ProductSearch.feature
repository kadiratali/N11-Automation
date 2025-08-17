Feature: n11.com Regression Set

  @Store @C2323897 @Regression
  Scenario: Store Search and Selection
    Given The user opens the "magazalar" page
    When The user filters for stores starting with the letter "S"
    And Press on a random store from the list
    Then Verify that the selected store's page is opened

  @AddToCart @Regression
  Scenario: Product Search and Add to Cart
    When The user searches for the word "iPhone"
    And Adds the first and the last product to the basket
    Then Verify that there are two items in the basket

  @FilterAndSort @Regression
  Scenario: Phone Search, Filter, Sort, and List
    When The user searches for the word "telefon"
    And Selects the 2 brand from the filters on the product listing page
    And Sorts the results by "Yorum Sayısı"
    Then The user should verify that all listed products have free shipping