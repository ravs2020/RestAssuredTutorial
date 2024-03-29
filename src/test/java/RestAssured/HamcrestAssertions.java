package RestAssured;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/*Hamcrest assertions to be used directly after then() method for assertion
and not works with jsonpath() method*/
public class HamcrestAssertions {

    @Test
    public void stringAssertions(){
        given()
        .when()
        .get("https://reqres.in/api/users?page=2")
        .then()
                .body("data[0].first_name",startsWith("Mi"))
                .body("data[0].last_name",endsWith("son"))
                .body("data[0].email",containsString("@reqres.in"))
                .body("data[1].first_name",equalTo("Lindsay"))
                .body("data[1].last_name",equalToIgnoringCase("ferguson"));

    }

    @Test
    public void numberAssertions(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .body("page",equalTo(2))
                .body("per_page",is(notNullValue()))
                .body("total",not(equalTo(10)));
    }
}
