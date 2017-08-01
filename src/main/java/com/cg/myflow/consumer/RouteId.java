package com.cg.myflow.consumer;

import com.cg.myflow.Consumer;
import com.cg.myflow.Endpoints;

public class RouteId extends Consumer {

    public RouteId(String routeId) {
        super(null);
        this.consumer = this;
        Endpoints.setEndpoint(routeId, this.consumer);
    }
}
