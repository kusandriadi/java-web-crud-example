package com.example.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for User information
 */
@RestController
@RequestMapping("/api")
public class UserController {

    /**
     * Get current authenticated user information
     * Supports both OAuth2 (Google) and form-based login
     */
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> userInfo = new HashMap<>();

        // Check if user logged in via OAuth2 (Google)
        if (oAuth2User != null) {
            userInfo.put("name", oAuth2User.getAttribute("name"));
            userInfo.put("email", oAuth2User.getAttribute("email"));
            userInfo.put("picture", oAuth2User.getAttribute("picture"));
        }
        // Check if user logged in via form login
        else if (userDetails != null) {
            userInfo.put("name", userDetails.getUsername());
            userInfo.put("email", userDetails.getUsername() + "@example.com");
            userInfo.put("picture", "https://ui-avatars.com/api/?name=" + userDetails.getUsername());
        }
        // Fallback to authentication principal
        else if (authentication != null) {
            userInfo.put("name", authentication.getName());
            userInfo.put("email", authentication.getName() + "@example.com");
            userInfo.put("picture", "https://ui-avatars.com/api/?name=" + authentication.getName());
        }

        // Add role information (for both OAuth2 and form login)
        if (authentication != null) {
            // Get roles
            String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
            userInfo.put("roles", roles);

            // Check if admin
            boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            userInfo.put("isAdmin", isAdmin);
        } else {
            userInfo.put("roles", "");
            userInfo.put("isAdmin", false);
        }

        return ResponseEntity.ok(userInfo);
    }
}
