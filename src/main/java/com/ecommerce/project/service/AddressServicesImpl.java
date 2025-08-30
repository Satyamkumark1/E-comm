package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositery.AddressRepositery;
import com.ecommerce.project.repositery.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AddressServicesImpl implements AddressServices {

    @Autowired
    AddressRepositery addressRepositery;


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;


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

    @Override
    public List<AddressDTO> getAddress() {
        List<Address> addresses = addressRepositery.findAll();
        List<AddressDTO> addressDTOS  = addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOS;
    }

    @Override
    public List<AddressDTO> getAddressbyUser(User user) {
        List<Address> userAddresses= user.getAddresses();
        List<AddressDTO> saveAddress = userAddresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());

        return saveAddress;
    }

    @Override
    public AddressDTO updateAddressById( Long addressId, AddressDTO addressDTO) {

        Address addressFromDb = addressRepositery.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("address","addressId",addressId));



        addressFromDb.setCity(addressDTO.getCity());
        addressFromDb.setPincode(addressDTO.getPincode());
        addressFromDb.setCountry(addressDTO.getCountry());
        addressFromDb.setStreet(addressDTO.getStreet());
        addressFromDb.setBuildingName(addressDTO.getBuildingName());
        addressFromDb.setState(addressDTO.getState());

        Address updatedAddress = addressRepositery.save(addressFromDb);
        User user= addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);


        return modelMapper.map(updatedAddress,AddressDTO.class);
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address addressFromDb = addressRepositery.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("address","addressId",addressId));


        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepositery.delete(addressFromDb);
        return " Address Deleted successfully"+ addressId;
    }


}
