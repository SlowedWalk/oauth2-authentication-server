package tech.hidetora.springoauthauthorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tech.hidetora.springoauthauthorizationserver.entity.Authority;
import tech.hidetora.springoauthauthorizationserver.entity.AuthorityName;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorityDTO {
    private String authority;
    private String description;

    public static Set<AuthorityDTO> toDTOList(Set<Authority> authorities) {
        List<AuthorityDTO> collect = authorities.stream().map(authority ->
                AuthorityDTO.builder()
                        .authority(authority.getAuthority().name())
                        .build()
        ).collect(Collectors.toList());
        return Set.copyOf(collect);
    }

    public static AuthorityDTO toDTO(Authority authority) {
        return AuthorityDTO.builder()
                .authority(authority.getAuthority().name())
                .build();
    }

    public static Authority toEntity(AuthorityDTO authorityDTO) {
        return Authority.builder()
                .authority(AuthorityName.valueOf(authorityDTO.getAuthority()))
                .build();
    }

    public static Set<Authority> toEntityList(Set<AuthorityDTO> authorityDTOs) {
        List<Authority> collect = authorityDTOs.stream().map(authorityDTO ->
                Authority.builder()
                        .authority(AuthorityName.valueOf(authorityDTO.getAuthority()))
                        .build()
        ).collect(Collectors.toList());
        return Set.copyOf(collect);
    }
}
