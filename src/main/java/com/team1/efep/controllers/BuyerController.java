package com.team1.efep.controllers;

import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.ViewCartRequest;
import com.team1.efep.models.response_models.AddToCartResponse;
import com.team1.efep.models.response_models.ViewCartResponse;
import com.team1.efep.services.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @GetMapping("/cart")
    @Operation(hidden = true)
    public String viewCart(HttpSession session, Model model) {
        return buyerService.viewCart(session, model);
    }

    @GetMapping("/cart/api/{id}")
    public ViewCartResponse viewCart(@PathVariable int id) {
        return buyerService.viewCartAPI(id);
    }

    @PostMapping("/cart")
    @Operation(hidden = true)
    public String addToCart(HttpSession session, Model model) {
        return buyerService.addToCart(session, model);
    }

    @PostMapping("/cart/api")
    public AddToCartResponse addToCart(@RequestBody AddToCartRequest request) {
        return buyerService.addToCartAPI(request);
    }

}
