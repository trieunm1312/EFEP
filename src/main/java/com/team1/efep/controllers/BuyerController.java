package com.team1.efep.controllers;

import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.AddToCartResponse;
import com.team1.efep.models.response_models.ForgotResponse;
import com.team1.efep.models.response_models.RenewPasswordResponse;
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

    @PostMapping("/pass/forgot")
    public String forgot(@RequestBody ForgotRequest request, Model model) {
        return buyerService.sendEmail(request, model);
    }

    @PostMapping("/pass/forgot/api")
    public ForgotResponse forgot(@RequestBody ForgotRequest request) {
        return buyerService.sendEmailAPI(request);
    }

    @PostMapping("/pass/renew")
    public String renewPass(@RequestBody RenewPasswordRequest request, Model model) {
        return buyerService.renewPass(request, model);
    }

    @PostMapping("/pass/renew/api")
    public RenewPasswordResponse renewPass(@RequestBody RenewPasswordRequest request) {
        return buyerService.renewPassAPI(request);
    }


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
