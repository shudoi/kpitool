package com.cg.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cg.app.util.CollectionUtil;
import com.cg.myflow.core.Endpoints;
import com.cg.myflow.core.Exchange;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;

@Controller
public class PageController {

    @Autowired
    private MongoClient client;

    @RequestMapping("/hello")
    String index() {
        return "index";
    }

    @RequestMapping("/")
    String hello(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        if (jwtToken == null) {
            return "login";
        } else {
            Exchange exchange = new Exchange();
            exchange.setHeader("jwt", jwtToken);
            Endpoints.sendTo("login", exchange);
            String username = exchange.getClaims().get("username", String.class);
            System.out.println(username);
            model.put("username", username);
            return "hello";
        }
    }

    @RequestMapping("/users")
    String users(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        if (jwtToken == null) {
            return "login";
        } else {
            Exchange exchange = new Exchange();
            exchange.setHeader("jwt", jwtToken);
            Endpoints.sendTo("login", exchange);
            String username = exchange.getClaims().get("username", String.class);
            System.out.println(username);
            model.put("username", username);
            return "users";
        }
    }

    @RequestMapping("/report")
    String report(@CookieValue(value = "jwt", required = false) String jwtToken, Map<String, Object> model) {
        System.out.println(jwtToken);
        if (jwtToken == null) {
            return "login";
        } else {
            Exchange exchange = new Exchange();
            exchange.setHeader("jwt", jwtToken);
            Endpoints.sendTo("login", exchange);
            String username = exchange.getClaims().get("username", String.class);
            List<Document> tasks = StreamSupport.stream(client.getDatabase("heroku_l2hknpxf").getCollection("tasks").find().spliterator(), false).collect(Collectors.toList());
            MongoCursor<Document> iterator = client.getDatabase("heroku_l2hknpxf").getCollection("report").find(new Document().append("username", username).append("date", "2017-07-25")).iterator();
            if (iterator.hasNext()) {
                List<Document> tableData = iterator.next().get("data", List.class);
                model.put("tableData", tableData);
                CollectionUtil.mergeTasksTotalProgressToReport(tableData, tasks);
            } else {
                model.put("tableData", new ArrayList<>());
            }

            model.put("username", username);
            model.put("tasks", tasks);
            return "report";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    String login() {
        return "login";
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    String tasks(Map<String, Object> model) {
        List<Document> tasks = StreamSupport.stream(client.getDatabase("heroku_l2hknpxf").getCollection("tasks").find().spliterator(), false).collect(Collectors.toList());
        model.put("tasks", tasks);
        return "tasks";
    }
}
