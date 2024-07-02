import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojo.Seller;
import pojo.Tag;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class IntroToSerialization {

    public static void main(String[] args) {

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTg1NDgyNTIsImlhdCI6MTcx" +
                "NTk1NjI1MiwidXNlcm5hbWUiOiJiZWRhbGJla292ZWxhbWFuQGdtYWlsLmNvbSJ9.8GcHv8htnjvZdIy" +
                "tWgmBCikg-aB4ays-5icn-fQucOJLtYrNeHmxKxlEMruj1ybgKeJwqQDyzb1S3RByQaV8iQ";

        /*
            1. create a POJO object
         */
        Tag tag = new Tag();
        tag.setName_tag("pineapple");
        tag.setDescription("this is pineapple");

        /*
            2. serialize POJO object to json object
         */
        Gson gson = new Gson();
        String requestBodyInJson = gson.toJson(tag);


        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .baseUri("https://backend.cashwise.us/api")
                .body(requestBodyInJson)
                .when()
                .post("/myaccount/tags/");

        System.out.println(response.statusCode());
        System.out.println(response.prettyPrint());

        /*
            3. deserialize json to java
         */

        String responseInJson = response.asString();
        gson = new Gson();

        Tag tagResponse = gson.fromJson(responseInJson, Tag.class);







    }

    @Test
    public void createSeller() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MTg1NDgyNTIsImlhdCI6MTcx" +
                "NTk1NjI1MiwidXNlcm5hbWUiOiJiZWRhbGJla292ZWxhbWFuQGdtYWlsLmNvbSJ9.8GcHv8htnjvZdIy" +
                "tWgmBCikg-aB4ays-5icn-fQucOJLtYrNeHmxKxlEMruj1ybgKeJwqQDyzb1S3RByQaV8iQ";

        Faker faker = new Faker();

        // 1. create pojo object
        Seller seller = new Seller();
        seller.setCompany_name(faker.company().name());
        seller.setSeller_name(faker.name().firstName());
        seller.setEmail(faker.internet().emailAddress());
        seller.setPhone_number(faker.phoneNumber().cellPhone());
        seller.setAddress(faker.address().streetAddress());

        // 2. serialize to json
        Gson gson = new Gson();
        String requestInJson = gson.toJson(seller, Seller.class);

        // 3. send request
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().oauth2(token)
                .baseUri("https://backend.cashwise.us/api/myaccount")
                .body(requestInJson)
                .when()
                .post("/sellers");

        // 4. deserialize from json to java
        String responseInJson = response.asString();
        gson = new Gson();

        Seller responseSeller = gson.fromJson(responseInJson, Seller.class);

        // 5. verify status code
        Assert.assertEquals(response.statusCode(), 201);

        // 6. verify response body
        Assert.assertEquals(responseSeller.getCompany_name(), seller.getCompany_name());

    }
}
