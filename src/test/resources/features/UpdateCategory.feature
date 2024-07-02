@regression
Feature: verify user can update a category


  Scenario: verify user can successfully update a category_title
    Given base URL "https://backend.cashwise.us/api/myaccount"
    And I have access
    And I have the endpoint "/categories"
    And I have "category_title" with "QA Engineer" in request body
    And I have "category_description" with "Engineer" in request body
    And I have "flag" with "true" in request body
    When I send POST request
    Then  verify status code is 201
    And I have "category_title" with "Rocket science" in request body
    And I retrieve id for "category_id"
    When I send PUT request
    Then verify I have "category_title" with "Rocket science" in response body
    Then verify status code is 200

