package com.ecommerce.project.repositery;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RoleRepository extends JpaRepository {
    Optional <Role> findByRoleName(AppRole appRole);
}
