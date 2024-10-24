package com.team1.efep.controllers;

import com.team1.efep.configurations.AllPage;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.services.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
//@RestController
@RequiredArgsConstructor
@RequestMapping("/buyer")
@Tag(name = "Buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping("/pass/forgot")
    @Operation(hidden = true)
    public String forgot(ForgotPasswordRequest request, Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.sendEmail(request, model, session);
    }

    @PostMapping("/pass/forgot/api")
    public ForgotPasswordResponse forgot(@RequestBody ForgotPasswordRequest request) {
        return buyerService.sendEmailAPI(request);
    }

    @GetMapping("/otp/verify")
    @Operation(hidden = true)
    public String verifyOTP(@RequestParam(name = "code") String code, Model model, HttpSession session) {
        return buyerService.handleOTP(code, model, session);
    }

    @PostMapping("/pass/renew")
    @Operation(hidden = true)
    public String renewPass(RenewPasswordRequest request, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.renewPass(request, model);
    }

    @PostMapping("/pass/renew/api")
    public RenewPasswordResponse renewPass(@RequestBody RenewPasswordRequest request) {
        return buyerService.renewPassAPI(request);
    }

    @GetMapping("/flower")
    @Operation(hidden = true)
    public String viewFlowerList(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewFlowerList(session, model);
    }

    @GetMapping("/flower/api")
    public ViewFlowerListResponse viewFlowerList() {
        return buyerService.viewFlowerListAPI();
    }

    //---------------ORDER----------------//
    @GetMapping("/order/history")
    @Operation(hidden = true)
    public String viewOrderHistory(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderHistory(session, model);
    }

    @GetMapping("/order/history/api/{accountId}")
    public ViewOrderHistoryResponse viewOrderHistory(@PathVariable int accountId) {
        return buyerService.viewOrderHistoryAPI(accountId);
    }

    @GetMapping("/order/detail")
    @Operation(hidden = true)
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderDetail(request, session, model);
    }

    @PostMapping("/order/detail/api")
    public ViewOrderDetailResponse viewOrderHistory(@RequestBody ViewOrderDetailRequest request) {
        return buyerService.viewOrderDetailAPI(request);
    }

    @GetMapping("/order/status")
    @Operation(hidden = true)
    public String viewOrderStatus(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewOrderStatus(session, model);
    }

    @PostMapping("/order/status/api")
    public ViewOrderStatusResponse viewOrderStatus(@RequestBody ViewOrderStatusRequest request) {
        return buyerService.viewOrderStatusAPI(request);
    }

    @PutMapping("/order")
    @Operation(hidden = true)
    public String cancelOrder(CancelOrderRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.cancelOrder(request, session, model);
    }

    @PutMapping("/order/api")
    public CancelOrderResponse updateWishlist(@RequestBody CancelOrderRequest request) {
        return buyerService.cancelOrderAPI(request);
    }

    @PostMapping("/order/payment")
    @Operation(hidden = true)
    public String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest) {
        AllPage.allConfig(model, buyerService);
        return buyerService.createVNPayPaymentLink(request, model, httpServletRequest);
    }

    @PostMapping("/order/payment/api")
    public VNPayResponse createVNPayPaymentLink(@RequestBody VNPayRequest request, HttpServletRequest httpServletRequest) {
        return buyerService.createVNPayPaymentLinkAPI(request, httpServletRequest);
    }

    @GetMapping("/order/payment/result")
    @Operation(hidden = true)
    public String getPaymentResult(@RequestParam Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        AllPage.allConfig(model, buyerService);
        return buyerService.getPaymentResult(params, httpServletRequest, model, session);
    }

//    @GetMapping("/order/payment/result")
//    public VNPayResponse getPaymentResult(@RequestParam Map<String, String> params,@RequestParam int accountId, HttpServletRequest httpServletRequest){
//        return buyerService.getPaymentResultAPI(params, accountId, httpServletRequest);
//    }

    @PostMapping("/order/cod")
    public String getCODPaymentResult(@RequestParam Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes) {
        return buyerService.getCODPaymentResult(params, session, redirectAttributes);
    }


    //------------------------------------WISHLIST---------------------------------//
    @PutMapping("/wishlist")
    @Operation(hidden = true)
    public String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.updateWishlist(request, session, model);
    }

    @PutMapping("/wishlist/api")
    public UpdateWishlistResponse updateWishlist(@RequestBody UpdateWishlistRequest request) {
        return buyerService.updateWishlistAPI(request);
    }

    @DeleteMapping("/wishlist")
    @Operation(hidden = true)
    public String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.deleteWishlist(request, session, model);
    }

    @DeleteMapping("/wishlist/api")
    public DeleteWishlistResponse deleteWishlist(@RequestBody DeleteWishlistRequest request) {
        return buyerService.deleteWishlistAPI(request);
    }

    @DeleteMapping("/wishlist-item")
    @Operation(hidden = true)
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.deleteWishlistItem(request, session, model);
    }

    @DeleteMapping("/wishlist-item/api")
    public DeleteWishlistItemResponse deleteWishlistItem(@RequestBody DeleteWishlistItemRequest request) {
        return buyerService.deleteWishlistItemAPI(request);
    }

    @GetMapping("/wishlist")
    @Operation(hidden = true)
    public String viewWishlist(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewWishlist(session, model);
    }

    @GetMapping("/wishlist/api/{accountId}")
    public ViewWishlistResponse viewWishlist(@PathVariable int accountId) {
        return buyerService.viewWishlistAPI(accountId);
    }

    @PostMapping("/wishlist")
    @Operation(hidden = true)
    public String addToWishlist(AddToWishlistRequest request,HttpServletRequest httpServletRequest, HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.addToWishlist(request, httpServletRequest, session, model);
    }

    @PostMapping("/wishlist/api")
    public AddToWishlistResponse addToWishlist(@RequestBody AddToWishlistRequest request) {
        return buyerService.addToWishlistAPI(request);
    }
    //-------------------------------------------------------------------------------//

    //mac dinh chay song song vs home page ==> ko can controller cho Thymeleaf
    @PostMapping("/flower/top/list/api")
    public ViewFlowerTopListResponse viewFlowerTopList(@RequestBody ViewFlowerTopListRequest request) {
        return buyerService.viewFlowerTopListAPI(request.getTop());
    }

    @PostMapping("/flower/search")
    @Operation(hidden = true)
    public String searchFlower(SearchFlowerRequest request, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.searchFlower(request, model);
    }

    @PostMapping("/flower/search/api")
    public SearchFlowerResponse searchFlower(@RequestBody SearchFlowerRequest request) {
        return buyerService.searchFlowerAPI(request);
    }

    @GetMapping("/flower/detail")
    @Operation(hidden = true)
    public String viewFlowerDetail(ViewFlowerDetailRequest request, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.viewFlowerDetail(request, model);
    }

    @PostMapping("/flower/detail/api")
    public ViewFlowerDetailResponse searchFlower(@RequestBody ViewFlowerDetailRequest request) {
        return buyerService.viewFlowerDetailAPI(request);
    }

//    @GetMapping("/category")
//    @Operation(hidden = true)
//    public String viewCategory(HttpSession session, Model model) {
//        return buyerService.viewCategory(session, model);
//    }

    @GetMapping("/category/api")
    public ViewCategoryListResponse viewCategory() {
        return buyerService.viewCategoryAPI();
    }

    @GetMapping("/order/confirm")
    public String confirmOrder(HttpSession session, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.confirmOrder(session, model);
    }


    @PostMapping("/category/filter")
    @Operation(hidden = true)
    public String filterCategory(FilterCategoryRequest request, Model model) {
        AllPage.allConfig(model, buyerService);
        return buyerService.filterCategory(request, model);
    }

    @PostMapping("/category/filter/api")
    public FilterCategoryResponse filterCategory(@RequestBody FilterCategoryRequest request) {
        return buyerService.filterCategoryAPI(request);
    }


}
