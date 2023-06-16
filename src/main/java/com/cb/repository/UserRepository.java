package com.cb.repository;

import com.cb.model.User;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID>, ReactiveQueryByExampleExecutor<User> {
    Mono<User> findByUsername(String username);
}

