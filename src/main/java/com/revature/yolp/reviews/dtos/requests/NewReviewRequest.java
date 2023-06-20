package com.revature.yolp.reviews.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewReviewRequest {
    private String id;
    private int rating;
    private String comment;
    private String username;
    private String userId;
    private String restaurantId;
}
