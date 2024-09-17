package com.team1.efep.controllers;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
@Tag(name = "Buyer")
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

    //mac dinh chay song song vs home page ==> ko can controller cho Thymeleaf
    @PostMapping("/flower/top/list/api")
    public ViewFlowerTopListResponse viewFlowerTopList(@RequestBody ViewFlowerTopListRequest request) {
        return buyerService.viewFlowerTopListAPI(request);
    }

    @PostMapping("/flower/search")
    public String searchFlower(SearchFlowerRequest request, Model model){
        return buyerService.searchFlower(request);
    }

    @PostMapping("/flower/search/api")
    public SearchFlowerResponse searchFlower(@RequestBody SearchFlowerRequest request){
        return buyerService.searchFlowerAPI(request);
    }

    @PostMapping("/flower/detail")
    public String viewFlowerDetail(ViewFlowerDetailRequest request, Model model){
        return buyerService.viewFlowerDetail(request, model);
    }

    @PostMapping("/flower/detail/api")
    public ViewFlowerDetailResponse searchFlower(@RequestBody ViewFlowerDetailRequest request){
        return buyerService.viewFlowerDetailAPI(request);
    }

}
