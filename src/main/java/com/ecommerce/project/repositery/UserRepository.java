package com.ecommerce.project.repositery;

import com.ecommerce.project.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {



    boolean existsByEmail(@NotBlank @Size(min = 5,max = 30) String userName);


    Optional<User> findByUserName(String username);

    boolean existsByUserName(@NotBlank @Size(min = 5,max = 30) String userName);
}
