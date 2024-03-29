package tests;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.xml.XmlPath;
import org.json.JSONObject;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpRequests {
    int id;
    String job;

    @Test(priority=1)
    public void getUsers(){
        Response rsp = given()
                        .when()
                        .get("https://reqres.in/api/users?page=2");

        System.out.println(rsp.getBody().asString());
        Assert.assertEquals(rsp.getStatusCode(), 200);
        Assert.assertEquals(rsp.jsonPath().getInt("page"), 2);
        //note - In case of xml response use xmlPath instead of jsonPath
        Assert.assertEquals(rsp.jsonPath().get("data[0].first_name"), "Michael");
    }

   @Test(priority=2)
    public void createUser(){
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

        Assert.assertEquals(rsp.getStatusCode(),201);
        Assert.assertEquals(rsp.jsonPath().getString("name"),"morpheus");
        id = Integer.parseInt(rsp.jsonPath().get("id"));
        System.out.println("ID created is: "+id);
    }

    @Test(priority=3, dependsOnMethods = {"createUser"})
    public void updateUser(){
        HashMap<String,String> data = new HashMap<>();
        data.put("name","morpheus");
        data.put("job","boss");

        Response rsp = given()
                .contentType("application/json")
                .body(data)
                .when()
                .put("https://reqres.in/api/users/"+id);
        job = rsp.jsonPath().get("job");
        Assert.assertEquals(rsp.getStatusCode(),200);
        Assert.assertEquals(job,"boss");
        System.out.println("Job# "+job+" updated to id "+id);
    }

    //API URL# https://reqres.in/api/users?page=2&id=5
    @Test
    public void testQueryPathParams(){
        given()
        .pathParams("mypath","users")
        .queryParam("page","2")
        .queryParam("id","5")
        .when()
        .get("https://reqres.in/api/{mypath}")
        .then()
        .statusCode(200)
        .log().all();
    }

    @Test
    public void getCookiesInfo(){
        Response rsp =given()
                .when()
                .get("https://www.google.com/");

        Map<String,String> cookiesLst = rsp.getCookies();
        for(String cookie:cookiesLst.keySet()){
            System.out.println("Cookie name: "+cookie+" and value: "+rsp.getCookie(cookie));
        }
    }

    @Test
    public void getHeaders(){
        Response rsp =given()
                .when()
                .get("https://www.google.com/");

        Headers hdrs = rsp.getHeaders();

        for(Header hdr:hdrs){
            System.out.println("Header name: "+hdr.getName()+" and value: "+hdr.getValue());
            if(hdr.getName().equalsIgnoreCase("Content-Type")){
                if(hdr.getValue().equalsIgnoreCase("text/html; charset=ISO-8859-1")){
                    System.out.println("Content type is as expected");
                }
                else{
                    System.out.println("Content type is not as expected");
                }
            }
        }
    }

    @Test
    public void testJsonResponse(){
        //Approach 1 - assuming the response is static
        Response rsp = given()
                //.contentType(ContentType.JSON)
                .when()
                .get("https://reqres.in/api/users?page=2");

        System.out.println("JsonPath: Id values are "+rsp.jsonPath().get("data.id") );
        System.out.println("JsonPath: Id value of first obj is "+rsp.jsonPath().get("data[0].id") );
        System.out.println("JsonPath: Id between 9 and 12 object "+rsp.jsonPath().get("data.findAll{data->data.id>9&&data.id<12}") );

        //Approach2 - assuming the response is dynamic (jsonobject inside jsonarray is retrieved in random order)
        Boolean flag = false;
        //As response started with {},JSONObject is used if in case response started with[] then JSONArray should be used
        JSONObject jo = new JSONObject(rsp.asString());
        //*asString() converts entire response to string,whereas toString() converts response data to string*//
        for(int i=0; i<jo.getJSONArray("data").length(); i++){
            String lastName = jo.getJSONArray("data").getJSONObject(i).get("last_name").toString();
            if(lastName.equalsIgnoreCase("Funke")){
                flag = true;
                break;
            }
        }

        Assert.assertTrue(flag,"Funke not found in json response");
    }

    @Test
    public void testXmlResponse(){
        //Response is getting 404 not found and this method written for illustration
        Response rsp = given()
                //.contentType(ContentType.JSON)
                .when()
                .get("http://restapi.adequateshop.com/api/Traveler?page=1");


        XmlPath xo = new XmlPath(rsp.asString());
        List<Object> traveller_info = xo.getList("TravelerInformationResponse.travelers.Travelerinformation");
        for(Object traveller:traveller_info){
            System.out.println("Traveller name: "+traveller.toString());
        }

    }

    @Test
    public void jsonSchemaValidation(){
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemaTests.json"));
        //note - In case of xml, RestAssuredMatchers.matchesXsdInClassPath() to be used
     }



}
