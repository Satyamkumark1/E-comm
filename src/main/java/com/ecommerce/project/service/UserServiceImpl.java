package com.ecommerce.project.service;

import com.ecommerce.project.helper.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositery.UserRepositery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositery userRepositery;

    @Override
    public List<User> getAllUsers() {
        return userRepositery.findAll();
    }

    @Override
    public User createUser(User user) {
        User user2= new User();
        user2.setRole(Role.CUSTOMER);
        user2.setFirstName(user.getFirstName());
        user2.setCity(user.getCity());

         User user1 = userRepositery.save(user2);


        return user1;
    }
}

