package com.cg.myflow.consumer;

import com.cg.myflow.core.Consumer;
import com.cg.myflow.core.Endpoints;

public class RouteId extends Consumer {

    public RouteId(String routeId) {
        super(null);
        this.consumer = this;
        Endpoints.setEndpoint(routeId, this.consumer);
    }
}
