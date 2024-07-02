package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import oracle.jdbc.proxy.annotation.Pre;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import utilities.CashwiseAuthorization;
import utilities.DBUtilities;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class ProductSteps {

    RequestSpecification request;
    Response response;
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MjE5MjIwOTMsImlhdCI6MTcxOTMzMDA5MywidXNlcm5hbWUiOiJiZWRhbGJla292ZWxhbWFuQGdtYWlsLmNvbSJ9.moqEU9qJAmhjXZZbyyjm1kEvvlS3ei7iMYGJYdmwXVwpITd-YpoZWhEI__9PdHND-Y5sbiumzsmS_4BKCv1EZw";
    JSONObject requestBody = new JSONObject();
    String id;

    @Given("I have {string} with {string} as query param")
    public void i_have_with_as_query_param(String key, String value) {
        request = request.queryParam(key, value);
    }
    @When("I send GET request")
    public void i_send_get_request() {
        response = request.get();
    }
    @Then("verify the invoice details in the response match the database")
    public void verify_the_invoice_details_in_the_response_match_the_database() throws SQLException {
        Map<String, String> dataFromAPI = new HashMap<>();
        dataFromAPI.put("invoice_id", response.jsonPath().getString("invoice_id"));
        dataFromAPI.put("invoice_title", response.jsonPath().getString("invoice_title"));
        dataFromAPI.put("client_id", response.jsonPath().getString("client.client_id"));
        dataFromAPI.put("client_name", response.jsonPath().getString("client.client_name"));
        dataFromAPI.put("company_name", response.jsonPath().getString("client.company_name"));
        dataFromAPI.put("email", response.jsonPath().getString("client.email"));
        dataFromAPI.put("phone_number", response.jsonPath().getString("client.phone_number"));

        System.out.println(dataFromAPI);

        Connection connection = DBUtilities.getDBConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select id, title, i.client_id, client_name,\n" +
                "       company_name, email, phone_number\n" +
                "from invoices i\n" +
                "join clients c\n" +
                "on c.client_id = i.client_id\n" +
                "where id = ?");
        preparedStatement.setInt(1, Integer.parseInt(id));
        ResultSet resultSet = preparedStatement.executeQuery();

        Map<String, String> dataFromDB = new HashMap<>();

        while (resultSet.next()) {



            dataFromDB.put("invoice_id", resultSet.getString("id"));
            dataFromDB.put("invoice_title", resultSet.getString("title"));
            dataFromDB.put("client_id", resultSet.getString("client_id"));
            dataFromDB.put("client_name", resultSet.getString("client_name"));
            dataFromDB.put("company_name", resultSet.getString("company_name"));
            dataFromDB.put("email", resultSet.getString("email"));
            dataFromDB.put("phone_number", resultSet.getString("phone_number"));
        }

        Assert.assertEquals(dataFromAPI, dataFromDB);

    }


    @Given("base URL {string}")
    public void base_url(String baseURL) {
        request = RestAssured.given().baseUri(baseURL).contentType(ContentType.JSON);
    }

    @Given("I have access")
    public void i_have_access() {
        request = request.auth().oauth2(token);
    }

    @Given("I have the endpoint {string}")
    public void i_have_the_endpoint(String endpoint) {
        request = request.basePath(endpoint);
    }

    @Given("I have {string} with {string} in request body")
    public void i_have_with_in_request_body(String key, String value) {
        requestBody.put(key, value);
    }

    @When("I send POST request")
    public void i_send_post_request() {
        response = request.body(requestBody.toString()).post();
        response.prettyPrint();
    }

    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer statusCode) {
        Assert.assertEquals(response.statusCode(), statusCode);
    }

    @Then("verify I have {string} with {string} in response body")
    public void verify_i_have_with_in_response_body(String key, String value) {
        response.prettyPrint();
        response.then()
                .body(key, equalTo(value));
    }

    @Then("I delete the product")
    public void i_delete_the_product() {
        id = response.body().jsonPath().getString("product_id");
        response = RestAssured.given().baseUri("https://backend.cashwise.us/api/myaccount")
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .delete("/products/" + id);

        Assert.assertEquals(response.statusCode(), 200);

    }


    @Given("I have {string} with product")
    public void i_have_with_product(String products) {

        Response response = RestAssured.given()
                .baseUri("https://backend.cashwise.us/api/myaccount")
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .get("/products/1800");
        System.out.println(response.prettyPrint());

        JSONObject product = new JSONObject();
        product.put("product_title", response.jsonPath().getString("product_title"));
        product.put("product_id", response.jsonPath().getString("product_id"));
        product.put("count_of_product", 0);
        product.put("product_price", response.jsonPath().getString("product_price"));
        product.put("service_type_id", response.jsonPath().getString("service_type.service_type_id"));
        product.put("category_id", response.jsonPath().getString("category.category_id"));
        product.put("product_description", response.jsonPath().getString("product_description"));

        JSONArray arrayOfProducts = new JSONArray();
        arrayOfProducts.put(product);

        requestBody.put(products, arrayOfProducts);

    }

    @Then("verify the product is not present in product list")
    public void verify_the_product_is_not_present_in_product_list() {
        response = RestAssured.given().baseUri("https://backend.cashwise.us/api/myaccount")
                .contentType(ContentType.JSON)
                .auth()
                .oauth2(token)
                .when()
                .get("/products/" + id);
        response.prettyPrint();

        Assert.assertEquals(response.statusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("details[0]"), "Product not found");



    }
    @Then("I retrieve id for {string}")
    public void i_retrieve_id_for(String id) {
        this.id = response.jsonPath().getString(id);
    }

    @When("I send PUT request")
    public void i_send_put_request() {
        response = request.body(requestBody.toString()).put();
    }

    @Then("I delete {string} category in database")
    public void i_delete_category_in_database(String categoryTitle) throws SQLException {
        Connection connection = DBUtilities.getDBConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("delete from products where category_id = ?;delete from categories where category_id = ?");
        preparedStatement.setInt(1, Integer.parseInt(id));
        preparedStatement.setInt(2, Integer.parseInt(id));
        Assert.assertEquals(preparedStatement.executeUpdate(), 0);


    }
    @Then("verify {string} is deleted from database using GET request")
    public void verify_is_deleted_from_database_using_get_request(String categoryTitle) throws SQLException {
        Connection connection = DBUtilities.getDBConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("delete from products where category_id = ?;delete from categories where category_id = ?");
        preparedStatement.setInt(1, Integer.parseInt(id));
        preparedStatement.setInt(2, Integer.parseInt(id));
        Assert.assertEquals(preparedStatement.executeUpdate(), 0);
    }



}
