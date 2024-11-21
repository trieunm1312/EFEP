package com.team1.efep.services;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

public interface SellerService {
    String updateFlowerCategory(UpdateFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String getFlowerCategory(HttpSession session, Model model, int flowerId,  RedirectAttributes redirectAttributes);

    String removeFlowerCategory(RemoveFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String createFlower(CreateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request);

    String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes);

    String viewOrderList(HttpSession session, Model model);

    String viewFlowerListForSeller(HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String viewBuyerList(HttpSession session, Model model);

    String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model);

    String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String filterOrder(FilterOrderRequest request, HttpSession session, Model model);

    String sortOrder(FilterOrderRequest filterOrderRequest, HttpSession session, Model model);

    String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    UpdateFlowerResponse updateFlowerAPI(UpdateFlowerRequest request);

    List<String> getAllFlowerStatus();

    String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes);

    String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes);

    void getTotalSoldFlowers(Model model, HttpSession session);

    void getSoldQuantityCategory(Model model, HttpSession session);

    void getTotalNumberOfCanceledOrder(Model model, HttpSession session);

    void getTotalNumberOfOrder(Model model, HttpSession session);

    void getRevenue(Model model, HttpSession session);

    void getOrderInDaily(Model model, HttpSession session);

    String viewFeedback(Model model, HttpSession session);

    ViewFeedbackResponse viewFeedbackAPI(int sellerId);
}
