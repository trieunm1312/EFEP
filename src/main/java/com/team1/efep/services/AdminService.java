package com.team1.efep.services;

import com.team1.efep.models.response_models.ViewBusinessServiceResponse;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface AdminService {

    String createBusinessPlan(CreateBusinessPlanRequest request, Model model, RedirectAttributes redirectAttributes);

    CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request);

    String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model, RedirectAttributes redirectAttributes);

    UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request);

    String disableBusinessPlan(DisableBusinessPlanRequest request, Model model, RedirectAttributes redirectAttributes);

    DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request);

    String createBusinessService(CreateBusinessServiceRequest request, Model model,  RedirectAttributes redirectAttributes);

    CreateBusinessServiceResponse createBusinessServiceAPI(CreateBusinessServiceRequest request);

    String updateBusinessService(UpdateBusinessServiceRequest request, Model model,  RedirectAttributes redirectAttributes);

    UpdateBusinessServiceResponse updateBusinessServiceAPI(UpdateBusinessServiceRequest request);

    String deleteBusinessService(DeleteBusinessServiceRequest request, Model model, RedirectAttributes redirectAttributes);

    DeleteBusinessServiceResponse deleteBusinessServiceAPI(DeleteBusinessServiceRequest request);

    String viewUserList(HttpSession session, Model model);

    ViewUserListResponse viewUserListAPI();

    String viewBusinessPlan(HttpSession session, Model model, RedirectAttributes redirectAttributes);

    ViewBusinessPlanResponse viewBusinessPlanAPI();

    String viewBusinessService(HttpSession session, Model model);

    ViewBusinessServiceResponse viewBusinessServiceAPI();

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
