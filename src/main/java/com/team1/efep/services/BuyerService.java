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

    ForgotPasswordResponse sendEmailAPI(ForgotPasswordRequest request);

    String renewPass(RenewPasswordRequest request, Model model, HttpSession session,  RedirectAttributes redirectAttributes);

    RenewPasswordResponse renewPassAPI(RenewPasswordRequest request);

    String viewWishlist(HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    ViewWishlistResponse viewWishlistAPI(int accountId);

    String addToWishlist(AddToWishlistRequest request,HttpServletRequest httpServletRequest, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    AddToWishlistResponse addToWishlistAPI(AddToWishlistRequest request);

    String viewFlowerList(HttpSession session, Model model);

    ViewFlowerListResponse viewFlowerListAPI();

    void viewSlideBar(Model model);

    ViewSlideBarResponse viewSlideBarAPI();

    String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    DeleteWishlistItemResponse deleteWishlistItemAPI(DeleteWishlistItemRequest request);

    String viewOrderHistory(HttpSession session, Model model);

    ViewOrderHistoryResponse viewOrderHistoryAPI(int accountId);

    void viewSellerTopList(int top, Model model);

    ViewSellerTopListResponse viewSellerTopListAPI(int top);

    String searchFlower(SearchFlowerRequest request, Model model);

    SearchFlowerResponse searchFlowerAPI(SearchFlowerRequest request);

    String viewFlowerDetail(ViewFlowerDetailRequest request, Model model);

    ViewFlowerDetailResponse viewFlowerDetailAPI(ViewFlowerDetailRequest request);

    String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model);

    ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request);

    String viewOrderStatus(HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    ViewOrderStatusResponse viewOrderStatusAPI(ViewOrderStatusRequest request);

    String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    UpdateWishlistResponse updateWishlistAPI(UpdateWishlistRequest request);

    String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    DeleteWishlistResponse deleteWishlistAPI(DeleteWishlistRequest request);

    String cancelOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes);

    CancelOrderResponse cancelOrderAPI(CancelOrderRequest request);

    String confirmOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes);

    CancelOrderResponse confirmOrderAPI(CancelOrderRequest request);

    String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest);

    VNPayResponse createVNPayPaymentLinkAPI(VNPayRequest request, HttpServletRequest httpServletRequest);

    String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session);

    String getCODPaymentResult(Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes);

    VNPayResponse getPaymentResultAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest);

    String createVNPayPaymentLinkForBuyNow(VNPayRequest request, Model model, HttpServletRequest httpServletRequest);

    VNPayResponse createVNPayPaymentLinkForBuyNowAPI(VNPayRequest request, HttpServletRequest httpServletRequest);

    String getPaymentResultForBuyNow(Map<String, String> params, BuyNowCODPayMentRequest request, HttpServletRequest httpServletRequest, Model model, HttpSession session);

//    VNPayResponse getPaymentResultForBuyNowAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest);

    String getCODPaymentResultForBuyNow(VNPayRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    void viewCategory(Model model);

    ViewCategoryListResponse viewCategoryAPI();

    String buyNow(ConfirmOrderRequest request, HttpSession session, Model model);

    String confirmCheckoutOrder(HttpSession session, Model model);

    String handleOTP(String code, Model model, HttpSession session);

    String filterCategory(FilterCategoryRequest request, RedirectAttributes redirectAttributes);

    FilterCategoryResponse filterCategoryAPI(FilterCategoryRequest request);

    String viewFeedback(int sellerId, Model model, HttpSession session);

    ViewFeedbackResponse viewFeedbackAPI(int sellerId);

    String createFeedback(CreateFeedbackRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    CreateFeedbackResponse createFeedbackAPI(CreateFeedbackRequest request);

}
