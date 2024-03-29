package tests;

import io.restassured.response.Response;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class ChainingClass1 {
    @Test
    public void createUser(ITestContext context){
        int id;
        //hashmap not recommended,but since only 2 key-value pair and so its fine to use
        HashMap<String,String> data = new HashMap<>();
        data.put("name","morpheus");
        data.put("job","leader");

        Response rsp = given()
                //.header("Content-Type", "application/json charset=utf-8")
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/users");
        id = Integer.parseInt(rsp.jsonPath().get("id"));
        System.out.println("ID created is: "+id);
        context.setAttribute("userid",id);
    }
}
