package com.ecommerce.project.security.response;

import lombok.Data;
import org.springframework.http.ResponseCookie;

import java.util.List;

@Data

public class UserInfoResponse {
    private  Long id;
    private String jwtToken;

    private String username;
    private List<String> roles;



    public UserInfoResponse(Long id, String username, List<String> roles) {
        this.id =id;
        this.username =username;
        this.roles=roles;
    }

    public UserInfoResponse(String username, List<String> roles, String jwtCookie, Long id) {
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtCookie;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

