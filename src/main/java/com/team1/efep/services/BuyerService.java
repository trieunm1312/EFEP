package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

public interface BuyerService {
    String sendEmail(ForgotPasswordRequest request, Model model, HttpSession session,  RedirectAttributes redirectAttributes);

    String renewPass(RenewPasswordRequest request, Model model, HttpSession session,  RedirectAttributes redirectAttributes);

    String viewWishlist(HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String addToWishlist(AddToWishlistRequest request,HttpServletRequest httpServletRequest, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String viewFlowerList(HttpSession session, Model model);

    void viewSlideBar(Model model);

    String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String viewOrderHistory(HttpSession session, Model model);

    void viewSellerTopList(int top, Model model);

    String searchFlower(SearchFlowerRequest request, Model model, HttpSession session);

    String viewFlowerDetail(ViewFlowerDetailRequest request, Model model, HttpSession session);

    String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model);

    String viewOrderStatus(HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String cancelOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes);

    String confirmOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes);

    String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest, HttpSession session);

    String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session);

    String getCODPaymentResult(Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes);

    String createVNPayPaymentLinkForBuyNow(VNPayRequest request, Model model, HttpServletRequest httpServletRequest, HttpSession session);

    String getPaymentResultForBuyNow(Map<String, String> params, BuyNowCODPayMentRequest request, HttpServletRequest httpServletRequest, Model model, HttpSession session);

    String getCODPaymentResultForBuyNow(VNPayRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    void viewCategory(Model model, HttpSession session);

    String buyNow(ConfirmOrderRequest request, HttpSession session, Model model);

    String confirmCheckoutOrder(HttpSession session, Model model);

    String handleOTP(String code, Model model, HttpSession session);

    String filterCategory(FilterCategoryRequest request, RedirectAttributes redirectAttributes);

    String viewFeedback(int sellerId, Model model, HttpSession session);

    String createFeedback(CreateFeedbackRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest);

    CreateFeedbackResponse createFeedback(CreateFeedbackRequest request);
}
