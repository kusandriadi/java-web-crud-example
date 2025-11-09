package com.example.webapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "home.html";
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, Object> user(Authentication authentication,
                                    @AuthenticationPrincipal OAuth2User oAuth2User,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> user = new HashMap<>();

        // Check if user logged in via OAuth2 (Google)
        if (oAuth2User != null) {
            user.put("name", oAuth2User.getAttribute("name"));
            user.put("email", oAuth2User.getAttribute("email"));
            user.put("picture", oAuth2User.getAttribute("picture"));
        }
        // Check if user logged in via form login
        else if (userDetails != null) {
            user.put("name", userDetails.getUsername());
            user.put("email", userDetails.getUsername() + "@example.com");
            user.put("picture", null);
        }
        // Fallback to authentication principal
        else if (authentication != null) {
            user.put("name", authentication.getName());
            user.put("email", authentication.getName() + "@example.com");
            user.put("picture", null);
        }

        return user;
    }
}
