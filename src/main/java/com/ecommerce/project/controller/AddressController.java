package com.ecommerce.project.controller;


import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.service.AddressServices;
import com.ecommerce.project.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private   AddressServices addressServices;

    @Autowired
    private AuthUtils authUtils;


    @PostMapping("/address")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtils.loggedInUser();
        AddressDTO savedAddressDTO = addressServices.createAddress(addressDTO, user);
        return new ResponseEntity<AddressDTO>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/allAddresses")
    public ResponseEntity<List<AddressDTO>> getAllTheAddress(){
        List<AddressDTO> addressDTOS = addressServices.getAddress();
        return  new ResponseEntity<List<AddressDTO>>(addressDTOS,HttpStatus.FOUND);

    }

    @GetMapping("users/address")
    public ResponseEntity<List<AddressDTO>> getAddressbyUser(){
        User user = authUtils.loggedInUser();
       List<AddressDTO> addressDTOS = addressServices.getAddressbyUser(user);
        return  new ResponseEntity<List<AddressDTO>>(addressDTOS,HttpStatus.FOUND);

    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(  @Valid @PathVariable Long addressId,
                                                    @RequestBody AddressDTO addressDTO
                                                    ){

        AddressDTO addressDTOS = addressServices.updateAddressById(addressId,addressDTO);
        return new ResponseEntity<>(addressDTOS,HttpStatus.OK);
    }


    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
      String status=   addressServices.deleteAddressById(addressId);

        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
