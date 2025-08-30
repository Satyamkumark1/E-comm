package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  addressId;

    @NotBlank(message = "Street is required")
    @Size(min = 5, message = "Street name must be at least 5 characters")
    private  String street;

    @NotBlank(message = "Building name is required")
    @Size(min = 4, message = "Building name must be at least 4 characters")
    private String buildingName;

    @NotBlank(message = "City is required")
    @Size(min = 4, message = "City name must be at least 4 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 4, message = "State name must be at least 4 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 4, message = "Country name must be at least 4 characters")
    private String country;

    @NotBlank(message = "Pincode is required")
    @Size(min = 4, message = "Pincode must be at least 4 characters")
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(Long addressId,
                   String street,
                   String buildingName,
                   String city,
                   String state,
                   String country,
                   String pincode) {
        this.addressId = addressId;
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
