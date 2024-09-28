package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

public interface BuyerService {
    String sendEmail(ForgotRequest request, Model model);

    ForgotResponse sendEmailAPI(ForgotRequest request);

    String renewPass(RenewPasswordRequest request, Model model);

    RenewPasswordResponse renewPassAPI(RenewPasswordRequest request);

    String viewWishlist(HttpSession session, Model model);

    ViewWishlistResponse viewWishlistAPI(int accountId);

    String addToWishlist(AddToWishlistRequest request, HttpSession session, Model model);

    AddToWishlistResponse addToWishlistAPI(AddToWishlistRequest request);

    String viewFlowerList(HttpSession session, Model model);

    ViewFlowerListResponse viewFlowerListAPI();

    void viewSlideBar(Model model);

    ViewSlideBarResponse viewSlideBarAPI();

    String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model);

    DeleteWishlistItemResponse deleteWishlistItemAPI(DeleteWishlistItemRequest request);

    String viewOrderHistory(HttpSession session, Model model);

    ViewOrderHistoryResponse viewOrderHistoryAPI(int accountId);

    void viewFlowerTopList(int top, Model model);

    ViewFlowerTopListResponse viewFlowerTopListAPI(int top);

    String searchFlower(SearchFlowerRequest request);

    SearchFlowerResponse searchFlowerAPI(SearchFlowerRequest request);

    String viewFlowerDetail(ViewFlowerDetailRequest request, Model model);

    ViewFlowerDetailResponse viewFlowerDetailAPI(ViewFlowerDetailRequest request);

    String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model);

    ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request);

    String viewOrderStatus(HttpSession session, Model model);

    ViewOrderStatusResponse viewOrderStatusAPI(ViewOrderStatusRequest request);

    String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model);

    UpdateWishlistResponse updateWishlistAPI(UpdateWishlistRequest request);

    String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model);

    DeleteWishlistResponse deleteWishlistAPI(DeleteWishlistRequest request);

    String cancelOrder(CancelOrderRequest request, HttpSession session, Model model);

    CancelOrderResponse cancelOrderAPI(CancelOrderRequest request);

    String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest);

    VNPayResponse createVNPayPaymentLinkAPI(VNPayRequest request, HttpServletRequest httpServletRequest);

    String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session);

    VNPayResponse getPaymentResultAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest);

    String viewCategory(HttpSession session, Model model);

    ViewCategoryListResponse viewCategoryAPI();
}
