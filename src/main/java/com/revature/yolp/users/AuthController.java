package com.revature.yolp.users;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.yolp.users.dtos.requests.NewLoginRequest;
import com.revature.yolp.users.dtos.requests.NewUserRequest;
import com.revature.yolp.utils.JwtTokenService;
import com.revature.yolp.utils.custom_exceptions.InvalidAuthException;
import com.revature.yolp.utils.custom_exceptions.ResourceConflictException;
import com.revature.yolp.utils.custom_exceptions.RoleNotFoundException;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody NewLoginRequest req) {
        logger.info("Login attempt for user: {}", req.getUsername());

        UserPrincipal userPrincipal = userService.login(req.getUsername(), req.getPassword());
        String token = jwtService.generateToken(userPrincipal);
        userPrincipal.setToken(token);

        logger.info("User {} successfully logged in.", req.getUsername());
        return ResponseEntity.ok(userPrincipal);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody NewUserRequest req) {
        logger.info("Registration attempt for user: {}", req.getUsername());

        if (!userService.isValidUsername(req.getUsername())) {
            logger.warn("Invalid username: {}", req.getUsername());
            throw new InvalidAuthException(
                    "Username must be at least 8 characters minimum and 20 characters max long and contain only letters and numbers.");
        }

        if (!userService.isUniqueUsername(req.getUsername())) {
            logger.warn("Username already taken: {}", req.getUsername());
            throw new ResourceConflictException("Username is already taken.");
        }

        if (!userService.isValidPassword(req.getPassword())) {
            logger.warn("Invalid password: {}", req.getPassword());
            throw new InvalidAuthException(
                    "Password must be at least 8 characters long and contain at least one letter and one number.");
        }

        if (!userService.isSamePassword(req.getPassword(), req.getConfirmPassword())) {
            logger.warn("Passwords do not match.");
            throw new InvalidAuthException("Passwords do not match.");
        }

        userService.register(req.getUsername(), req.getPassword());

        logger.info("User {} successfully registered.", req.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(InvalidAuthException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAuthException(InvalidAuthException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Map<String, Object>> handleResourceConflictException(ResourceConflictException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotFoundException(RoleNotFoundException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
