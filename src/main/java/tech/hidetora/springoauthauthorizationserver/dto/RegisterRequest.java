package tech.hidetora.springoauthauthorizationserver.dto;

import lombok.*;

@Builder
public record RegisterRequest(
        String username,
        String email,
        String imageUrl,
        String password
) { }
