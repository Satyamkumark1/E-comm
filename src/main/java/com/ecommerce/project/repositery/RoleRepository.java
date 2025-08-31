package com.ecommerce.project.repositery;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional <Role> findByRoleName(AppRole appRole);
}
