package utilities;

import entities.RequestBody;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CashwiseAuthorization {
    public static String getToken() {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/auth/login";
        RequestBody requestBody = new RequestBody();
        requestBody.setEmail("bedalbekovelaman@gmail.com");
        requestBody.setPassword("elamanbedalbekov");

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody).post(url);

        return response.jsonPath().getString("jwt_token");

    }
}
