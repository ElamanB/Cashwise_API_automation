import com.sun.net.httpserver.*;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class IntroToAPI {


    public static void main(String[] args) {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTg1NDgyNTIsImlhdCI6MTcx" +
                "NTk1NjI1MiwidXNlcm5hbWUiOiJiZWRhbGJla292ZWxhbWFuQGdtYWlsLmNvbSJ9.8GcHv8htnjvZdIy" +
                "tWgmBCikg-aB4ays-5icn-fQucOJLtYrNeHmxKxlEMruj1ybgKeJwqQDyzb1S3RByQaV8iQ";

        RestAssured.given()
                .auth()
                .oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .when()
                .get("/myaccount/sellers/all")
                .then()
                .statusCode(200);


        RestAssured.given()
                .auth()
                .oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .when()
                .get("/myaccount/tags/all")
                .then()
                .statusCode(200);

        RestAssured.given()
                .auth()
                .oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .queryParam("page", "1")
                .queryParam("size", 4)
                .when()
                .get("/myaccount/reminder/requests")
                .then()
                .statusCode(200);

        RestAssured.given()
                .auth()
                .oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .when()
                .get("/myaccount/reminder/notifications")
                .then()
                .statusCode(200);

    }
}
