package com.cg.app.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cg.myflow.core.Endpoints;
import com.cg.myflow.core.Exchange;

@RestController
public class AjaxController {

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange login(@RequestBody Exchange exchange) {
        Endpoints.sendTo("login_action", exchange);
        return exchange;
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange report(@RequestBody Exchange exchange) {
        Endpoints.sendTo("dailyReport/update", exchange);
        return exchange;
    }

    @RequestMapping(value = "/task-info", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange taskInfo(@RequestBody Exchange exchange) {
        Endpoints.sendTo("task/info", exchange);
        return exchange;
    }
}
