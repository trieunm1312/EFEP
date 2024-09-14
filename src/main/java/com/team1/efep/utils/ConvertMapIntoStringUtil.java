package com.team1.efep.utils;

import java.util.HashMap;
import java.util.Map;

public class ConvertMapIntoStringUtil {

    public static String convert(Map<String, String> map) {

        return String.join(", ", map.entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .toList());
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
//        map.put("key1", "value1");
//        map.put("key2", "value2");
        map.put("error", "invalid");
        System.out.println(convert(map));
    }
}
