package com.ecommerce.project.security;
import com.ecommerce.project.security.jwt.AuthEntryPointJwt;
import com.ecommerce.project.security.jwt.AuthTokenFilter;

import com.ecommerce.project.security.jwt.JwtUtils;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositery.RoleRepository;
import com.ecommerce.project.repositery.UserRepository;
import com.ecommerce.project.securityService.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {



    @Autowired
    private  AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public JwtUtils jwtUtils(){
        return  new JwtUtils();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){ // Configures DAO auth provider.
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService); // Use your users/roles source.
        authenticationProvider.setPasswordEncoder(passwordEncoder());     // Use BCrypt to verify passwords.
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){          // Exposes a PasswordEncoder bean.
        return  new BCryptPasswordEncoder();           // BCrypt (default strength 10) for secure hashing.
    }

    @Bean
    // Defining rules and what url can be accessed
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ // Main security DSL (should also be @Bean).
        http.csrf(csrf -> csrf.disable())              // Disable CSRF (OK for stateless APIs using tokens).
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception ->        // Configure how exceptions are turned into HTTP responses.
                        exception.authenticationEntryPoint(unauthorizedHandler)) // Use your 401 entry point for auth failures.
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS  // Do not create HTTP sessions; use JWT instead.
                                )
                )
                .authorizeHttpRequests(auth ->         // Authorization rules for endpoints.
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/carts/**").permitAll()
                                .requestMatchers("/api/public/categories").permitAll()// Public: login/register/token refresh endpoints.
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()   // Public: Swagger UI.
                                .requestMatchers("/api/admin/**").permitAll()    // ⚠ Currently PUBLIC — likely unintended.
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class
        );
        http.headers(headers ->headers.frameOptions(
                HeadersConfigurer.FrameOptionsConfig::sameOrigin
        ));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                        return roleRepository.save(newUserRole);
                    });

            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role newSellerRole = new Role(AppRole.ROLE_SELLER);
                        return roleRepository.save(newSellerRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newAdminRole = new Role(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newAdminRole);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);


            // Create users if not already present
            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("seller1")) {
                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            // Update roles for existing users
            userRepository.findByUserName("user1").ifPresent(user -> {
                user.setRoles(userRoles);
                userRepository.save(user);
            });

            userRepository.findByUserName("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoles);
                userRepository.save(seller);
            });

            userRepository.findByUserName("admin").ifPresent(admin -> {
                admin.setRoles(adminRoles);
                userRepository.save(admin);
            });
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("http://localhost:*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addExposedHeader("Set-Cookie");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
