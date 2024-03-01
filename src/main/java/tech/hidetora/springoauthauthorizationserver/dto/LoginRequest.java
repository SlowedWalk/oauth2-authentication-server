package tech.hidetora.springoauthauthorizationserver.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
public record LoginRequest(
        @NotNull
        @Size(min = 1, max = 50)
        String login,
        @NotNull
        @Size(min = 4, max = 100)
        String password,
        boolean rememberMe
) { }
