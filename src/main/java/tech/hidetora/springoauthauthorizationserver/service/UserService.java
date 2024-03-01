package tech.hidetora.springoauthauthorizationserver.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.hidetora.springoauthauthorizationserver.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(long id);
    List<UserDTO> getAllUsers();

    Page<UserDTO> getAllPublicUsers(Pageable pageable);

    void deleteUser(String username);

    List<String> getAuthorities();
}
