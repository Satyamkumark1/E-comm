package com.ecommerce.project.security;
import com.ecommerce.project.jwt.AuthEntryPointJwt;
import com.ecommerce.project.jwt.AuthTokenFilter;

import com.ecommerce.project.jwt.JwtUtils;
import com.ecommerce.project.securityService.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {




 private  AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    UserDetailsServiceImpl userDetailsService;       // Injects your service that loads users by username.

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;   // Injects handler that returns 401 JSON when unauthorized.

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){ // Registers your JWT filter as a bean.
        return new AuthTokenFilter();                // Spring will manage its lifecycle & let you add it to the chain.
    }

    @Bean
    public JwtUtils jwtUtils(){
        return  new JwtUtils();
    }

    @Bean
    // NOTE: This should be @Bean in most setups so you can inject AuthenticationManager elsewhere.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Obtain the shared AuthenticationManager from Spring.
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

    // Defining rules and what url can be accessed
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{ // Main security DSL (should also be @Bean).
        http.csrf(csrf -> csrf.disable())              // Disable CSRF (OK for stateless APIs using tokens).
                .exceptionHandling(exception ->        // Configure how exceptions are turned into HTTP responses.
                        exception.authenticationEntryPoint(unauthorizedHandler)) // Use your 401 entry point for auth failures.
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS  // Do not create HTTP sessions; use JWT instead.
                                )
                )
                .authorizeHttpRequests(auth ->         // Authorization rules for endpoints.
                        auth.requestMatchers("/api/auth/**").permitAll()     // Public: login/register/token refresh endpoints.
                                .requestMatchers("/v3/api-docs/**").permitAll() // Public: OpenAPI docs.
                                .requestMatchers("/swagger-ui/**").permitAll()   // Public: Swagger UI.
                                .requestMatchers("/api/admin/**").permitAll()    // ⚠ Currently PUBLIC — likely unintended.
                                .requestMatchers("/api/test/**").permitAll()     // Public: test/demo endpoints.
                                .requestMatchers("/images/**").permitAll()       // Public: static images.
                                .anyRequest().authenticated()                    // Everything else requires a valid JWT.
                );
        http.authenticationProvider(authenticationProvider()); // Plug in the DAO provider (UserDetailsService + BCrypt).
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class   // Run your JWT filter before the username/password filter.
        );
        return http.build();                                  // Build and return the SecurityFilterChain.
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){     // Requests here are ignored by Spring Security entirely.
        return  (web -> web.ignoring().requestMatchers(
                "/configuration/ui",                          // Old swagger UIs/static; bypass security chain completely.
                "swagger-resources/**",                       // ⚠ Missing leading slash (see note below).
                "configuration/security",                     // ⚠ Missing leading slash.
                "/swagger-ui.html",                           // Legacy swagger index.
                "/webjars/**"                                 // Static webjars.
        ));
    }
}
