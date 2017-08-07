package com.cg.app.routes;

import com.cg.myflow.consumer.RouteId;
import static com.cg.myflow.core.Components.direct;
import static com.cg.myflow.core.Expression.claims;
import static com.cg.myflow.core.Expression.model;
import static com.cg.myflow.core.Expression.page;
import org.springframework.stereotype.Component;

@Component
public class PageRoutes {

    public PageRoutes() {

        new RouteId("page/home")
                .to(direct("required_auth"))
                .update(model("username", claims("username")), page("hello"));

        new RouteId("page/dailyReport")
                .to(direct("required_auth"))
                .to(direct("dailyReport/get"))
                .to(direct("task/list"))
                .update(page("report"));

        new RouteId("page/tasks")
                .to(direct("required_auth"))
                .to(direct("task/list"))
                .update(page("tasks"));
        
        new RouteId("page/users")
                .to(direct("required_auth"))
                .to(direct("user/list"))
                .update(page("users"));
    }
}
