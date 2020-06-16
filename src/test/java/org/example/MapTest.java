package org.example;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();

        // String name = map.get("name").toString();
        // NullPointerException

        String name = map.getOrDefault("name", "default").toString();
        System.out.println(name);
        // default
    }
}
