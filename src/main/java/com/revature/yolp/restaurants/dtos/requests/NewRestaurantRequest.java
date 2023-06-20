package com.revature.yolp.restaurants.dtos.requests;

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
public class NewRestaurantRequest {
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String imgUrl;
}
