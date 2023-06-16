package com.cb.controller;

import com.cb.dto.UserDto;
import com.cb.model.User;
import com.cb.service.UserService;
import com.cb.service.UserServiceImpl;
import com.cb.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {
    Logger logger = Logger.getLogger(UserHandler.class.getName());

    @Autowired
    private UserServiceImpl userService;

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<UserDto> user = request.bodyToMono(UserDto.class);

        return userService
                .create(user)
                .flatMap(res -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> retrieve(ServerRequest request) {
        logger.info("11122222" + request.pathVariable("userId"));
        UUID id = UUID.fromString(request.pathVariable("userId"));

        return userService
                .retrieve(id)
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("userId"));
        Mono<UserDto> updatedUser = request.bodyToMono(UserDto.class);

//        return updatedUser
//                .flatMap(u -> ServerResponse
//                        .ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(userService.update(id, u), User.class)
//                );
        return userService
                .update(id, updatedUser)
                .flatMap(res -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res)
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        UUID id = UUID.fromString(request.pathVariable("userId"));

        return userService.delete(id)
                .flatMap(u -> ServerResponse.ok().bodyValue(u))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> list(ServerRequest request) {

//        Mono<JwtAuthenticationToken> authentication = request
//                .principal()
//                .cast(JwtAuthenticationToken.class);

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.list(), User.class);
    }

    public Mono<ServerResponse> searchUsers(ServerRequest request) {
//        bodyToFlux

        MultiValueMap<String, String> pathVariables = request.queryParams();
        logger.info("=======" + request.queryParams());
        UserUtils.SetUser user = new UserUtils.SetUser();
        if (pathVariables.containsKey("firstName")) {
            Optional<String> result = request.queryParam("firstName");
            result.ifPresent(user::setFirstName);
        }
        if (pathVariables.containsKey("lastName")) {
            Optional<String> result = request.queryParam("lastName");
            result.ifPresent(user::setLastName);
        }
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.fetchUsers(new User(user.firstName, user.lastName)), User.class);
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAll(), User.class);
    }
    public Mono<ServerResponse> findAllByQuery(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.findAllByQuery(), User.class);
    }

}
