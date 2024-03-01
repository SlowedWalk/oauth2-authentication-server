package tech.hidetora.springoauthauthorizationserver.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hidetora.springoauthauthorizationserver.entity.OAuthClient;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisteredClientService implements RegisteredClientRepository {
    private final OAuthClientRepository clientRepository;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        OAuthClient client = mapToOAuthClient(registeredClient);
        client.setId(registeredClient.getId());
        clientRepository.save(client);
    }

    @Override
    @Transactional
    public RegisteredClient findById(String clientId) {
        return clientRepository.findById(clientId)
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(this::mapToRegisteredClient)
                .orElse(null);
    }

    private OAuthClient mapToOAuthClient(RegisteredClient registeredClient) {
        OAuthClient client = new OAuthClient();
        client.setClientId(registeredClient.getClientId());
        client.setClientSecret(registeredClient.getClientSecret());
        client.setClientName(registeredClient.getClientName());
        client.setScopes(Set.of(OidcScopes.OPENID));
        client.setClientAuthenticationMethods(Set.of(
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                ClientAuthenticationMethod.CLIENT_SECRET_POST));
        client.setAuthorizationGrantTypes(Set.of(
                AuthorizationGrantType.AUTHORIZATION_CODE,
                AuthorizationGrantType.CLIENT_CREDENTIALS,
                AuthorizationGrantType.REFRESH_TOKEN));
        client.setRedirectUris(registeredClient.getRedirectUris());
        client.setTokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofHours(10))
                .build());
        client.setClientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .requireProofKey(true)
                .build());
        // Map other fields as needed
        return client;
    }

    private RegisteredClient mapToRegisteredClient(OAuthClient client) {
        Set<String> redirectUris = new HashSet<>(); // Replace with your logic to get redirect URIs
        RegisteredClient.Builder builder = RegisteredClient.withId(client.getId())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .scopes(scopes -> scopes.addAll(client.getScopes()))
                .clientAuthenticationMethods(clientAuthenticationMethods -> clientAuthenticationMethods.addAll(client.getClientAuthenticationMethods()))
                .authorizationGrantTypes(authorizationGrantTypes -> authorizationGrantTypes.addAll(client.getAuthorizationGrantTypes()))
                .redirectUris(uris -> uris.addAll(client.getRedirectUris()))
                .tokenSettings(client.getTokenSettings())
                .clientSettings(client.getClientSettings());
        // Map other fields as needed
        return builder.build();
    }
}
