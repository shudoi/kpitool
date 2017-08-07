package com.cg.myflow.core;

import java.util.function.Function;

public class Components {

    public static Function<Exchange, Exchange> direct(String endpointName) {
        return (exchange) -> {
            return Endpoints.sendTo(endpointName, exchange);
        };
    }

    public static Function<Exchange, Exchange> log(String logName) {
        return (exchange) -> {
            System.out.println("log:" + logName + ":body " + exchange.getBody());
            System.out.println("log:" + logName + ":header " + exchange.getHeaders());
            return exchange;
        };
    }
}
