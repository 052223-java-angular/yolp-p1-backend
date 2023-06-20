package com.revature.yolp.reviews;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.yolp.reviews.dtos.requests.NewReviewRequest;
import com.revature.yolp.utils.JwtTokenService;
import com.revature.yolp.utils.custom_exceptions.InvalidJwtException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final JwtTokenService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @PostMapping("/create")
    public ResponseEntity<?> addReview(@RequestBody NewReviewRequest req, HttpServletRequest sreq) {
        String token = sreq.getHeader("auth-token");

        if (token == null || token.isEmpty()) {
            logger.info("No token provided");
            throw new InvalidJwtException("No token provided");
        }

        if (!jwtService.validateToken(token, jwtService.extractUsername(token))) {
            logger.info("Invalid token provided");
            throw new InvalidJwtException("Invalid token provided");
        }

        logger.info("Adding new review: " + req.toString());
        reviewService.addReview(req);
        logger.info("Review added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") String reviewId, HttpServletRequest sreq) {
        logger.info("Deleting review with id: " + reviewId);

        String token = sreq.getHeader("auth-token");

        if (token == null || token.isEmpty()) {
            logger.info("No token provided");
            throw new InvalidJwtException("No token provided");
        }

        if (!jwtService.validateToken(token, jwtService.extractUsername(token))) {
            logger.info("Invalid token provided");
            throw new InvalidJwtException("Invalid token provided");
        }

        reviewService.deleteReview(reviewId);
        logger.info("Review deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        logger.info("Getting all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<List<Review>> getReviewsByRestaurantId(@PathVariable("restaurantId") String restaurantId) {
        logger.info("Getting reviews for restaurant with id: " + restaurantId);
        return ResponseEntity.ok(reviewService.getReviewsByRestaurantId(restaurantId));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJwtException(InvalidJwtException e) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", e.getMessage());
        body.put("timestamp", new Date(System.currentTimeMillis()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
