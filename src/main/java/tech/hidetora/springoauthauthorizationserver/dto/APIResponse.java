package tech.hidetora.springoauthauthorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record APIResponse(
        String message,
        Object data,
        boolean success,
        HttpStatus status,
        String error,
        int statusCode,
        String timestamp
) { }
