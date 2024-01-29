package tech.hidetora.springoauthauthorizationserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import tech.hidetora.springoauthauthorizationserver.key.KeyManager;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {
//    private final CORSCustomizer corsCustomizer;
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
        http.exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//        corsCustomizer.corsCustomizer(http);
        return http.formLogin(Customizer.withDefaults()).build();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository() {
        var registeredClientRepository = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("main-client")
                .clientSecret("secret")
                .scope(OidcScopes.OPENID)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:3000/login/oauth2/code/main-client-oidc")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(1))
                        .refreshTokenTimeToLive(Duration.ofHours(10))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true)
//                        .requireProofKey(true)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(registeredClientRepository);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        // return default properties
        return AuthorizationServerSettings.builder().issuer("http://localhost:8080").build();
    }
//
//    @Bean
//    OAuth2AuthorizationServerProperties authorizationServerProperties() {
//        // return default properties
//        return new OAuth2AuthorizationServerProperties();
//    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = KeyManager.generateRSAKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) ->
            // return default JWKs
            jwkSelector.select(jwkSet);
    }

//    @Bean
//    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//    }
}

// http://127.0.0.1:3000/login/oauth2/code/main-client-oidc?code=Y7wnojxMa6jgQ1Iw4U1URDxaXykVQTp8rf6QInvIMEvG8zC8emr90rZV-r6JrALXCzcUTBufocQ_r3fEv5sqtUZx8Lg2zi5uXKRLDQWVo_kU-w8gzAd5acKl3xXHVc9p