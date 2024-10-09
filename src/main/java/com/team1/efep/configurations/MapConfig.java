package com.team1.efep.configurations;

import java.util.Map;

public class MapConfig {
    public static Map<String, String> buildMapKey(Map<String, String> map, String newError) {
        map.put("err", newError);
        return map;
    }
}
