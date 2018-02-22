package com.example.demo.configurations;

import com.mongodb.MongoClient;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    public String mongoHost;

    @Value("${spring.data.mongodb.port}")
    public int mongoPort;


    @Bean
    public MongoTemplate mongoTemplate () {
        return new DefaultMongoTemplate(mongo(), "axon");
    }

    @Bean
    public MongoClient mongo () {
        return new MongoClient(mongoHost, mongoPort);
    }

}
