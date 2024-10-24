package com.team1.efep.controllers;

import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
@Tag(name = "Seller")
public class SellerController {

    private final SellerService sellerService;

    //--------------------------------------------Flower------------------------------------------------//

    @PutMapping("/flower")
    @Operation(hidden = true)
    public String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model) {
        return sellerService.updateFlower(request, session, model);
    }

    @PutMapping("/flower/api")
    public UpdateFlowerResponse updateFlower(@RequestBody UpdateFlowerRequest request) {
        return sellerService.updateFlowerAPI(request);
    }

    @DeleteMapping("/flower")
    @Operation(hidden = true)
    public String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model) {
        return sellerService.deleteFlower(request, session, model);
    }

    @DeleteMapping("/flower/api")
    public DeleteFlowerResponse deleteFlower(@RequestBody DeleteFlowerRequest request) {
        return sellerService.deleteFlowerAPI(request);
    }

    @GetMapping("/flower/image")
    @Operation(hidden = true)
    public String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model) {
        return sellerService.viewFlowerImage(request, session, model);
    }

    @PostMapping("/flower/image/api")
    public ViewFlowerImageResponse viewFlowerImageAPI(@RequestBody ViewFlowerImageRequest request) {
        return sellerService.viewFlowerImageAPI(request);
    }

    @PostMapping("/flower/image/add")
    @Operation(hidden = true)
    public String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model) {
        return sellerService.addFlowerImage(request, session, model);
    }

    @PostMapping("/flower/image/add/api")
    public AddFlowerImageResponse addFlowerImage(@RequestBody AddFlowerImageRequest request) {
        return sellerService.addFlowerImageAPI(request);
    }

    @DeleteMapping("/flower/image")
    @Operation(hidden = true)
    public String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model) {
        return sellerService.deleteFlowerImage(request, session, model);
    }

    @DeleteMapping("/flower/image/api")
    public DeleteFlowerImageResponse deleteFlowerImage(@RequestBody DeleteFlowerImageRequest request) {
        return sellerService.deleteFlowerImageAPI(request);
    }

    @GetMapping("/flower/status")
    public List<String> getAllFlowerStatus() {
        return sellerService.getAllFlowerStatus();
    }

    @GetMapping("/view/flower/api")
    public ViewFlowerListForSellerResponse viewFlowerListForSeller(int sellerId) {
        return sellerService.viewFlowerListForSellerAPI(sellerId);
    }

    @PostMapping("/flower")
    @Operation(hidden = true)
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
        return sellerService.createFlower(request, session, model);
    }

    @PostMapping("/flower/api")
    public CreateFlowerResponse createFlower(@RequestBody CreateFlowerRequest request, HttpSession session) {
        return sellerService.createFlowerAPI(request);
    }

    //-------------------------------------------------------------------------------------------------//

    //--------------------------------------------Order------------------------------------------------//

    @PutMapping("/order/status")
    @Operation(hidden = true)
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model) {
        return sellerService.changeOrderStatus(request, session, model);
    }

    @PutMapping("/order/status/api")
    public ChangeOrderStatusResponse changeOrderStatus(@RequestBody ChangeOrderStatusRequest request) {
        return sellerService.changeOrderStatusAPI(request);
    }

    @GetMapping("/order/list")
    @Operation(hidden = true)
    public String viewOrderList(HttpSession session, Model model) {
        return sellerService.viewOrderList(session, model);
    }

    @GetMapping("/order/list/api/{id}")
    public ViewOrderListResponse viewOrderListAPI(@PathVariable int id) {
        return sellerService.viewOrderListAPI(id);
    }

    @GetMapping("/view/flower")
    public String viewFlowerListForSeller(HttpSession session, Model model) {
        return sellerService.viewFlowerListForSeller(session, model);
    }


    @PutMapping("/order/detail")
    @Operation(hidden = true)
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        return sellerService.viewOrderDetail(request, session, model);
    }

    @PutMapping("/order/detail/api")
    public ViewOrderDetailResponse viewOrderDetail(@RequestBody ViewOrderDetailRequest request) {
        return sellerService.viewOrderDetailAPI(request);
    }

    @PostMapping("/order/filter")
    @Operation(hidden = true)
    public String filterOrder(FilterOrderRequest request, HttpSession session, Model model) {
        return sellerService.filterOrder(request, session, model);
    }

    @PostMapping("/order/filter/api")
    public FilterOrderResponse filterOrder(@RequestBody FilterOrderRequest request) {
        return sellerService.filterOrderAPI(request);
    }

    @PostMapping("/order/payment")
    @Operation(hidden = true)
    public String createVNPayPaymentLink(VNPayBusinessPlanRequest request, Model model, HttpServletRequest httpServletRequest) {
        return sellerService.createVNPayPaymentLink(request, model, httpServletRequest);
    }

    @PostMapping("/order/payment/api")
    public VNPayResponse createVNPayPaymentLinkAPI(@RequestBody VNPayBusinessPlanRequest request, HttpServletRequest httpServletRequest) {
        return sellerService.createVNPayPaymentLinkAPI(request, httpServletRequest);
    }

    @GetMapping("/order/payment/result")
    @Operation(hidden = true)
    public String getPaymentResult(@RequestParam Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        return sellerService.getPaymentResult(params, httpServletRequest, model, session);
    }

//    @GetMapping("/order/payment/result")
//    public VNPayResponse getPaymentResult(@RequestParam Map<String, String> params, @RequestParam int accountId, HttpServletRequest httpServletRequest) {
//        return sellerService.getPaymentResultAPI(params, accountId, httpServletRequest);
//    }

    @GetMapping("/order/confirm")
    @Operation(hidden = true)
    public String confirmOrder(HttpSession session, Model model,@RequestParam int busPlanId) {
        return sellerService.confirmOrder(session, model, busPlanId);
    }

    @GetMapping("/order/sort")
    @Operation(hidden = true)
    public String sortOrder(FilterOrderRequest filterOrderRequest, SortOrderRequest sortOrderRequest, HttpSession session, Model model) {
        return sellerService.sortOrder(filterOrderRequest, sortOrderRequest, session, model);
    }

    @PostMapping("/order/sort/api")
    public SortOrderResponse sortOrder(@RequestBody FilterOrderRequest filterOrderRequest, @RequestBody SortOrderRequest sortOrderRequest) {
        return sellerService.sortOrderAPI(filterOrderRequest, sortOrderRequest);
    }

    //-------------------------------------------------------------------------------------------------//

    @PutMapping("/plan")
    public String cancelBusinessPlan(CancelBusinessPlanRequest request, Model model) {
        return sellerService.cancelBusinessPlan(request, model);
    }

    @PostMapping("/view/plan")
    @Operation(hidden = true)
    public String viewBusinessPlan(HttpSession session, Model model) {
        return sellerService.viewBusinessPlan(session, model);
    }

    @PostMapping("/view/plan/api")
    public ViewBusinessPlanResponse viewBusinessPlan() {
        return sellerService.viewBusinessPlanAPI();
    }

    @PutMapping("/plan/api")
    public CancelBusinessPlanResponse cancelBusinessPlan(@RequestBody CancelBusinessPlanRequest request) {
        return sellerService.cancelBusinessPlanAPI(request);
    }

    @GetMapping("/buyer/list")
    public String viewBuyerList(HttpSession session, Model model) {
        return sellerService.viewBuyerList(session, model);
    }

    @PostMapping("/buyer/list/api")
    public ViewBuyerListResponse viewBuyerList(@RequestBody ViewBuyerListRequest request) {
        return sellerService.viewBuyerListAPI(request);
    }

    @PostMapping("/search/buyer/")
    public String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model) {
        return sellerService.searchBuyerList(session, request, model);
    }

    @PostMapping("/search/buyer/api")
    public SearchBuyerListResponse searchBuyerList(@RequestBody SearchBuyerListRequest request, @RequestBody int id) {
        return sellerService.searchBuyerListAPI(request, id);
    }

    @GetMapping("/plan/detail")
    @Operation(hidden = true)
    public String viewBusinessPlanDetail(HttpSession session, Model model) {
        return sellerService.viewBusinessPlanDetail(session, model);
    }

    @GetMapping("/plan/detail/api")
    public ViewBusinessPlanDetailResponse viewBusinessPlanDetail(ViewBusinessPlanDetailRequest request) {
        return sellerService.viewBusinessPlanDetailAPI(request);
    }
}
