/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cg;

import com.cg.myflow.Endpoints;
import com.cg.myflow.Exchange;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@SpringBootApplication
public class Main {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MongoClient client;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

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

    @RequestMapping(value = "/boo", method = RequestMethod.GET)
    String boo() {
        System.out.println("get");
        return "hello";
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

    @RequestMapping("/db")
    String db(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
            ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            ArrayList<String> output = new ArrayList<>();
            while (rs.next()) {
                output.add("Read from DB: " + rs.getTimestamp("tick"));
            }

            model.put("records", output);
            return "db";
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return "error";
        }
    }

    @Bean
    public DataSource dataSource() throws SQLException {
        if (dbUrl == null || dbUrl.isEmpty()) {
            return new HikariDataSource();
        } else {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            return new HikariDataSource(config);
        }
    }

}
