package com.revature.yolp.restaurants;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.yolp.restaurants.dtos.requests.NewRestaurantRequest;
import com.revature.yolp.utils.JwtTokenService;
import com.revature.yolp.utils.custom_exceptions.InvalidJwtException;
import com.revature.yolp.utils.custom_exceptions.InvalidPermissionException;
import com.revature.yolp.utils.custom_exceptions.RestaurantCreationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restoService;
    private final JwtTokenService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createRestaurant(@RequestBody NewRestaurantRequest req, HttpServletRequest sreq) {
        logger.debug("Received request to create restaurant: {}", req.toString());

        String token = sreq.getHeader("auth-token");
        logger.debug("Token: {}", token);

        if (token == null) {
            throw new InvalidJwtException("No token provided");
        }

        String username = jwtService.extractUsername(token);
        logger.debug("Username: {}", username);

        if (!jwtService.validateToken(token, username)) {
            throw new InvalidJwtException("This token is invalid");
        }

        String role = jwtService.extractUserRole(token);
        logger.debug("Role: {}", role);

        if (!role.equals("ADMIN")) {
            logger.warn("User {} attempted to create a restaurant", username);
            throw new InvalidPermissionException("You do not have permission to create a restaurant");
        }

        if (!restoService.isValidAdress(req.getAddress())) {
            logger.warn("User {} attempted to create a restaurant with an invalid address", username);
            throw new RestaurantCreationException("Invalid address");
        }

        if (!restoService.isValidCity(req.getCity())) {
            logger.warn("User {} attempted to create a restaurant with an invalid city", username);
            throw new RestaurantCreationException("Invalid city");
        }

        if (!restoService.isValidState(req.getState())) {
            logger.warn("User {} attempted to create a restaurant with an invalid state", username);
            throw new RestaurantCreationException("Invalid state");
        }

        if (!restoService.isValidZip(req.getZip())) {
            logger.warn("User {} attempted to create a restaurant with an invalid zip", username);
            throw new RestaurantCreationException("Invalid zip");
        }

        restoService.createRestaurant(req);

        logger.info("User {} created a restaurant", username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        logger.debug("Getting all restaurants");
        return ResponseEntity.ok(restoService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable("id") String id) {
        logger.debug("Getting restaurant with id: {}", id);
        return ResponseEntity.ok(restoService.findById(id));
    }

    @ExceptionHandler(RestaurantCreationException.class)
    public ResponseEntity<Map<String, Object>> handleRestaurantCreationException(RestaurantCreationException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJwtException(InvalidJwtException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(InvalidPermissionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidPermissionException(InvalidPermissionException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date(System.currentTimeMillis()));
        body.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}
