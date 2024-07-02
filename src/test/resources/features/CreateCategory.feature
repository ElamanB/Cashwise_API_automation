@regression
Feature: user should be able to create a category

  @createCategory
  Scenario: verify user successfully creates a category
    Given base URL "https://backend.cashwise.us/api/myaccount"
    And I have access
    And I have the endpoint "/categories"
    And I have "category_title" with "QA Engineer" in request body
    And I have "category_description" with "Engineer" in request body
    And I have "flag" with "true" in request body
    When I send POST request
    Then  verify status code is 201
    And I retrieve id for "category_id"
    Then I delete "Foods" category in database
    Then verify "Foods" is deleted from database using GET request


    @smoke
  Scenario: verify flag accepts only boolean
    Given base URL "https://backend.cashwise.us/api/myaccount"
    And I have access
    And I have the endpoint "/categories"
    And I have "category_title" with "QA Engineer" in request body
    And I have "category_description" with "Engineer" in request body
    And I have "flag" with "real" in request body
    When I send POST request
    Then  verify status code is 500
    And I have "flag" with "FALSE" in request body
    When I send POST request
    Then  verify status code is 201

  Scenario Outline: verify flag accepts only boolean
    Given base URL "https://backend.cashwise.us/api/myaccount"
    And I have access
    And I have the endpoint "/categories"
    And I have "category_title" with "QA Engineer" in request body
    And I have "category_description" with "Engineer" in request body
    And I have "flag" with "<valueOfFlag>" in request body
    When I send POST request
    Then  verify status code is 500
    Examples:
      | valueOfFlag |
      | real madrid |
      | falsee      |
      | truefalse   |
      |             |
      | 8428842     |
      | 684.4       |

  Scenario: verify category_title doesn't accept more than 255 characters
    Given base URL "https://backend.cashwise.us/api/myaccount"
    And I have access
    And I have the endpoint "/categories"
    And I have "category_title" with "tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt" in request body
    And I have "category_description" with "Engineer" in request body
    And I have "flag" with "true" in request body
    When I send POST request
    Then  verify status code is 500
