Feature: Test amazon basic functionality
  This is a test task to assess Veselin Petrov's ability to create automation test over Amazon page using BDD, Selenium and Java.

  Scenario: Search, view product details, add to basket, edit basket
    # Open browser and visit desired page:
    Given I open new "Firefox" browser
    When I visit the page "https://www.amazon.co.uk/"
    Then The page "https://www.amazon.co.uk/" is correct and opened
    # Perform search using given search term and verify results:
    When I search for "Game of Thrones" in section Books
    Then The first item in the results list has the title "A Game of Thrones (A Song of Ice and Fire, Book 1)"
    And It has a badge "Best Seller"
    And the type "Paperback" has price "£4.00"
    And the type "Kindle Edition" has price "£3.99"
    # Navigate to item details page and assert item attributes:
    When I click over the first item in the search result list
    Then The item details page is opened
    And the title of the item is "A Game of Thrones (A Song of Ice and Fire, Book 1)"
    And the item has "Best Seller" badge
    And the type "Paperback" is selected and has price "£4.00"
    # Add item to basket and assert notification appears with correct title and quantity:
    When I add product to basket
    Then Notification about product added to basket is shown
    And The notification shows that there is "1" item added to basket
    # Edit the basket and assert basket content and item attributes
    When I click the 'Edit basket' button
    Then there is "1" item inside the basket
    And the 1st item inside the basket has attributes as follows:
      | title    | A Game of Thrones (A Song of Ice and Fire, Book 1) |
      | type     | Paperback                                          |
      | price    | £4.00                                              |
      | quantity | 1                                                  |
    And the basket total price is "£4.00"
    Given I close the browser
