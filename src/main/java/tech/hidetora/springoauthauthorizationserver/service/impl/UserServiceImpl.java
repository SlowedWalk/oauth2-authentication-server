package tech.hidetora.springoauthauthorizationserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hidetora.springoauthauthorizationserver.dto.UserDTO;
import tech.hidetora.springoauthauthorizationserver.entity.User;
import tech.hidetora.springoauthauthorizationserver.exceptions.UserNotFoundException;
import tech.hidetora.springoauthauthorizationserver.repository.AuthRepository;
import tech.hidetora.springoauthauthorizationserver.repository.UserRepository;
import tech.hidetora.springoauthauthorizationserver.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(long id) {
        log.info("Fetching user with id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user with id " + id + " not found"));
        return UserDTO.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDTO::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::toDTO);
    }

    @Override
    public void deleteUser(String username) {
        userRepository
                .findOneByUsername(username)
                .ifPresent(user -> {
                    userRepository.delete(user);
                    log.debug("Deleted User: {}", user);
                });
    }

    @Override
    public List<String> getAuthorities() {
        return authRepository.findAll()
                .stream()
                .map(authority -> authority.getAuthority().name())
                .toList();
    }
}
