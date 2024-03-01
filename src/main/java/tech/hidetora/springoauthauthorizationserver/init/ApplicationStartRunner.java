package tech.hidetora.springoauthauthorizationserver.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import tech.hidetora.springoauthauthorizationserver.entity.Authority;
import tech.hidetora.springoauthauthorizationserver.entity.AuthorityName;
import tech.hidetora.springoauthauthorizationserver.entity.User;
import tech.hidetora.springoauthauthorizationserver.exceptions.AuthorityNotFoundException;
import tech.hidetora.springoauthauthorizationserver.repository.AuthRepository;
import tech.hidetora.springoauthauthorizationserver.repository.RegisteredClientService;
import tech.hidetora.springoauthauthorizationserver.repository.UserRepository;
import tech.hidetora.springoauthauthorizationserver.utils.AuthoritiesConstants;
import tech.hidetora.springoauthauthorizationserver.utils.Constants;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class ApplicationStartRunner implements CommandLineRunner {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisteredClientService clientService;

    @Override
    public void run(String... args) throws Exception {
        log.info("ApplicationStartRunner.run");

        // Create some authorities
        authRepository.saveAll(Set.of(
                Authority.builder().authority(AuthorityName.valueOf(AuthoritiesConstants.USER)).build(),
                Authority.builder().authority(AuthorityName.valueOf(AuthoritiesConstants.ADMIN)).build(),
                Authority.builder().authority(AuthorityName.valueOf(AuthoritiesConstants.ANONYMOUS)).build(),
                Authority.builder().authority(AuthorityName.valueOf(AuthoritiesConstants.TENANT)).build()
        ));

        // create main client
        clientService.save(
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("edge-service")
                        .clientSecret(passwordEncoder.encode("rocking-secret"))
                        .scope(OidcScopes.OPENID)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                        // allow wildcard in redirect uri
                        .redirectUri(Constants.CLIENT_REDIRECT_URI)
//                        .redirectUri("http://beans.localhost:8000/login/oauth2/code")
//                        .redirectUri("http://admin.localhost:8000/login/oauth2/code")
//                        .redirectUri("http://ducks.localhost:8000/login/oauth2/code")
                        .tokenSettings(TokenSettings.builder()
                                .accessTokenTimeToLive(Duration.ofHours(1))
                                .refreshTokenTimeToLive(Duration.ofHours(10))
                                .build())
                        .clientSettings(ClientSettings.builder()
                                .requireAuthorizationConsent(true)
                                .requireProofKey(true)
                                .build())
                        .build()
        );

        // Create an admin user
         userRepository.save(User.builder()
                 .username("admin")
                 .password(passwordEncoder.encode("admin"))
                 .email("admin@admin.com")
                 .activated(true)
                 .authorities(
                         Set.of(authRepository.findOneByAuthority(AuthorityName.valueOf(AuthoritiesConstants.ADMIN))
                                 .orElseThrow(() -> new AuthorityNotFoundException("Admin authority not found"))
                         )
                 )
                 .build());

        // Create a tenant user
            userRepository.save(User.builder()
                    .username("beans")
                    .password(passwordEncoder.encode("password"))
                    .email("beans@gmail.com")
                    .activated(true)
                    .authorities(
                            Set.of(authRepository.findOneByAuthority(AuthorityName.valueOf(AuthoritiesConstants.TENANT))
                                    .orElseThrow(() -> new AuthorityNotFoundException("Tenant authority not found"))
                            )
                    ).build());

            // Create a tenant user
            userRepository.save(User.builder()
                    .username("dukes")
                    .password(passwordEncoder.encode("password"))
                    .email("dukes@gmail.com")
                    .activated(true)
                    .authorities(
                            Set.of(authRepository.findOneByAuthority(AuthorityName.valueOf(AuthoritiesConstants.TENANT))
                                    .orElseThrow(() -> new AuthorityNotFoundException("Tenant authority not found"))
                            )
                    ).build());

        // add a redirect uri for all users
        RegisteredClient registeredClient = clientService.findByClientId("edge-service");
        RegisteredClient.Builder builder = RegisteredClient.from(Objects.requireNonNull(registeredClient))
                .redirectUris(uri -> uri.addAll(List.of(
                        "http://admin.localhost/login/oauth2/code/admin",
                        "http://beans.localhost/login/oauth2/code/beans",
                        "http://ducks.localhost/login/oauth2/code/ducks",
                        "http://127.0.0.1:80/login/oauth2/code"))
                );
        clientService.save(builder.build());
    }
}
