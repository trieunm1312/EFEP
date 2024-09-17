package com.team1.efep.services;

import com.team1.efep.models.request_models.DeleteCartItemRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.*;

import com.team1.efep.models.request_models.AddToCartRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface BuyerService {
    String sendEmail(ForgotRequest request, Model model);

    ForgotResponse sendEmailAPI(ForgotRequest request);

    String renewPass(RenewPasswordRequest request, Model model);

    RenewPasswordResponse renewPassAPI(RenewPasswordRequest request);

    String viewCart(HttpSession session, Model model);

    ViewCartResponse viewCartAPI(int id);

    String addToCart(AddToCartRequest request, HttpSession session, Model model);

    AddToCartResponse addToCartAPI(AddToCartRequest request);

    String viewFlowerList(HttpSession session, Model model);

    ViewFlowerListResponse viewFlowerListAPI();

    void viewSlideBar(Model model);

    ViewSlideBarResponse viewSlideBarAPI();

    String deleteCartItem(DeleteCartItemRequest request, HttpSession session, Model model);

    DeleteCartItemResponse deleteCartItemAPI(DeleteCartItemRequest request);

    String viewOrderHistory(HttpSession session, Model model);

    ViewOrderHistoryResponse viewOrderHistoryAPI(int accountId);
}
