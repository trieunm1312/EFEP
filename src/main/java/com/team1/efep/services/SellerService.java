package com.team1.efep.services;

import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.ChangeOrderStatusResponse;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderListResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface SellerService {

    String createFlower(CreateFlowerRequest request, HttpSession session, Model model);

    CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request);

    String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model);

    ChangeOrderStatusResponse changeOrderStatusAPI(ChangeOrderStatusRequest request);

    String viewOrderList(HttpSession session, Model model);

    ViewOrderListResponse viewOrderListAPI(int id);

}
