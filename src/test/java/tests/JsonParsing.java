package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import pojo.MyObject;

public class JsonParsing {
    //*Java object(POJO) to json*//
    //@Test
    public void serializeJson() throws JsonProcessingException {
        MyObject obj1 = new MyObject();
        obj1.setName("John");
        obj1.setAge("30");

        //approach 1 - jackson
        ObjectMapper om = new ObjectMapper();
        String json = om.writeValueAsString(obj1);
        System.out.println("Serialization using jackson "+json);

        //approach 2 - gson
        Gson gs = new Gson();
        String json1 = gs.toJson(obj1);
        System.out.println("Serialization using Gson "+json1);
    }

    //*Json to java object(POJO)*//
    //@Test
    public void deserializeJson() throws JsonProcessingException {
        String json = "{\"name\":\"John\",\"age\":30}";

        //approach 1 - jackson
        ObjectMapper om = new ObjectMapper();
        MyObject obj1 = om.readValue(json, MyObject.class);
        System.out.println("Deserialization using jackson "+ obj1.getName()+" "+obj1.getAge());

        //approach 2 - gson
        Gson gs = new Gson();
        MyObject obj2 = gs.fromJson(json, MyObject.class);
        System.out.println("Deserialization using Gson "+ obj2.getName()+" "+obj2.getAge());
    }
}
