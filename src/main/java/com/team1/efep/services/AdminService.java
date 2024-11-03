package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AdminService {

    String viewUserList(HttpSession session, Model model);

    ViewUserListResponse viewUserListAPI();

    String banUser(BanUserRequest request, Model model, RedirectAttributes redirectAttributes);

    BanUserResponse banUserAPI(BanUserRequest request);

    String unBanUser(UnBanUserRequest request, Model model,  RedirectAttributes redirectAttributes);

    UnBanUserResponse unBanUserAPI(UnBanUserRequest request);

    String searchUserList(HttpSession session, SearchUserListRequest request, Model model);

    SearchUserListResponse searchUserListAPI(SearchUserListRequest request);

    String createAccountForSeller(CreateAccountForSellerRequest request, Model model,  RedirectAttributes redirectAttributes);

    CreateAccountForSellerResponse createAccountForSellerAPI(CreateAccountForSellerRequest request);

    void getTotalUser(Model model);

    void getTotalSeller(Model model);

    void getTotalBuyer(Model model);

    void getTotalFlower(Model model);

    void getTotalSale(Model model);

    void getTotalRevenue(Model model);

    void getOrdersInMonth(Model model);

    void getTop3SellerInMonth(Model model);
}
