package com.team1.efep.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class PageController {
    @GetMapping("/")
    public String startPage() {
        return "base";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/base")
    public String basePage() {
        return "base";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


}
