package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface SellerService {

    String createFlower(CreateFlowerRequest request, HttpSession session, Model model);

    CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request);

    String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model);

    ChangeOrderStatusResponse changeOrderStatusAPI(ChangeOrderStatusRequest request);

    String viewOrderList(HttpSession session, Model model);

    ViewOrderListResponse viewOrderListAPI(int id);

    String viewFlowerListForSeller(ViewFlowerListForSellerRequest request, HttpSession session, Model model);

    ViewFlowerListForSellerResponse viewFlowerListForSellerAPI(ViewFlowerListForSellerRequest request);

    String cancelBusinessPlan(CancelBusinessPlanRequest request, Model model);

    CancelBusinessPlanResponse cancelBusinessPlanAPI(CancelBusinessPlanRequest request);

    String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model);

    ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request);

    String filterOrder(FilterOrderRequest request, HttpSession session, Model model);

    FilterOrderResponse filterOrderAPI(FilterOrderRequest request);

    String viewBuyerList(HttpSession session, Model model);

    ViewBuyerListResponse viewBuyerListAPI(ViewBuyerListRequest request);

    String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model);

    SearchBuyerListResponse searchBuyerListAPI(SearchBuyerListRequest request, int sellerId);
}
