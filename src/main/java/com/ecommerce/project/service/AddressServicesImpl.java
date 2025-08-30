package com.ecommerce.project.service;

import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositery.AddressRepositery;
import com.ecommerce.project.utils.AuthUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AddressServicesImpl implements AddressServices {

    @Autowired
    AddressRepositery addressRepositery;


    @Autowired
    private ModelMapper modelMapper;




    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        
        // Set the user for the address
        address.setUser(user);
        
        // Save the address first
        Address savedAddress = addressRepositery.save(address);
        
        // Add to user's addresses list if not already present
        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }
        
        // Check if address is not already in the list
        boolean addressExists = user.getAddresses().stream()
                .anyMatch(addr -> addr.getAddressId().equals(savedAddress.getAddressId()));
        
        if (!addressExists) {
            user.getAddresses().add(savedAddress);
        }
        
        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
