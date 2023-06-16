package com.cb.service;

import com.cb.dto.UserDto;
import com.cb.model.User;
import com.cb.repository.UserRepository;
import com.cb.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;

import org.springframework.r2dbc.core.DatabaseClient;

import java.util.function.BiFunction;
import java.util.logging.Logger;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Service
public class UserServiceImpl implements UserService {
    Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private final DatabaseClient databaseClient;
    public static final BiFunction<Row, RowMetadata, User> MAPPING_FUNCTION = (row, rowMetaData) -> User.builder()
            .id(row.get("id", UUID.class))
            .build();

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }


    @Override
    public Mono<User> create(Mono<UserDto> userDto) {
        return userDto.map(UserUtils::toUser).flatMap(userRepository::save);
    }

    @Override
    public Mono<User> retrieve(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono<User> update(UUID userId, Mono<UserDto> userDto) {
        return userRepository.findById(userId)
                .flatMap(user -> userDto
                        .map(UserUtils::toUser)
                        .doOnNext(u -> u.setId(userId)))
                .flatMap(userRepository::save);
    }

    @Override
    public Mono<Boolean> delete(UUID userId) {
        return userRepository.deleteById(userId).then(Mono.just(true));
    }

    @Override
    public Flux<User> list() {
        return userRepository.findAll();
    }

    @Override
    public Flux<User> fetchUsers(User userDto) {
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withTransformer("country", op -> op.map(c -> countryMap.getOrDefault(c, "UNKNOWN")));
//        return this.repository.findAll(Example.of(customer, matcher));
//        ReactiveQueryByExampleExecutor
//        return this.repository.findAll(Example.of(customer, ExampleMatcher.matching().withIgnoreCase("lastName")));
//        return this.repository.findAll(Example.of(customer, ExampleMatcher.matchingAny().withIgnoreCase()));
//        ExampleMatcher exampleObjectMatcher = ExampleMatcher.matching()
//                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.endsWith());
//        return this.repository.findAll(Example.of(customer, exampleObjectMatcher));
//        return this.repository.findAll(Example.of(customer, ExampleMatcher.matching().withIncludeNullValues()));
//        return this.userRepository.findAll(Example.of(userDto, ExampleMatcher.matchingAny().withIgnoreCase()));

//        return this.userRepository.findAll(Example.of(userDto, ExampleMatcher.matching().withIgnoreCase("lastName")));

//        ExampleMatcher exampleObjectMatcher = ExampleMatcher.matching()
//                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.endsWith());
//        return this.userRepository.findAll(Example.of(userDto, exampleObjectMatcher));
        return this.userRepository.findAll(Example.of(userDto, ExampleMatcher.matchingAny()));
    }

    @Override
    @Query("SELECT * FROM users WHERE lastname = :#{[0]}")
    public Flux<User> findByQueryWithExpression(User userDto) {
        return null;
    }


    @Override
    @Query("select id, firstname, lastname from users c where c.lastname = :lastname")
    public Flux<User> findByLastname(User userDto) {
        return null;
    }

    @Query("SELECT * FROM users")
    public Flux<User> findAllByQuery() {
        return null;
    }

    @Override
    public Flux<User> findAll() {
        return this.databaseClient
                .sql("SELECT * FROM users")
                .filter((statement, executeFunction) -> statement.fetchSize(10).execute())
                .map(MAPPING_FUNCTION)
                .all();
//        String allUsedExpiredAttempts = """
//         select t.id failed_id, c.id disable_id, t.username
//           from user.failed_sign_attempts t
//             join user.disable_sign_attempts c on c.username = t.username
//           where c.is_used = true
//             and :now >= c.expires_at + interval '%d seconds'
//          """;
//
//        // POTENTIAL SQL injection - half-arsed but %d ensures that only Number is allowed
//        client
//                .sql(String.format(allUsedExpiredAttempts, resetInterval.getSeconds()))
//                .bind("now", Instant.now())
//                .fetch()
//                .all()
//                .flatMap(this::deleteFailed)
//                .flatMap(this::deleteDisabled)
//                .as(operator::transactional)
//                .subscribe(v1 -> log.debug("Successfully reset {} user(s)", v1));
    }
}
