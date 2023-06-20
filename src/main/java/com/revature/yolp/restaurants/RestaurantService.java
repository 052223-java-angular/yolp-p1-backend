package com.revature.yolp.restaurants;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revature.yolp.restaurants.dtos.requests.NewRestaurantRequest;
import com.revature.yolp.utils.custom_exceptions.RestaurantNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restoRepo;

    public List<Restaurant> getAllRestaurants() {
        return restoRepo.findAll();
    }

    public Restaurant createRestaurant(NewRestaurantRequest req) {
        Restaurant newResto = new Restaurant(req.getName(), req.getAddress(), req.getCity(), req.getState(),
                req.getZip(), req.getImgUrl());

        return restoRepo.save(newResto);
    }

    public Restaurant findById(String id) {
        return restoRepo.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with id " + id + " was not found"));
    }

    public boolean isValidAdress(String address) {
        return address.matches("^[a-zA-Z0-9\\s\\.,#-]*$");
    }

    public boolean isValidCity(String city) {
        return city.matches("^[a-zA-Z\\s\\-]*$");
    }

    public boolean isValidState(String state) {
        return state.matches("^[A-Za-z]{2}$");
    }

    public boolean isValidZip(String zip) {
        return zip.matches("^\\d{5}(?:-\\d{4})?$");
    }

}
