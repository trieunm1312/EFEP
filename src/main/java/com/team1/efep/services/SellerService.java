package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface SellerService {

    String createFlower(CreateFlowerRequest request, HttpSession session, Model model);

    CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request);

    String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model);

    ChangeOrderStatusResponse changeOrderStatusAPI(ChangeOrderStatusRequest request);

    String viewOrderList(HttpSession session, Model model);

    ViewOrderListResponse viewOrderListAPI(int id);

    String viewFlowerListForSeller(HttpSession session, Model model);

    ViewFlowerListForSellerResponse viewFlowerListForSellerAPI(int sellerId);

    String viewBusinessPlan(HttpSession session, Model model);

    ViewBusinessPlanResponse viewBusinessPlanAPI();

    String cancelBusinessPlan(CancelBusinessPlanRequest request, Model model);

    CancelBusinessPlanResponse cancelBusinessPlanAPI(CancelBusinessPlanRequest request);

    String viewBuyerList(HttpSession session, Model model);

    ViewBuyerListResponse viewBuyerListAPI(ViewBuyerListRequest request);

    String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model);

    SearchBuyerListResponse searchBuyerListAPI(SearchBuyerListRequest request, int sellerId);

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

    List<String> getAllFlowerStatus();

    String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model);

    DeleteFlowerResponse deleteFlowerAPI(DeleteFlowerRequest request);

    String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model);

    ViewFlowerImageResponse viewFlowerImageAPI(ViewFlowerImageRequest request);

    String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model);

    AddFlowerImageResponse addFlowerImageAPI(AddFlowerImageRequest request);

    String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model);

    DeleteFlowerImageResponse deleteFlowerImageAPI(DeleteFlowerImageRequest request);

    String confirmOrder(HttpSession session, Model model, int busPlanId);

    String viewBusinessPlanDetail(HttpSession session, Model model);

    ViewBusinessPlanDetailResponse viewBusinessPlanDetailAPI(ViewBusinessPlanDetailRequest request);
}
