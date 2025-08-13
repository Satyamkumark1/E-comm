package com.ecommerce.project.repositery;

import com.ecommerce.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositery  extends JpaRepository<User ,Long> {
}
