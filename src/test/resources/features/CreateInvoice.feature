@regression
Feature: creates an invoice

  Background:
    Given base URL "https://backend.cashwise.us/api/myaccount"


    @createInvoice @smoke
  Scenario: user successfully creates an invoices
    And I have access
    And I have the endpoint "/invoices"
    And I have "invoice_title" with "Java Invoice" in request body
    And I have "client_id" with "3" in request body
    And I have "date_of_creation" with "2024-05-30" in request body
    And I have "end_date" with "2024-06-29" in request body
    And I have "products" with product
    And I have "sum" with "0" in request body
    And I have "discount" with "0" in request body
    And I have "sum_of_discount" with "0" in request body
    When I send POST request
    Then verify status code is 201
    And verify I have "invoice_title" with "Java Invoice" in response body

  @updateInvoice
  Scenario: user can update invoice
    And I have access
    And I have the endpoint "/invoices"
    And I have "invoice_title" with "Lagman Invoice" in request body
    And I have "client_id" with "2" in request body
    And I have "date_of_creation" with "2024-05-30" in request body
    And I have "end_date" with "2024-06-29" in request body
    And I have "products" with product
    And I have "sum" with "0" in request body
    And I have "discount" with "0" in request body
    And I have "sum_of_discount" with "0" in request body
    When I send POST request
    Then verify status code is 201
    And verify I have "invoice_title" with "Lagman Invoice" in response body