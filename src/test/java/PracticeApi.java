import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.it.Ma;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PracticeApi {

    @Test
    public void getUsers() {
        String baseUri = "https://reqres.in/";

        Response response = RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users?page=1");

        JsonPath jsonPath = response.jsonPath();
        int total = jsonPath.getInt("total");

        String jsonList = response.getBody().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        








    }
}
