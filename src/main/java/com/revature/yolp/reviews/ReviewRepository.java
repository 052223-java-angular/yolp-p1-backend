package com.revature.yolp.reviews;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    void deleteById(String reviewId);

    List<Review> findByRestaurantId(String restaurantId);
}
