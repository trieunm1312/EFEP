package com.team1.efep.controllers;

import com.team1.efep.models.request_models.AddToWishlistRequest;
import com.team1.efep.models.request_models.DeleteWishlistItemRequest;
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
// @RestController
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

    @GetMapping("/wishlist")
    @Operation(hidden = true)
    public String viewWishlist(HttpSession session, Model model) {
        return buyerService.viewWishlist(session, model);
    }

    @GetMapping("/wishlist/api/{id}")
    public ViewWishlistResponse viewWishlist(@PathVariable int id) {
        return buyerService.viewWishlistAPI(id);
    }

    @PostMapping("/wishlist")
    @Operation(hidden = true)
    public String addToWishlist(AddToWishlistRequest request, HttpSession session, Model model) {
        return buyerService.addToWishlist(request, session, model);
    }

    @PostMapping("/wishlist/api")
    public AddToWishlistResponse addToWishlist(@RequestBody AddToWishlistRequest request) {
        return buyerService.addToWishlistAPI(request);
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

    @DeleteMapping("/wishlist-item")
    @Operation(hidden = true)
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model){
        return buyerService.deleteWishlistItem(request, session, model);
    }

    @DeleteMapping("/wishlist-item/api")
    public DeleteWishlistItemResponse deleteWishlistItem(@RequestBody DeleteWishlistItemRequest request){
        return buyerService.deleteWishlistItemAPI(request);
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
