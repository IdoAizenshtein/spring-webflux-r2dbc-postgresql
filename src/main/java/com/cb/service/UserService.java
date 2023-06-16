package com.cb.service;

import com.cb.dto.UserDto;
import com.cb.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> create(Mono<UserDto> userDto);

    Mono<User> retrieve(UUID userId);

    Mono<User> update(UUID userId, Mono<UserDto> userDto);

    Mono<Boolean> delete(UUID userId);

    Flux<User> list();

    Flux<User> fetchUsers(User userDto);


    Flux<User> findByQueryWithExpression(User userDto);

    Flux<User> findByLastname(User userDto);

    Flux<User> findAll();
}


