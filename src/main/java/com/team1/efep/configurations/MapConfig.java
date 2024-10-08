package com.team1.efep.configurations;

import java.util.Map;

public class MapConfig {
    public static void buildMapKey(Map<String, String> map, String newError) {
        map.put("err" + (map.size() + 1), newError);
    }
}
