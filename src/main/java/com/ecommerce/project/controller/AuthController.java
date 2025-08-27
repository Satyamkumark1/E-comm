package com.ecommerce.project.controller;

import com.ecommerce.project.jwt.JwtUtils;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositery.RoleRepository;
import com.ecommerce.project.repositery.UserRepository;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SignUpRequest;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.securityService.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private  AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUp) {

        // Check if username already exists
        if (userRepository.existsByUserName(signUp.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signUp.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user object
        User user = new User(
                signUp.getUserName(),
                signUp.getEmail(),
                passwordEncoder.encode(signUp.getPassword())
        );

        // Handle roles
        Set<String> strRoles = signUp.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            // Default role = USER
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error : role user not found"));
            roles.add(userRole);

        } else {
            strRoles.forEach(role -> {
                if (role.toLowerCase().equals("admin")) {
                    Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role ADMIN not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role USER not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



    @PostMapping("/signing")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest){

        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()

                    ));
        } catch (AuthenticationException e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Bad credential");
            map.put("status",false);
            return  new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String > roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),

                roles);

        return ResponseEntity.ok(response);


    }
    @GetMapping("/userName")
    public String userName(Authentication authentication){
        System.out.println("Authentication object: " + authentication);
        if (authentication!= null){
            System.out.println("Authentication name: " + authentication.getName());
            return authentication.getName();
        } else {
            System.out.println("Authentication is null");
            return "null";
        }

    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> userDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String > roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles);

        return ResponseEntity.ok(response);

    }





}
