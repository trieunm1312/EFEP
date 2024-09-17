package com.team1.efep.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.team1.efep.services.BuyerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final BuyerService buyerService;
    
    @GetMapping("/")
    public String startPage(Model model) {
        buyerService.viewSlideBar(model);
        return "base";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/base")
    public String basePage(Model model) {
        buyerService.viewSlideBar(model);
        return "base";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }


}


