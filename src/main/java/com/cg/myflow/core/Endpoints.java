package com.cg.myflow.core;

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
        Consumer consumer = MAPPING.get(endpointName);
        if(consumer == null){
            System.out.println("endpoint:" + endpointName +" doesn't exist.");
        }
        return consumer.consume(exchange);
    }
}
