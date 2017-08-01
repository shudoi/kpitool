package com.cg;

import com.cg.myflow.Components;
import com.cg.myflow.Endpoints;
import com.cg.myflow.consumer.RouteId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Routes {

    public Routes(@Autowired Components comp) {
        new RouteId("foo").to((exchange) -> {
            System.out.println("fooooo");
            return exchange;
        });

        new RouteId("bar").to(comp.direct("foo"));

        new RouteId("login").to(comp.auth());
    }
}
