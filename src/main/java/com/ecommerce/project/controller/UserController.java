package com.ecommerce.project.controller;

import com.ecommerce.project.model.User;
import com.ecommerce.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class UserController {

    @Autowired
    private UserService userService;

    // GET - List all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // POST - Create a new user
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
