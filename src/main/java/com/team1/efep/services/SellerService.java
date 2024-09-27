package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.Map;

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

    String createVNPayPaymentLink(VNPayBusinessPlanRequest request, Model model, HttpServletRequest httpServletRequest);

    VNPayResponse createVNPayPaymentLinkAPI(VNPayBusinessPlanRequest request, HttpServletRequest httpServletRequest);

    String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session);

    VNPayResponse getPaymentResultAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest);

    String sortOrder(FilterOrderRequest filterOrderRequest, SortOrderRequest sortOrderRequest, HttpSession session, Model model);

    SortOrderResponse sortOrderAPI(FilterOrderRequest filterOrderRequest, SortOrderRequest sortOrderRequest);

    String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model);

    UpdateFlowerResponse updateFlowerAPI(UpdateFlowerRequest request);
}
