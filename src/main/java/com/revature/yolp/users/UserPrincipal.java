package com.revature.yolp.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPrincipal {
    private String id;
    private String username;
    private String role;
    private String token;

    public UserPrincipal(String id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
