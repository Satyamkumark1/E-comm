package com.ecommerce.project.service;

import com.ecommerce.project.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
}
