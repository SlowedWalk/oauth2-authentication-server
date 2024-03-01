package tech.hidetora.springoauthauthorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.hidetora.springoauthauthorizationserver.entity.OAuthClient;

import java.util.Optional;

public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
    // find by client id
    Optional<OAuthClient> findByClientId(String clientId);
}
