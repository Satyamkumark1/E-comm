package com.ecommerce.project.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String jwt = parseJwt(request);
            logger.debug("Parsed JWT: {}", jwt);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                logger.debug("JWT is valid, extracting username");
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.debug("Extracted username: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("Loaded user details: {}", userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.debug("Created authentication token with roles: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Set authentication in SecurityContext");
            } else {
                logger.debug("JWT is null or invalid. JWT: {}, Valid: {}", jwt, jwt != null ? jwtUtils.validateJwtToken(jwt) : false);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        logger.debug("Request URI: {}", request.getRequestURI());
        logger.debug("Authorization header: {}", request.getHeader("Authorization"));
        return jwt;
    }
}
