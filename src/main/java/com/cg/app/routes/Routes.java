package com.cg.app.routes;

import com.cg.myflow.core.Components;
import com.cg.myflow.consumer.RouteId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Routes {

    public Routes(@Autowired Components comp) {
        new RouteId("login").to(comp.auth());
    }
}
