package com.team1.efep.controllers;

import com.team1.efep.configurations.AllPage;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
//@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
@Tag(name = "Buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/pass/forgot")
    @Operation(hidden = true)
    public String forgot(ForgotPasswordRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.sendEmail(request, model, session, redirectAttributes);
    }

    @GetMapping("/otp/verify")
    @Operation(hidden = true)
    public String verifyOTP(@RequestParam(name = "code") String code, Model model, HttpSession session) {
        return buyerService.handleOTP(code, model, session);
    }

    @PostMapping("/pass/renew")
    @Operation(hidden = true)
    public String renewPass(RenewPasswordRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.renewPass(request, model, session, redirectAttributes);
    }

    @GetMapping("/flower")
    @Operation(hidden = true)
    public String viewFlowerList(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewFlowerList(session, model);
    }

    //---------------ORDER----------------//
    @GetMapping("/order/history")
    @Operation(hidden = true)
    public String viewOrderHistory(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderHistory(session, model);
    }

    @GetMapping("/order/detail")
    @Operation(hidden = true)
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderDetail(request, session, model, redirectAttributes);
    }

    @GetMapping("/order/status")
    @Operation(hidden = true)
    public String viewOrderStatus(HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderStatus(session, model, redirectAttributes);
    }

    @PutMapping("/order/confirm")
    @Operation(hidden = true)
    public String confirmOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.confirmOrder(request, session, model, httpServletRequest, redirectAttributes);
    }

    @PutMapping("/order/cancel")
    @Operation(hidden = true)
    public String cancelOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.cancelOrder(request, session, model, httpServletRequest, redirectAttributes);
    }

    @PostMapping("/order/payment")
    @Operation(hidden = true)
    public String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.createVNPayPaymentLink(request, model, httpServletRequest, session);
    }

    @PostMapping("/order/now/payment")
    @Operation(hidden = true)
    public String createVNPayPaymentLinkForBuyNow(VNPayRequest request, Model model, HttpServletRequest httpServletRequest, HttpSession session, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.createVNPayPaymentLinkForBuyNow(request, model, httpServletRequest, session, redirectAttributes);
    }

    @GetMapping("/order/payment/result")
    @Operation(hidden = true)
    public String getPaymentResult(@RequestParam Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.getPaymentResult(params, httpServletRequest, model, session);
    }

    @GetMapping("/order/now/payment/result")
    @Operation(hidden = true)
    public String getPaymentResulForBuyNow(@RequestParam Map<String, String> params, BuyNowCODPayMentRequest request, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.getPaymentResultForBuyNow(params, request, httpServletRequest, model, session);
    }

    @PostMapping("/order/cod")
    public String getCODPaymentResult(@RequestParam Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes,@RequestParam String destination, @RequestParam String phone) {
        return buyerService.getCODPaymentResult(params, session, redirectAttributes, destination, phone);
    }

    @PostMapping("/order/now/cod")
    public String getCODPaymentResultForBuyNow(VNPayRequest request,  HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        return buyerService.getCODPaymentResultForBuyNow(request, session, model, redirectAttributes);
    }

    @GetMapping("/order/confirm")
    public String confirmCheckoutOrder(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.confirmCheckoutOrder(session, model);
    }

    @GetMapping("/order/buyNow")
    public String buyNow(ConfirmOrderRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.buyNow(request, session, model);
    }



    //------------------------------------WISHLIST---------------------------------//
    @GetMapping("/wishlist")
    @Operation(hidden = true)
    public String viewWishlist(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewWishlist(session, model, redirectAttributes);
    }

    @PutMapping("/wishlist")
    @Operation(hidden = true)
    public String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        return buyerService.updateWishlist(request, session, model, redirectAttributes);
    }

    @DeleteMapping("/wishlist")
    @Operation(hidden = true)
    public String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.deleteWishlist(request, session, model, redirectAttributes);
    }

    @DeleteMapping("/wishlist-item")
    @Operation(hidden = true)
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.deleteWishlistItem(request, session, model, redirectAttributes);
    }

    @PostMapping("/wishlist")
    @Operation(hidden = true)
    public String addToWishlist(AddToWishlistRequest request,HttpServletRequest httpServletRequest, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.addToWishlist(request, httpServletRequest, session, model, redirectAttributes);
    }

    //-------------------------------------------------------------------------------//
    @PostMapping("/flower/search")
    @Operation(hidden = true)
    public String searchFlower(SearchFlowerRequest request, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.searchFlower(request, model, session, redirectAttributes);
    }

    @GetMapping("/flower/detail")
    @Operation(hidden = true)
    public String viewFlowerDetail(ViewFlowerDetailRequest request, Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewFlowerDetail(request, model, session);
    }

    @PostMapping("/category/filter")
    @Operation(hidden = true)
    public String filterCategory(FilterCategoryRequest request, RedirectAttributes redirectAttributes, HttpSession session) {
        return buyerService.filterCategory(request, redirectAttributes);
    }

    //---------------FEEDBACK----------------//

    @GetMapping("/feedback")
    @Operation(hidden = true)
    public String viewFeedback(int p, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewFeedback(p, model, session, redirectAttributes);
    }

    @PostMapping("/feedback")
    @Operation(hidden = true)
    public String createFeedback(CreateFeedbackRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        AllPage.allConfig(model, buyerService);
        return buyerService.createFeedback(request, session, model, redirectAttributes, httpServletRequest);
    }

    @PostMapping("/feedback/api")
    public CreateFeedbackResponse createFeedbackAPI(CreateFeedbackRequest request) {
        return buyerService.createFeedback(request);
    }

    //---------------SELLER CHANNEL----------------//

    @GetMapping("/seller/channel")
    @Operation(hidden = true)
    public String viewSellerChannel(HttpSession session, Model model) {
        return buyerService.directToSellerChannel(session, model);
    }

    //---------------SELLER APPLICATION----------------//

    @GetMapping("/seller/application")
    @Operation(hidden = true)
    public String viewSellerApplication(HttpSession session, Model model) {
        return buyerService.createSellerApplication(session, model);
    }

}
