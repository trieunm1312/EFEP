package com.team1.efep.services;

import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.ChangeOrderStatusResponse;
import com.team1.efep.models.request_models.ViewFlowerListForSellerRequest;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderListResponse;
import com.team1.efep.models.response_models.ViewFlowerListForSellerResponse;
import com.team1.efep.models.response_models.ViewOrderHistoryResponse;
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

}
