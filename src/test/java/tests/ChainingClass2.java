package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class ChainingClass2 {

    @Test
    public void updateUser(ITestContext context){
        HashMap<String,String> data = new HashMap<>();
        data.put("name","morpheus");
        data.put("job","boss");
        int id = (Integer)context.getAttribute("userid");
        Response rsp = given()
                .contentType("application/json")
                .body(data)
                .when()
                .put("https://reqres.in/api/users/"+id);

        Assert.assertEquals(rsp.getStatusCode(),200);
        System.out.println(rsp.getBody().asString());
        System.out.println("Job updated to id "+id);
    }
}
