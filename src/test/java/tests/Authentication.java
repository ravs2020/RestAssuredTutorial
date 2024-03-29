package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Authentication {


   /* Different authentication:
    Basic,Digest,Preemptive}==>these follow same code but underlying algorithm is different,
    used mostly for email authentication like gmail, yahoo etc.
    Oauth1.0(deprecated)
    Oauth2.0
    API Keys*/

    @Test
    public void testBasicAuthentication(){
        given()
           .auth().basic("postman","password")
        .when()
           .get("https://postman-echo.com/basic-auth")
        .then()
           .statusCode(200)
           .body("authenticated",equalTo(true))
           .log().all();
    }

    @Test
    public void testDigestAuthentication(){
        given()
            .auth().digest("postman","password")
        .when()
            .get("https://postman-echo.com/basic-auth")
        .then()
            .statusCode(200)
            .body("authenticated",equalTo(true))
            .log().all();
    }

    //note - preemptive internally calls basic authentication
    @Test
    public void testPreemptiveAuthentication(){
        given()
                .auth().preemptive().basic("postman","password")
                .when()
                .get("https://postman-echo.com/basic-auth")
                .then()
                .statusCode(200)
                .body("authenticated",equalTo(true))
                .log().all();
    }

    //note - bearer token passed along with headers
    @Test
    public void testBearerAuthentication(){
        //token generated from github by navigating:account=>Settings=>Developer settings=>Personal access tokens
        String token = "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";
        given()
            .headers("Authorization","Bearer "+token)
        .when()
            .get("https://api.github.com/user/repos")
        .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    public void testOauth2Authentication(){
        //token generated from github by navigating:account=>Settings=>Developer settings=>Personal access tokens
        String token = "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";
        given()
        .auth().oauth2(token)
        .when()
        .get("https://api.github.com/user/repos")
        .then()
        .statusCode(200)
        .log().all();
    }
}
