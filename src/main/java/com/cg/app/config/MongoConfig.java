package com.cg.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient getMongoClient() {
        return new MongoClient(new MongoClientURI("mongodb://heroku_l2hknpxf:5765bbukrob5pvu932sm2v11bb@ds121483.mlab.com:21483/heroku_l2hknpxf"));
    }
}
