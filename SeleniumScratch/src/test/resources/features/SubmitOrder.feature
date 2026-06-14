Feature: Submit Order on E-Commerce Platform
  As a registered user
  I want to add a product to my cart

  Scenario: Add IPHONE 13 PRO to cart
    Given I am logged in with valid credentials
    When I add the product "IPHONE 13 PRO" to my cart
    And I navigate to the cart
    Then the product "IPHONE 13 PRO" should be visible in the cart
