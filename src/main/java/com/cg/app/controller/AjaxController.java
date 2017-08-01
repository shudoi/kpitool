package com.cg.app.controller;

import com.cg.app.util.MongoUtil;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cg.myflow.core.Endpoints;
import com.cg.myflow.core.Exchange;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

import io.jsonwebtoken.Claims;

@RestController
public class AjaxController {

    @Autowired
    MongoClient client;

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange login(@RequestBody Exchange exchange) {
        Endpoints.sendTo("login", exchange);
        return exchange;
    }

    @RequestMapping(value = "/report", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange report(@RequestBody Exchange exchange) {
        Endpoints.sendTo("login", exchange);
        Claims claims = exchange.getClaims();
        if (claims != null) {
            String username = claims.get("username", String.class);
            String date = "2017-07-25";
            MongoUtil.getCollection(client, "report").updateOne(Filters.and(
                    Filters.eq("username", username), Filters.eq("date", date)),
                    new Document("$set", new Document()
                            .append("data", exchange.getHeader("data"))
                            .append("username", username)
                            .append("date", date)),
                    new UpdateOptions().upsert(true));
            String taskName = exchange.getHeader("name", String.class);
            System.out.println(taskName);
            int sum = MongoUtil.calcTotalProgress(client, taskName);
            System.out.println(sum);
            MongoUtil.setTotalProgress(client, taskName, sum);
            exchange.setHeader("total_progress", sum);
        } else {
            System.out.println("authentication required");
        }
        return exchange;
    }

    @RequestMapping(value = "/task-info", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    Exchange taskInfo(@RequestBody Exchange exchange) {
        Endpoints.sendTo("login", exchange);
        Claims claims = exchange.getClaims();
        if (claims != null) {
            Document taskInfoDocument = client.getDatabase("heroku_l2hknpxf")
                    .getCollection("tasks").find(Filters.eq("name", exchange.getHeader("name", String.class))).first();
            exchange.setHeader("taskInfo", taskInfoDocument);
            Boolean flag = exchange.getHeader("show_log", Boolean.class);
            if (flag != null && flag) {
                exchange.setHeader("data", MongoUtil.getTaskHistory(client, exchange.getHeader("name", String.class)));
            }
        }
        return exchange;
    }
}
