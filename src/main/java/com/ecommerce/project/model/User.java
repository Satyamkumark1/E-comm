package com.ecommerce.project.model;

import com.ecommerce.project.helper.Role;
import com.ecommerce.project.helper.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(length = 500)
    private String profilePicUrl;

    @ElementCollection
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "address")
    private List<String> addresses;

    private String country;
    private String state;
    private String city;
    private String zipCode; // better as String
    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String passwordHash;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime lastLogin;
}
