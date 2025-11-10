package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for serving HTML pages
 */
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
}
