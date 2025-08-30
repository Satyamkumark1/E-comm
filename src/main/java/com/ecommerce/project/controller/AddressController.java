package com.ecommerce.project.controller;


import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressServices;
import com.ecommerce.project.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private   AddressServices addressServices;

    @Autowired
    private AuthUtils authUtils;

    /**
     * Create a new address for the authenticated user.
     * 
     * IMPORTANT: This endpoint requires authentication.
     * You must include a valid JWT token in the Authorization header:
     * Authorization: Bearer <your-jwt-token>
     * 
     * To get a JWT token, first authenticate via /api/auth/signin
     */
    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtils.loggedInUser();
        AddressDTO savedAddressDTO = addressServices.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }
}
