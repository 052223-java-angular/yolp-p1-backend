package com.revature.yolp.users;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.revature.yolp.roles.Role;
import com.revature.yolp.roles.RoleService;
import com.revature.yolp.utils.custom_exceptions.InvalidAuthException;
import com.revature.yolp.utils.custom_exceptions.UserNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserPrincipal login(String username, String password) {
        logger.debug("Attempting to log in user with username: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(password, user.getPassword())) {
                logger.info("User {} logged in successfully.", username);
                return new UserPrincipal(user.getId(), user.getUsername(), user.getRole().getName());
            }
        }

        logger.warn("Invalid login attempt for username: {}", username);
        throw new InvalidAuthException("Invalid Credentials");
    }

    public User register(String username, String password) {
        Role foundRole = roleService.findByName("USER");
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(username, hashed, foundRole);
        logger.info("New user registered with username: {}", username);
        return userRepository.save(newUser);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    public boolean isUniqueUsername(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    public boolean isSamePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}