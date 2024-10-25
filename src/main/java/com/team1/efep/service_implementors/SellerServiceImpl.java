package com.team1.efep.service_implementors;

import com.team1.efep.VNPay.BusinessPlanVNPayConfig;
import com.team1.efep.VNPay.VNPayConfig;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.fn.builders.requestbody.Builder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final FlowerRepo flowerRepo;

    private final FlowerImageRepo flowerImageRepo;

    private final AccountRepo accountRepo;

    private final OrderRepo orderRepo;

    private final SellerRepo sellerRepo;

    private final OrderDetailRepo orderDetailRepo;

    private final PurchasedPlanRepo purchasedPlanRepo;

    private final BusinessPlanRepo businessPlanRepo;

    private final BusinessServiceRepo businessServiceRepo;

    private final UserRepo userRepo;
    private final PaymentMethodRepo paymentMethodRepo;


    //--------------------------------------CREATE FLOWER------------------------------------------------//

    @Override
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", CreateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = createFlowerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)) {
            model.addAttribute("msg1", (CreateFlowerResponse) output);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            return "redirect:/manageFlower";
        }
        model.addAttribute("msg1", (Map<String, String>) output);
        return "redirect:/manageFlower";
    }

    @Override
    public CreateFlowerResponse createFlowerAPI(CreateFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return CreateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = createFlowerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)) {
            return (CreateFlowerResponse) output;
        }
        return CreateFlowerResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object createFlowerLogic(CreateFlowerRequest request) {
        Map<String, String> error = CreateFlowerValidation.validateInput(request, flowerRepo);
        if (error.isEmpty()) {
            //success
            Flower flower = createNewFlower(request);
            return CreateFlowerResponse.builder()
                    .status("200")
                    .message("Flower created successfully")
                    .flower(
                            CreateFlowerResponse.FlowerInfo.builder()
                                    .id(flower.getId())
                                    .name(flower.getName())
                                    .price(flower.getPrice())
                                    .description(flower.getDescription())
                                    .flowerAmount(flower.getFlowerAmount())
                                    .quantity(flower.getQuantity())
                                    .soldQuantity(flower.getSoldQuantity())
                                    .imageList(
                                            addFlowerImages(request, flower).stream()
                                                    .map(image -> CreateFlowerResponse.FlowerInfo.Images.builder()
                                                            .link(image.getLink())
                                                            .build())
                                                    .toList()

                                    )
                                    .build()
                    )
                    .build();
        }
        //failed
        return error;
    }


    private Flower createNewFlower(CreateFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        assert account != null;
        Flower flower = Flower.builder()
                .name(request.getName())
                .price(request.getPrice())
                .seller(account.getUser().getSeller())
                .description(request.getDescription())
                .flowerAmount(request.getFlowerAmount())
                .quantity(request.getQuantity())
                .soldQuantity(0)
                .status(Status.FLOWER_STATUS_AVAILABLE)
                .build();


        return flowerRepo.save(flower);
    }


    private List<FlowerImage> addFlowerImages(CreateFlowerRequest request, Flower flower) {
        if (request.getImgList() == null) {
            List<String> imgList = new ArrayList<>();
            imgList.add("/img/noImg.png");
            request.setImgList(imgList);
        }
        List<FlowerImage> flowerImages = request.getImgList().stream()
                .map(link -> FlowerImage.builder()
                        .flower(flower)
                        .link(link)
                        .build())
                .collect(Collectors.toList());
        return flowerImageRepo.saveAll(flowerImages);
    }

    //--------------------------------------GET ALL FLOWER STATUS------------------------------------------------//

    @Override
    public List<String> getAllFlowerStatus() {
        return Status.getFlowerStatusList();
    }

    //---------------------------------------------VIEW ORDER LIST--------------------------------------------------------//

    @Override
    public String viewOrderList(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewOrderListResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderListLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderListResponse.class)) {
            model.addAttribute("msg", (ViewOrderListResponse) output);
            return "viewOrderList";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderList";
    }

    @Override
    public ViewOrderListResponse viewOrderListAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return ViewOrderListResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = viewOrderListLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderListResponse.class)) {
            return (ViewOrderListResponse) output;
        }
        return ViewOrderListResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object viewOrderListLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        List<Order> orderList = getOrdersBySeller(account.getUser().getSeller().getId());
        if (!orderList.isEmpty()) {
            List<ViewOrderListResponse.OrderBill> orderBills = orderList.stream()
                    .map(this::viewOrderList)
                    .collect(Collectors.toList());
            return ViewOrderListResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orderBills)
                    .build();
        }
        return ViewOrderListValidation.orderListValidation();
    }

    private ViewOrderListResponse.OrderBill viewOrderList(Order order) {
        return ViewOrderListResponse.OrderBill.builder()
                .orderId(order.getId())
                .buyerName(order.getBuyerName())
                .createDate(order.getCreatedDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDetailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderListResponse.Item> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderListResponse.Item.builder()
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-----------------------------------CHANGE ORDER STATUS----------------------------------------//

    @Override
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = changeOrderStatusLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            model.addAttribute("msg", (ChangeOrderStatusResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ChangeOrderStatusResponse changeOrderStatusAPI(ChangeOrderStatusRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = changeOrderStatusLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            return (ChangeOrderStatusResponse) output;
        }
        return ChangeOrderStatusResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object changeOrderStatusLogic(ChangeOrderStatusRequest request) {
        Map<String, String> error = ChangeOrderStatusValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, request.getStatus(), orderRepo);
        return ChangeOrderStatusResponse.builder()
                .status("200")
                .message("Change order status successful")
                .order(ChangeOrderStatusResponse.ChangedStatus.builder()
                        .id(order.getId())
                        .status(order.getStatus())
                        .build())
                .build();
    }


    //--------------------------------------VIEW FLOWER LIST FOR SELLER---------------------------------------//

    @Override
    public String viewFlowerListForSeller(HttpSession session, Model model) {
        model.addAttribute("msg", viewFlowerListForSellerLogic(((Account) session.getAttribute("acc")).getUser().getSeller().getId()));
        return "manageFlower";
    }

    @Override
    public ViewFlowerListForSellerResponse viewFlowerListForSellerAPI(int sellerId) {
        return viewFlowerListForSellerLogic(sellerId);
    }

    public ViewFlowerListForSellerResponse viewFlowerListForSellerLogic(int sellerId) {
        List<Flower> flowers = flowerRepo.findBySeller_Id(sellerId);
        return ViewFlowerListForSellerResponse.builder()
                .status("200")
                .message("Number of flower" + flowers.size())
                .flowerList(viewFlowerList(flowers))
                .build();
        // if find -> print size of flower
    }

    private List<ViewFlowerListForSellerResponse.Flower> viewFlowerList(List<Flower> flowers) {
        return flowers.stream()
                .map(item -> ViewFlowerListForSellerResponse.Flower.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .description(item.getDescription())
                        .imageList(viewImageList(item.getFlowerImageList()))
                        .flowerAmount(item.getFlowerAmount())
                        .quantity(item.getQuantity())
                        .soldQuantity(item.getSoldQuantity())
                        .status(item.getStatus())
                        .build())
                .toList();
    }

    private List<ViewFlowerListForSellerResponse.Image> viewImageList(List<FlowerImage> imageList) {
        return imageList.stream()
                .map(img -> ViewFlowerListForSellerResponse.Image.builder()
                        .link(img.getLink())
                        .build())
                .toList();
    }

    //----------------------------------------VIEW BUSINESS PLAN FOR SELLER--------------------------------------------//

    @Override
    public String viewBusinessPlan(HttpSession session, Model model) {
        model.addAttribute("msg", viewBusinessPlanLogic());
        return "planList";
    }

    @Override
    public ViewBusinessPlanResponse viewBusinessPlanAPI() {

        return viewBusinessPlanLogic();
    }

    private ViewBusinessPlanResponse viewBusinessPlanLogic() {

        return ViewBusinessPlanResponse.builder()
                .status("200")
                .message("")
                .serviceList(
                        businessServiceRepo.findAll()
                                .stream()
                                .map(
                                        service -> ViewBusinessPlanResponse.BusinessService.builder()
                                                .id(service.getId())
                                                .name(service.getName())
                                                .description(service.getDescription())
                                                .price(service.getPrice())
                                                .build()
                                )
                                .toList()
                )
                .businessPlanList(
                        businessPlanRepo.findAll()
                                .stream()
                                .map(
                                        plan -> ViewBusinessPlanResponse.BusinessPlan.builder()
                                                .id(plan.getId())
                                                .name(plan.getName())
                                                .description(plan.getDescription())
                                                .price(plan.getPrice())
                                                .duration(plan.getDuration())
                                                .status(plan.getStatus())
                                                .businessServiceList(plan.getPlanServiceList().stream()
                                                        .map(service -> ViewBusinessPlanResponse.BusinessService.builder()
                                                                .id(service.getBusinessService().getId())
                                                                .name(service.getBusinessService().getName())
                                                                .description(service.getBusinessService().getDescription())
                                                                .price(service.getBusinessService().getPrice())
                                                                .build()
                                                        )
                                                        .toList())
                                                .build()
                                )
                                .toList())
                .build();

    }

    //--------------------------------------CANCEL BUSINESS PLAN ---------------------------------------//

    @Override
    public String cancelBusinessPlan(CancelBusinessPlanRequest request, Model model) {
        Object output = cancelBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelBusinessPlanResponse.class)) {
            model.addAttribute("msg", (CancelBusinessPlanResponse) output);
            return "redirect:/seller/plan/detail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "404";
    }

    @Override
    public CancelBusinessPlanResponse cancelBusinessPlanAPI(CancelBusinessPlanRequest request) {
        Object output = cancelBusinessPlanLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelBusinessPlanResponse.class)) {
            return (CancelBusinessPlanResponse) output;
        }
        return CancelBusinessPlanResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object cancelBusinessPlanLogic(CancelBusinessPlanRequest request) {
        Map<String, String> errors = CancelBusinessPlanValidation.validate(request, businessPlanRepo);
        if (errors.isEmpty()) {
            Seller seller = sellerRepo.findById(request.getId()).orElse(null);
            assert seller != null;
            seller.setPlanPurchaseDate(null);
            seller.setBusinessPlan(null);
            sellerRepo.save(seller);
            return DisableBusinessPlanResponse.builder()
                    .status("200")
                    .message("Disabled successfully")
                    .build();
        }
        return errors;
    }

    //-----------------------------------------VIEW BUYER LIST--------------------------------------//

    @Override
    public String viewBuyerList(HttpSession session, Model model) {
        Object output = viewBuyerListLogic(((Account) session.getAttribute("acc")).getUser().getSeller().getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewBuyerListResponse.class)) {
            model.addAttribute("msg", (ViewBuyerListResponse) output);
            return "buyerList";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public ViewBuyerListResponse viewBuyerListAPI(ViewBuyerListRequest request) {
        Object output = viewBuyerListLogic(request.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewBuyerListResponse.class)) {
            return (ViewBuyerListResponse) output;
        }
        return ViewBuyerListResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object viewBuyerListLogic(int sellerId) {
        Map<String, String> errors = ViewBuyerListValidation.validate(sellerId, userRepo);
        if (!errors.isEmpty()) {
            return errors;
        }
        return ViewBuyerListResponse.builder()
                .status("200")
                .message("")
                .buyerList(getBuyerList(sellerId).stream()
                        .map(seller -> ViewBuyerListResponse.Buyer.builder()
                                .id(seller.getId())
                                .name(seller.getName())
                                .build())
                        .toList())
                .build();
    }

    private List<User> getBuyerList(int sellerId) {
        return getOrderList(sellerId).stream()
                .map(Order::getUser)
                .filter(user -> user.getAccount().getRole().equals(Role.BUYER))
                .distinct()
                .toList();
    }

    // :: sai lai ham(thay vi tao moi) (kieu funtional)
    private List<Order> getOrderList(int sellerId) {
        return getOrderDetailList(sellerId).stream()
                .map(OrderDetail::getOrder)
                .toList();
    }

    private List<OrderDetail> getOrderDetailList(int sellerId) {
        return getFlowerList(sellerId).stream()
                .map(Flower::getOrderDetailList)
                .flatMap(List::stream)
                .toList();
        //The syntax is ClassName::methodName
        //In this case, Flower::getOrderDetailList is equivalent to the lambda expression flower -> flower.getOrderDetailList()
    }

    private List<Flower> getFlowerList(int sellerId) {
        Seller seller = sellerRepo.findById(sellerId).orElse(null);
        assert seller != null;
        return seller.getFlowerList();
    }

    //-----------------------------------------SEARCH BUYER LIST--------------------------------------//

    @Override
    public String searchBuyerList(HttpSession session, SearchBuyerListRequest request, Model model) {
        model.addAttribute("msg", searchBuyerListLogic(request, ((Account) session.getAttribute("acc")).getUser().getSeller().getId()));
        return "buyerList";
    }

    @Override
    public SearchBuyerListResponse searchBuyerListAPI(SearchBuyerListRequest request, int sellerId) {
        return searchBuyerListLogic(request, sellerId);
    }

    private SearchBuyerListResponse searchBuyerListLogic(SearchBuyerListRequest request, int sellerId) {
        return SearchBuyerListResponse.builder()
                .status("200")
                .message("")
                .buyerList(getBuyerList(sellerId).stream()
                        .filter(buyer -> buyer.getName().contains(request.getKeyword()))
                        .map(user -> SearchBuyerListResponse.Buyer.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .build())
                        .toList()
                )
                .build();
    }

    //--------------------------------VIEW ORDER DETAIL-----------------------------------//

    @Override
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            model.addAttribute("msg", (ViewOrderDetailResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return ViewOrderDetailResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            return (ViewOrderDetailResponse) output;
        }
        return ViewOrderDetailResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object viewOrderDetailLogic(ViewOrderDetailRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Map<String, String> error = ViewOrderDetailValidation.validate(request, account, order);
        if (!error.isEmpty()) {
            return error;
        }

        List<ViewOrderDetailForSellerResponse.Detail> detailList = viewOrderDetailLists(order.getOrderDetailList());

        return ViewOrderDetailForSellerResponse.builder()
                .status("200")
                .message("Order details retrieved successfully")
                .orderId(order.getId())
                .buyerName(order.getUser().getName())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .detailList(detailList)
                .build();
    }

    private List<ViewOrderDetailForSellerResponse.Detail> viewOrderDetailLists(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderDetailForSellerResponse.Detail.builder()
                        .sellerName(detail.getFlower().getSeller().getUser().getName())
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-----------------------------------------FILTER ORDER--------------------------------------//

    @Override
    public String filterOrder(FilterOrderRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewOrderListResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        Object output = filterOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, FilterOrderResponse.class)) {
            model.addAttribute("msg", (FilterOrderResponse) output);
            return "viewOrderList";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderList";
    }

    @Override
    public FilterOrderResponse filterOrderAPI(FilterOrderRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return FilterOrderResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = filterOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, FilterOrderResponse.class)) {
            return (FilterOrderResponse) output;
        }
        return FilterOrderResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object filterOrderLogic(FilterOrderRequest request) {
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Map<String, String> error = FilterOrderValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }

        List<Order> orders = getOrdersBySeller(account.getUser().getSeller().getId());
        assert !orders.isEmpty();

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase(request.getStatus()))
                    .collect(Collectors.toList());
        }

        if (request.getCreatedDate() != null) {
            orders = orders.stream()
                    .filter(order -> order.getCreatedDate().getMonth().equals(request.getCreatedDate().getMonth()))
                    .collect(Collectors.toList());
        }

        List<FilterOrderResponse.OrderBill> orderBills = orders.stream()
                .map(this::viewFilterOrderList)
                .toList();

        if (!orders.isEmpty()) {
            return FilterOrderResponse.builder()
                    .status("200")
                    .message("Filter successful")
                    .orderList(orderBills)
                    .build();
        }
        return FilterOrderResponse.builder()
                .status("400")
                .message("No order found")
                .build();

    }

    private List<Order> getOrdersBySeller(int sellerId) {
        List<OrderDetail> orderDetails = orderDetailRepo.findAllByFlower_Seller_Id(sellerId);

        return orderDetails.stream()
                .map(OrderDetail::getOrder)
                .distinct()
                .collect(Collectors.toList());
    }

    private FilterOrderResponse.OrderBill viewFilterOrderList(Order order) {
        return FilterOrderResponse.OrderBill.builder()
                .orderId(order.getId())
                .buyerName(order.getBuyerName())
                .createDate(order.getCreatedDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDetailList(viewFilterOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<FilterOrderResponse.Item> viewFilterOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> FilterOrderResponse.Item.builder()
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }


    //-------------------------------------------------VN PAY----------------------------------------//

    @Override
    public String createVNPayPaymentLink(VNPayBusinessPlanRequest request, Model model, HttpServletRequest httpServletRequest) {
        VNPayResponse vnPayResponse = createVNPayPaymentLinkLogic(request, httpServletRequest);
        model.addAttribute("msg", vnPayResponse);
        return "redirect:" + vnPayResponse.getPaymentURL();
    }

    @Override
    public VNPayResponse createVNPayPaymentLinkAPI(VNPayBusinessPlanRequest request, HttpServletRequest httpServletRequest) {
        return createVNPayPaymentLinkLogic(request, httpServletRequest);
    }

    private VNPayResponse createVNPayPaymentLinkLogic(VNPayBusinessPlanRequest request, HttpServletRequest httpServletRequest) {
        Map<String, String> paramList = new HashMap<>();

        long amount = getAmount(request);
        int busPlanId = getBusPlanId(request);
        String txnRef = BusinessPlanVNPayConfig.getRandomNumber(8);
        String ipAddress = BusinessPlanVNPayConfig.getIpAddress(httpServletRequest);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        paramList.put("vnp_Version", getVersion());
        paramList.put("vnp_Command", getCommand());
        paramList.put("vnp_TmnCode", BusinessPlanVNPayConfig.vnp_TmnCode);
//        paramList.put("vnp_BusPlanId", String.valueOf(busPlanId));
        paramList.put("vnp_Amount", String.valueOf(amount));
        paramList.put("vnp_CurrCode", "VND");
        paramList.put("vnp_TxnRef", txnRef);
        paramList.put("vnp_OrderInfo", "Order payment No " + txnRef + ", Amount: " + amount + ", Business Plan ID: " + busPlanId);
        paramList.put("vnp_OrderType", getOrderType());
        paramList.put("vnp_Locale", "vn");
        paramList.put("vnp_ReturnUrl", BusinessPlanVNPayConfig.vnp_ReturnUrl);
        paramList.put("vnp_IpAddr", ipAddress);
        paramList.put("vnp_CreateDate", getCreateDate(calendar, formatter));
        paramList.put("vnp_ExpireDate", getExpiredDate(15, calendar, formatter));

        return VNPayResponse.builder()
                .status("200")
                .message("Create payment link successfully")
                .paymentURL(buildVNPayLink(paramList))
                .build();
    }

    private String getVersion() {
        return "2.1.0";
    }

    private String getCommand() {
        return "pay";
    }

    private String getOrderType() {
        return "other";
    }

    private Long getAmount(VNPayBusinessPlanRequest request) {
        return (long) request.getAmount() * 100;
    }

    private Integer getBusPlanId(VNPayBusinessPlanRequest request) {
        return request.getBusinessPlanId();
    }

    private String getCreateDate(Calendar calendar, SimpleDateFormat dateFormat) {
        return dateFormat.format(calendar.getTime());
    }

    private String getExpiredDate(int minutes, Calendar calendar, SimpleDateFormat dateFormat) {
        calendar.add(Calendar.MINUTE, minutes);
        return dateFormat.format(calendar.getTime());
    }

    private String buildVNPayLink(Map<String, String> paramList) {
        try {
            List<String> fieldNames = new ArrayList<>(paramList.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) paramList.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = BusinessPlanVNPayConfig.hmacSHA512(BusinessPlanVNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            return BusinessPlanVNPayConfig.vnp_PayUrl + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //------------------------GET PAYMENT RESULT-------------------------------------//

    @Override
    public String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        Object output = getPaymentResultLogic(params, account.getId(), httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            model.addAttribute("msg", (VNPayResponse) output);
            return ((VNPayResponse) output).getPaymentURL();
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "paymentFailed";
    }

    @Override
    public VNPayResponse getPaymentResultAPI(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest) {

        Object output = getPaymentResultLogic(params, accountId, httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            return (VNPayResponse) output;
        }
        return VNPayResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object getPaymentResultLogic(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest) {
        User user = Role.getCurrentLoggedAccount(accountId, accountRepo).getUser();
        Map<String, String> error = VNPayValidation.validate(params, httpServletRequest);
        if (!error.isEmpty()) {
            return error;
        }
        String transactionStatus = params.get("vnp_TransactionStatus");
        if ("00".equals(transactionStatus)) {
            String orderInfo = params.get("vnp_OrderInfo");
            int busPlanId = Integer.parseInt(extractBusPlanId(orderInfo));
            BusinessPlan businessPlan = businessPlanRepo.findById(busPlanId).orElse(null);
            savePurchasedPlan(params, user, businessPlan);
            return VNPayResponse.builder()
                    .status("200")
                    .message("Your payment is successfully")
                    .paymentURL("/viewOrderSummary")
                    .build();
        }
        return VNPayResponse.builder()
                .status("400")
                .message("Your payment is failed")
                .paymentURL("/viewOrderSummary")
                .build();
    }

    private void savePurchasedPlan(Map<String, String> params, User user, BusinessPlan businessPlan) {

        float vnpAmount = Float.parseFloat(params.get("vnp_Amount"));

        Seller seller = user.getSeller();
        seller.setBusinessPlan(businessPlan);
        seller.setPlanPurchaseDate(LocalDateTime.now());
        sellerRepo.save(seller);

        purchasedPlanRepo.save(PurchasedPlan.builder()
                .seller(user.getSeller())
                .status(Status.ORDER_STATUS_PROCESSING)
                .name(businessPlan.getName())
                .status(Status.PURCHASED_PLAN_STATUS_PURCHASED)
                .paymentMethod(paymentMethodRepo.findById(1).orElse(null))
                .purchasedDate(LocalDateTime.now())
                .price(vnpAmount / 100)
                .build());
    }

    private String extractBusPlanId(String orderInfo) {
        if (orderInfo != null && orderInfo.contains("Business Plan ID: ")) {
            return orderInfo.split("Business Plan ID: ")[1];
        }
        return null;
    }

    //----------------------------------SORT ORDER BY CREATE DATE-------------------------------------//

    @Override
    public String sortOrder(FilterOrderRequest filterOrderRequest, SortOrderRequest sortOrderRequest, HttpSession session, Model model) {
        model.addAttribute("msg", sortOrderLogic(filterOrderRequest, sortOrderRequest));
        return "viewOrderList";
    }

    @Override
    public SortOrderResponse sortOrderAPI(FilterOrderRequest filterOrderRequest, SortOrderRequest sortOrderRequest) {
        return sortOrderLogic(filterOrderRequest, sortOrderRequest);
    }

    private SortOrderResponse sortOrderLogic(FilterOrderRequest filterRequest, SortOrderRequest sortRequest) {
        FilterOrderResponse response = (FilterOrderResponse) filterOrderLogic(filterRequest);
        List<FilterOrderResponse.OrderBill> orders = response.getOrderList();

        if ("asc".equalsIgnoreCase(sortRequest.getSortDirection())) {
            orders.sort(Comparator.comparing(FilterOrderResponse.OrderBill::getCreateDate));
        } else if ("desc".equalsIgnoreCase(sortRequest.getSortDirection())) {
            orders.sort(Comparator.comparing(FilterOrderResponse.OrderBill::getCreateDate).reversed());
        }

        return SortOrderResponse.builder()
                .status("200")
                .message("Sort order successfully")
                .orderList(orders)
                .build();
    }

    //------------------------------UPDATE FLOWER------------------------------------------//

    @Override
    public String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", CreateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        model.addAttribute("response", updateFlowerLogic(request));
        return "redirect:/manageFlower";
    }

    @Override
    public UpdateFlowerResponse updateFlowerAPI(UpdateFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return UpdateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        Object output = updateFlowerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateFlowerResponse.class)) {
            return (UpdateFlowerResponse) output;
        }
        return UpdateFlowerResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object updateFlowerLogic(UpdateFlowerRequest request) {
        Map<String, String> error = UpdateFlowerValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId())
                .orElseThrow(() -> new RuntimeException("Flower not found with id: " + request.getFlowerId()));

        flower.setName(request.getName());
        flower.setPrice(request.getPrice());
        flower.setDescription(request.getDescription());
        flower.setFlowerAmount(request.getFlowerAmount());
        flower.setQuantity(request.getQuantity());
        flower.setStatus(request.getStatus());

        flowerRepo.save(flower);
        return UpdateFlowerResponse.builder()
                .status("400")
                .message("Update flower successfully")
                .build();

    }

    private List<FlowerImage> updateFlowerImages(UpdateFlowerRequest request, Flower flower) {
        List<FlowerImage> flowerImages = request.getFlowerImageList().stream()
                .map(link -> FlowerImage.builder()
                        .flower(flower)
                        .link(link.getLink())
                        .build())
                .collect(Collectors.toList());
        return flowerImageRepo.saveAll(flowerImages);
    }


    //----------------------------------------DELETE FLOWER--------------------------------------------//

    @Override
    public String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", DeleteFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        model.addAttribute("msg", deleteFlowerLogic(request));
        return "redirect:/manageFlower";
    }

    @Override
    public DeleteFlowerResponse deleteFlowerAPI(DeleteFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return DeleteFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        return deleteFlowerLogic(request);
    }

    private DeleteFlowerResponse deleteFlowerLogic(DeleteFlowerRequest request) {
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        flower.setStatus(Status.FLOWER_STATUS_DELETED);
        flowerRepo.save(flower);
        return DeleteFlowerResponse.builder()
                .status("200")
                .message(flower.getName() + " has been deleted" + "(" + flower.getStatus() + ")")
                .build();
    }


    //----------------------------------------VIEW FLOWER IMAGE----------------------------------------------//

    @Override
    public String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        model.addAttribute("msg", viewFlowerImageLogic(request));
        return "redirect:/seller/view/flower";
    }

    @Override
    public ViewFlowerImageResponse viewFlowerImageAPI(ViewFlowerImageRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return ViewFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        return viewFlowerImageLogic(request);
    }

    private ViewFlowerImageResponse viewFlowerImageLogic(ViewFlowerImageRequest request) {
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        List<ViewFlowerImageResponse.FlowerImageLink> imageLinks = flower.getFlowerImageList().stream()
                .map(image -> ViewFlowerImageResponse.FlowerImageLink.builder()
                        .link(image.getLink())
                        .build())
                .toList();

        return ViewFlowerImageResponse.builder()
                .status("200")
                .message("Flower images retrieved successfully")
                .images(imageLinks)
                .build();
    }

    //------------------------------ADD FLOWER IMAGE---------------------------------//

    @Override
    public String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", AddFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }

        model.addAttribute("msg", (Map<String, String>) addFlowerImageLogic(request));
        return "redirect:/seller/view/flower";
    }

    @Override
    public AddFlowerImageResponse addFlowerImageAPI(AddFlowerImageRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return AddFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        addFlowerImageLogic(request);
        return AddFlowerImageResponse.builder()
                .status("400")
                .message("Please login a seller account to do this action")
                .build();
    }


    private Object addFlowerImageLogic(AddFlowerImageRequest request) {
        Map<String, String> error = AddFlowerImageValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);

        FlowerImage newImage = FlowerImage.builder()
                .flower(flower)
                .link(request.getLink())
                .build();

        assert flower != null;
        flower.getFlowerImageList().add(newImage);
        flowerRepo.save(flower);
        flowerImageRepo.save(newImage);
        return AddFlowerImageResponse.builder()
                .status("200")
                .message("Add image successfully")
                .build();
    }

    //----------------------------------------DELETE FLOWER IMAGE----------------------------------------------//

    public String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", AddFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "login";
        }
        model.addAttribute("msg", (DeleteFlowerImageResponse) deleteFlowerImageLogic(request.getImageId()));
        return "redirect:/seller/view/flower";
    }

    public DeleteFlowerImageResponse deleteFlowerImageAPI(DeleteFlowerImageRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            return DeleteFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }
        return deleteFlowerImageLogic(request.getImageId());
    }

    public DeleteFlowerImageResponse deleteFlowerImageLogic(int imageId) {
        FlowerImage image = flowerImageRepo.findById(imageId).orElse(null);
        if (image == null) {
            return DeleteFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build();
        }

        flowerImageRepo.delete(image);
        return DeleteFlowerImageResponse.builder()
                .status("200")
                .message("Delete image successfully")
                .build();
    }

    //----------------------------------------VIEW FLOWER CATEGORY----------------------------------------------//



    //----------------------------------------CHECK OUT----------------------------------------------//

    @Override
    public String confirmOrder(HttpSession session, Model model, int busPlanId) {
        model.addAttribute("msg", businessPlanRepo.findById(busPlanId).orElse(null));
        return "checkoutBusinessPlan";
    }

//----------------------------------------VIEW BUSINESS PLAN DETAIL----------------------------------------------//

    @Override
    public String viewBusinessPlanDetail(HttpSession session, Model model) {
        Seller seller = Objects.requireNonNull(Role.getCurrentLoggedAccount(session)).getUser().getSeller();
        if (seller.getBusinessPlan() == null) {
            model.addAttribute("nullPlan", "No business plan found for the seller.");
            return "sellerPlan";
        }
        int planId = seller.getBusinessPlan().getId();
        model.addAttribute("msg", viewBusinessPlanDetailLogic(planId));
//        model.addAttribute("msg", viewBusinessPlanDetailLogic(
//                Role.getCurrentLoggedAccount(session).getUser().getSeller().getBusinessPlan().getId()
//        ));

        return "sellerPlan";
    }

    @Override
    public ViewBusinessPlanDetailResponse viewBusinessPlanDetailAPI(ViewBusinessPlanDetailRequest request) {
        return viewBusinessPlanDetailLogic(request.getId());
    }

    private ViewBusinessPlanDetailResponse viewBusinessPlanDetailLogic(int planId) {
        BusinessPlan plan = businessPlanRepo.findById(planId).orElse(null);
        assert plan != null;
        return ViewBusinessPlanDetailResponse.builder()
                .status("200")
                .message("")
                .id(plan.getId())
                .name(plan.getName())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .duration(plan.getDuration())
                .planStatus(plan.getStatus())
                .businessServiceList(plan.getPlanServiceList().stream()
                        .map(planService -> ViewBusinessPlanDetailResponse.BusinessService.builder()
                                .id(planService.getBusinessService().getId())
                                .name(planService.getBusinessService().getName())
                                .price(planService.getBusinessService().getPrice())
                                .description(planService.getBusinessService().getDescription())
                                .build()
                        )
                        .toList())
                .build();
    }
}


