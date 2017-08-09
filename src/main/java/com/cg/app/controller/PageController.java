package com.cg.app.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cg.app.entity.DailyReport;
import com.cg.myflow.core.Endpoints;
import com.cg.myflow.core.Exchange;

@Controller
public class PageController {

    @RequestMapping("/hello")
    String index() {
        return "index";
    }

    @RequestMapping("/")
    String hello(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        Exchange exchange = new Exchange();
        exchange.setModel(model);
        exchange.setHeader("jwt", jwtToken);
        Endpoints.sendTo("page/home", exchange);
        Endpoints.sendTo("_user/list", exchange);
        return exchange.getPage();
    }

    @RequestMapping("/users")
    String users(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        Exchange exchange = new Exchange();
        exchange.setHeader("jwt", jwtToken);
        exchange.setModel(model);
        Endpoints.sendTo("page/users", exchange);
        model.put("users", exchange.getHeader("data"));
        return exchange.getPage();
    }

    @RequestMapping("/report")
    String report(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        Exchange exchange = new Exchange();
        exchange.setHeader("jwt", jwtToken);
        exchange.setModel(model);
        Endpoints.sendTo("page/dailyReport", exchange);
        model.put("fooData", exchange.getRawData(DailyReport.class));
        return exchange.getPage();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String login() {
        return "login";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    String tasks(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        Exchange exchange = new Exchange();
        exchange.setHeader("jwt", jwtToken);
        exchange.setModel(model);
        Endpoints.sendTo("page/tasks", exchange);
        return exchange.getPage();
    }
}
