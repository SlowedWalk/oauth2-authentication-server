package tech.hidetora.springoauthauthorizationserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.hidetora.springoauthauthorizationserver.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author hidetora
 * @version 1.0.0
 * @since 2022/04/18
 * @desc Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";
    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    @EntityGraph(attributePaths = "authorities")
//    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByUsername(String login);

//    Optional<AppUser> findOneByLogin(String login);
    Optional<User> findOneByActivationKey(String activationKey);
    Optional<User> findOneByUsername(String login);
    Optional<User> findOneByEmailIgnoreCase(String login);

    @EntityGraph(attributePaths = "authorities")
//    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    List<User> findAllByActivatedIsFalseAndCreatedAtBefore(Instant createdAt);
    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
