import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

import static org.hamcrest.Matchers.equalTo;

public class IntroToPostRequest {

    public static void main(String[] args) {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTg1NDgyNTIsImlhdCI6MTcx" +
                "NTk1NjI1MiwidXNlcm5hbWUiOiJiZWRhbGJla292ZWxhbWFuQGdtYWlsLmNvbSJ9.8GcHv8htnjvZdIy" +
                "tWgmBCikg-aB4ays-5icn-fQucOJLtYrNeHmxKxlEMruj1ybgKeJwqQDyzb1S3RByQaV8iQ";

        JSONObject requestBody = new JSONObject();

        requestBody.put("product_title", "blueberry");
        requestBody.put("product_price", 4);
        requestBody.put("service_type_id", 2);
        requestBody.put("category_id", 2);
        requestBody.put("product_description", "Italian");
        requestBody.put("date_of_payment", "2024-05-20");
        requestBody.put("remind_before_day", 2);
        requestBody.put("do_remind_every_month", "REPEAT_EVERY_MONTH");


        RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .auth()
                .oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .body(requestBody.toString())
                .when()
                .post("/myaccount/products")
                .then()
                .statusCode(201)
                .body("product_title", equalTo("blueberry"))
                .body("product_price", equalTo(4.0F));


//        Response response = RestAssured.given()
//                .contentType(ContentType.JSON)
//                .auth()
//                .oauth2(token)
//                .baseUri("")
//                .body(requestBody.toString())
//                .when()
//                .post("endpoint");

    }
}
