package com.team1.efep.controllers;

import com.team1.efep.models.request_models.ChangeOrderStatusRequest;
import com.team1.efep.models.request_models.CreateFlowerRequest;
import com.team1.efep.models.response_models.ChangeOrderStatusResponse;
import com.team1.efep.models.response_models.CreateFlowerResponse;
import com.team1.efep.models.response_models.ViewOrderListResponse;
import com.team1.efep.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/flower")
    @Operation(hidden = true)
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
        return sellerService.createFlower(request, session, model);
    }

    @PostMapping("/flower/api")
    public CreateFlowerResponse createFlower(@RequestBody CreateFlowerRequest request, HttpSession session) {
        return sellerService.createFlowerAPI(request);
    }

    @PutMapping("/order-status")
    @Operation(hidden = true)
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model) {
        return sellerService.changeOrderStatus(request, session, model);
    }

    @PutMapping("/order-status/api")
    public ChangeOrderStatusResponse changeOrderStatus(@RequestBody ChangeOrderStatusRequest request) {
        return sellerService.changeOrderStatusAPI(request);
    }

    @GetMapping("/order-list")
    @Operation(hidden = true)
    public String createFlower(HttpSession session, Model model) {
        return sellerService.viewOrderList(session, model);
    }

    @GetMapping("/order-list/api/{id}")
    public ViewOrderListResponse createFlower(@PathVariable int id) {
        return sellerService.viewOrderListAPI(id);
    }
}
