package com.ecommerce.project.repositery;

import com.ecommerce.project.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepositery extends JpaRepository<Address ,Long> {
}
