package com.cg.myflow.core;

import java.util.Map;

public class MyFlowUtil {

    public static boolean trueOrNotNull(Object object) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        } else {
            return object != null;
        }
    }

    public static Integer mapValueAsInt(Map map, String key, Integer defaultValue) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            int len = keys.length;
            for (int i = 0; i < len - 1; i++) {
                if (map.containsKey(keys[i])) {
                    map = (Map) map.get(keys[i]);
                } else {
                    return defaultValue;
                }
            }
            return tryParseInt(map.get(keys[len - 1]), defaultValue);
        } else {
            return tryParseInt(map.get(key), defaultValue);
        }
    }

    public static Integer tryParseInt(Object o, Integer defaultValue) {
        try {
            return Integer.parseInt(o.toString());
        } catch (Throwable t) {
            return defaultValue;
        }
    }
}
