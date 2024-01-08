package org.example.other.network;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Log4j2
class ParserTest {


    @Test
    void testJsonParser(){
        String test = "{\n" +
                "  \"people\": [\n" +
                "    {\n" +
                "      \"name\": \"John\",\n" +
                "      \"age\": 30,\n" +
                "      \"hobbies\": [\"Reading\", \"Traveling\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Alice\",\n" +
                "      \"age\": 25,\n" +
                "      \"hobbies\": [\"Painting\", \"Gardening\"]\n" +
                "    }\n" +
                "  ]\n" +
                "";
        System.out.println(JsonParser.parse(test));

        Map<String, Object> map = JsonParser.parse(test);

        System.out.println(JsonParser.toJson(map));
    }

    @Test
    void toJsonTest(){
        Map<String, Object> map = Map.of("name", "John", "age", 30, "hobbies", List.of("Reading", "Traveling"));
    }

}