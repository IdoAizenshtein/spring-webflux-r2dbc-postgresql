package com.cb.routerconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.cb.controller.UserHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.web.bind.annotation.RequestMethod;
import com.cb.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration(proxyBeanMethods = false)
public class RouterConfig {

    @Autowired
    private AuthHandler authHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/user", method = RequestMethod.GET, beanMethod = "findAll", operation = @Operation(operationId = "findAll")),
            @RouterOperation(path = "/user/{userId}", method = RequestMethod.GET, beanMethod = "retrieve", operation = @Operation(operationId = "retrieve", parameters = {@Parameter(in = ParameterIn.PATH, name = "userId")})),
            @RouterOperation(path = "/user/search/", method = RequestMethod.GET, beanMethod = "searchUsers", operation = @Operation(operationId = "searchUsers", parameters = {@Parameter(in = ParameterIn.QUERY, name = "firstName"), @Parameter(in = ParameterIn.QUERY, name = "lastName")})),
            @RouterOperation(path = "/user", method = RequestMethod.POST, beanMethod = "create", operation = @Operation(operationId = "create", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserDto.class))))),
            @RouterOperation(path = "/user/{userId}", method = RequestMethod.PUT, beanMethod = "update", operation = @Operation(operationId = "update", parameters = {@Parameter(in = ParameterIn.PATH, name = "userId")}, requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserDto.class))))),
            @RouterOperation(path = "/user/{userId}", method = RequestMethod.DELETE, beanMethod = "delete", operation = @Operation(operationId = "delete", parameters = {@Parameter(in = ParameterIn.PATH, name = "userId")}))
    })
    public RouterFunction<ServerResponse> usersRoutes(UserHandler handler) {
        return RouterFunctions.route(GET("/user").and(accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(GET("/user/{userId}").and(accept(MediaType.APPLICATION_JSON)), handler::retrieve)
                .andRoute(GET("/user/search/").and(accept(MediaType.APPLICATION_JSON)), handler::searchUsers)
                .andRoute(POST("/user").and(accept(MediaType.APPLICATION_JSON)), handler::create)
                .andRoute(PUT("/user/{userId}").and(accept(MediaType.APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/user/{userId}").and(accept(MediaType.APPLICATION_JSON)), handler::delete);
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/auth/login", method = RequestMethod.POST, beanMethod = "login", operation = @Operation(operationId = "login", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserDto.class))))),
            @RouterOperation(path = "/auth/signUp", method = RequestMethod.POST, beanMethod = "signUp", operation = @Operation(operationId = "signUp", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserDto.class))))),
    })
    public RouterFunction<ServerResponse> authRoute() {
        return RouterFunctions
                .route(POST("/auth/login").and(accept(MediaType.APPLICATION_JSON)), authHandler::login)
                .andRoute(POST("/auth/signup").and(accept(MediaType.APPLICATION_JSON)), authHandler::signUp);
    }
}