package com.cg.myflow;

import java.util.LinkedHashMap;
import java.util.Map;

public class Endpoints {

    private static final Map<String, Consumer> MAPPING = new LinkedHashMap<>();

    public static Consumer getEndpoint(String endpointName) {
        return MAPPING.get(endpointName);
    }

    public static void setEndpoint(String endpointName, Consumer consumer) {
        MAPPING.put(endpointName, consumer);
    }

    public static Exchange sendTo(String endpointName, Exchange exchange) {
        return MAPPING.get(endpointName).consume(exchange);
    }
}
