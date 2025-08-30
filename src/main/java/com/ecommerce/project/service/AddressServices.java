package com.ecommerce.project.service;


import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;


public interface AddressServices {

    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddress();


    List<AddressDTO> getAddressbyUser(User user);

    AddressDTO updateAddressById( @Valid Long addressId, AddressDTO addressDTO);

    String deleteAddressById(Long addressId);
}
