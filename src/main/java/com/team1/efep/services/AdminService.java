package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AdminService {

    String viewUserList(HttpSession session, Model model);

    String banUser(BanUserRequest request, Model model, RedirectAttributes redirectAttributes);

    String unBanUser(UnBanUserRequest request, Model model,  RedirectAttributes redirectAttributes);

    String searchUserList(HttpSession session, SearchUserListRequest request, Model model);

    String createAccountForSeller(CreateAccountForSellerRequest request, Model model,  RedirectAttributes redirectAttributes);

    void getTotalUser(Model model);

    void getTotalSeller(Model model);

    void getTotalBuyer(Model model);

    void getTotalFlower(Model model);

    void getTotalSale(Model model);

    void getTotalRevenue(Model model);

    void getOrdersInMonth(Model model);

    void getSellersInMonth(Model model);

    void getTop3SellerInMonth(Model model);

    String viewApplicationList(HttpSession session, Model model);

    String acceptApplication(ApproveApplicationRequest request, Model model, RedirectAttributes redirectAttributes);

    String rejectApplication(RejectApplicationRequest request, Model model, RedirectAttributes redirectAttributes);
}
