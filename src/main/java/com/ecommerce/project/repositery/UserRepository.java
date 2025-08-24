package com.ecommerce.project.repositery;

import com.ecommerce.project.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByUserName(@NotBlank @Size(min = 5,max = 30) String userName);

    boolean existsByEmail(@NotBlank @Size(min = 5,max = 30) String userName);
}
