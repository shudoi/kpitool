package com.cg.app.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cg.myflow.consumer.RouteId;
import com.cg.myflow.core.Components;

@Component
public class Routes {

	@Autowired
	Components comp;

	public Routes(@Autowired Components comp) {
		new RouteId("login").to(comp.auth());

		new RouteId("required_auth")
				.to(comp.direct("check_jwt"));
	}
}
