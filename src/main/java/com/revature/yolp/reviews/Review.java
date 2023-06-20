package com.revature.yolp.reviews;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.revature.yolp.restaurants.Restaurant;
import com.revature.yolp.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    private String id;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "username", nullable = false)
    private String username;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Review(int rating, String comment, String username, User user, Restaurant restaurant) {
        this.id = UUID.randomUUID().toString();
        this.rating = rating;
        this.comment = comment;
        this.username = username;
        this.user = user;
        this.restaurant = restaurant;
    }
}
