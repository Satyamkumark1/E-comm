package com.ecommerce.project.service;


import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;


public interface AddressServices {

    AddressDTO createAddress(AddressDTO addressDTO, User user);
}
