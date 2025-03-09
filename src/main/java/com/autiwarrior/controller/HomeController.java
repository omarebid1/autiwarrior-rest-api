package com.autiwarrior.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Home Page";
    }
}
