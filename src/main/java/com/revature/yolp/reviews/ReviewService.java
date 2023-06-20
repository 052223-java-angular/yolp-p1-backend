package com.revature.yolp.reviews;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.yolp.restaurants.Restaurant;
import com.revature.yolp.restaurants.RestaurantService;
import com.revature.yolp.reviews.dtos.requests.NewReviewRequest;
import com.revature.yolp.users.User;
import com.revature.yolp.users.UserService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepo;
    private final UserService userService;
    private final RestaurantService restoService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public Review addReview(NewReviewRequest req) {
        User foundUser = userService.findById(req.getUserId());
        Restaurant foundRestaurant = restoService.findById(req.getRestaurantId());
        Review newReview = new Review(req.getRating(), req.getComment(), req.getUsername(), foundUser, foundRestaurant);
        reviewRepo.save(newReview);
        logger.info("New review added: " + newReview.toString());
        return newReview;
    }

    public List<Review> getAllReviews() {
        logger.info("Getting all reviews");
        return reviewRepo.findAll();
    }

    public List<Review> getReviewsByRestaurantId(String restaurantId) {
        return reviewRepo.findByRestaurantId(restaurantId);
    }

    public void deleteReview(String reviewId) {
        reviewRepo.deleteById(reviewId);
    }
}