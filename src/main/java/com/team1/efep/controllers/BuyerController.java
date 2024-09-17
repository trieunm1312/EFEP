package com.team1.efep.controllers;

import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.DeleteCartItemRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/pass/forgot")
    @Operation(hidden = true)
    public String forgot(ForgotRequest request, Model model) {
        return buyerService.sendEmail(request, model);
    }

    @PostMapping("/pass/forgot/api")
    public ForgotResponse forgot(@RequestBody ForgotRequest request) {
        return buyerService.sendEmailAPI(request);
    }

    @PostMapping("/pass/renew")
    @Operation(hidden = true)
    public String renewPass(RenewPasswordRequest request, Model model) {
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
    public String addToCart(AddToCartRequest request, HttpSession session, Model model) {
        return buyerService.addToCart(request, session, model);
    }

    @PostMapping("/cart/api")
    public AddToCartResponse addToCart(@RequestBody AddToCartRequest request) {
        return buyerService.addToCartAPI(request);
    }

    @GetMapping("/flower")
    @Operation(hidden = true)
    public String viewFlowerList(HttpSession session, Model model) {
        return buyerService.viewFlowerList(session, model);
    }

    @GetMapping("/flower/api")
    public ViewFlowerListResponse viewFlowerList() {
        return buyerService.viewFlowerListAPI();
    }

    @DeleteMapping("/cart-item")
    @Operation(hidden = true)
    public String deleteCartItem(DeleteCartItemRequest request, HttpSession session, Model model){
        return buyerService.deleteCartItem(request, session, model);
    }

    @DeleteMapping("/cart-item/ap")
    public DeleteCartItemResponse deleteCartItem(DeleteCartItemRequest request){
        return buyerService.deleteCartItemAPI(request);
    }

    @GetMapping("/order-history")
    @Operation(hidden = true)
    public String viewOrderHistory(HttpSession session, Model model) {
        return buyerService.viewOrderHistory(session, model);
    }

    @GetMapping("/order-history/api/{accountId}")
    public ViewOrderHistoryResponse viewOrderHistory(@PathVariable int accountId) {
        return buyerService.viewOrderHistoryAPI(accountId);
    }

}
