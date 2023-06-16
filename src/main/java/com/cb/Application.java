package com.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.web.reactive.config.EnableWebFlux;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.r2dbc.spi.ConnectionFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.r2dbc.core.DatabaseClient;

//@EnableWebFlux
//@EnableR2dbcRepositories

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Swagger Demo", version = "1.0", description = "Documentation APIs v1.0"))
@Configuration(proxyBeanMethods = false)
public class Application {
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        var initializer = new ConnectionFactoryInitializer();
//        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ByteArrayResource(("CREATE SEQUENCE primary_key;"
//                + "DROP TABLE IF EXISTS customer;"
//                + "CREATE TABLE customer (id INT PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);")
//                .getBytes())));


//        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
//        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                //.bindMarkers(() -> BindMarkersFactory.named(":", "", 20).create())
                .namedParameters(true)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}