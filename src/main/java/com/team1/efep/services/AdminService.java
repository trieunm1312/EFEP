package com.team1.efep.services;

import com.team1.efep.models.response_models.ViewBusinessServiceResponse;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface AdminService {

    String createBusinessPlan(CreateBusinessPlanRequest request, Model model);

    CreateBusinessPlanResponse createBusinessPlanAPI(CreateBusinessPlanRequest request);

    String updateBusinessPlan(UpdateBusinessPlanRequest request, Model model);

    UpdateBusinessPlanResponse updateBusinessPlanAPI(UpdateBusinessPlanRequest request);

    String disableBusinessPlan(DisableBusinessPlanRequest request, Model model);

    DisableBusinessPlanResponse disableBusinessPlanAPI(DisableBusinessPlanRequest request);

    String createBusinessService(CreateBusinessServiceRequest request, Model model);

    CreateBusinessServiceResponse createBusinessServiceAPI(CreateBusinessServiceRequest request);

    String updateBusinessService(UpdateBusinessServiceRequest request, Model model);

    UpdateBusinessServiceResponse updateBusinessServiceAPI(UpdateBusinessServiceRequest request);

    String deleteBusinessService(DeleteBusinessServiceRequest request, Model model);

    DeleteBusinessServiceResponse deleteBusinessServiceAPI(DeleteBusinessServiceRequest request);

    String viewUserList(HttpSession session, Model model);

    ViewUserListResponse viewUserListAPI();

    String viewBusinessPlan(HttpSession session, Model model);

    ViewBusinessPlanResponse viewBusinessPlanAPI();

    String viewBusinessService(HttpSession session, Model model);

    ViewBusinessServiceResponse viewBusinessServiceAPI();

    String banUser(BanUserRequest request, Model model);

    BanUserResponse banUserAPI(BanUserRequest request);

    String unBanUser(UnBanUserRequest request, Model model);

    UnBanUserResponse unBanUserAPI(UnBanUserRequest request);

    String searchUserList(HttpSession session, SearchUserListRequest request, Model model);

    SearchUserListResponse searchUserListAPI(SearchUserListRequest request);

    String createAccountForSeller(CreateAccountForSellerRequest request, Model model);

    CreateAccountForSellerResponse createAccountForSellerAPI(CreateAccountForSellerRequest request);
}
