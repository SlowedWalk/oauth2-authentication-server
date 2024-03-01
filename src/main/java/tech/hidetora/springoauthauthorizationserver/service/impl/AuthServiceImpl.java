package tech.hidetora.springoauthauthorizationserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hidetora.springoauthauthorizationserver.dto.RegisterRequest;
import tech.hidetora.springoauthauthorizationserver.dto.UserDTO;
import tech.hidetora.springoauthauthorizationserver.entity.Authority;
import tech.hidetora.springoauthauthorizationserver.entity.User;
import tech.hidetora.springoauthauthorizationserver.exceptions.EmailAlreadyUsedException;
import tech.hidetora.springoauthauthorizationserver.exceptions.UsernameAlreadyUsedException;
import tech.hidetora.springoauthauthorizationserver.repository.AuthRepository;
import tech.hidetora.springoauthauthorizationserver.repository.UserRepository;
import tech.hidetora.springoauthauthorizationserver.service.AuthService;
import tech.hidetora.springoauthauthorizationserver.utils.AuthoritiesConstants;
import tech.hidetora.springoauthauthorizationserver.utils.RandomUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Optional<User> activateUser(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
                .findOneByActivationKey(key)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
//                    userRepository.save(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    /**
     * Register a user.
     *
     * @param userDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserDTO registerUser(RegisterRequest userDTO) {
        userRepository
                .findOneByUsername(userDTO.username().toLowerCase())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                });
        userRepository
                .findOneByEmailIgnoreCase(userDTO.email())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                });
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);

        tech.hidetora.springoauthauthorizationserver.entity.User newUser = tech.hidetora.springoauthauthorizationserver.entity.User.builder()
                .username(userDTO.username())
//                .login("")
                .email(userDTO.email().toLowerCase())
                // new user gets initially a generated password
                .password(passwordEncoder.encode(userDTO.password()))
                // new user is not active
                .activated(false)
                .activationKey(RandomUtil.generateActivationKey())
                .authorities(authorities)
                .build();
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return UserDTO.toDTO(newUser);
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        return null;
    }

    /**
     * Delete a user by id.
     *
     * @param id the id of the user.
     */
    @Override
    public void deleteUser(String id) {

    }

    private boolean removeNonActivatedUser(tech.hidetora.springoauthauthorizationserver.entity.User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndCreatedAtBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting not activated user {}", user.getUsername());
                    userRepository.delete(user);
                });
    }
}
