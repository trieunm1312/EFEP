package com.team1.efep.services;

import com.team1.efep.models.request_models.DeleteWishlistItemRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.*;

import com.team1.efep.models.request_models.AddToWishlistRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface BuyerService {
    String sendEmail(ForgotRequest request, Model model);

    ForgotResponse sendEmailAPI(ForgotRequest request);

    String renewPass(RenewPasswordRequest request, Model model);

    RenewPasswordResponse renewPassAPI(RenewPasswordRequest request);

    String viewWishlist(HttpSession session, Model model);

    ViewWishlistResponse viewWishlistAPI(int id);

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
}
