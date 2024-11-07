package com.team1.efep.service_implementors;

import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.SellerService;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final UserRepo userRepo;

    private final PaymentMethodRepo paymentMethodRepo;

    private final CategoryRepo categoryRepo;

    private final FlowerCategoryRepo flowerCategoryRepo;

    private final JavaMailSenderImpl mailSender;


    //--------------------------------------CREATE FLOWER------------------------------------------------//

    @Override
    public String createFlower(CreateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            MapConfig.buildMapKey(error, "Flower name is required");
            return "redirect:/login";
        }
        Object output = createFlowerLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFlowerResponse.class)) {
            model.addAttribute("msg1", (CreateFlowerResponse) output);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            return "redirect:/manageFlower";
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "redirect:/manageFlower";
    }

    private Object createFlowerLogic(CreateFlowerRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        Map<String, String> error = CreateFlowerValidation.validateInput(request, flowerRepo, account.getUser().getSeller());

        if (error.isEmpty()) {
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
                                    .createDate(LocalDateTime.now())
                                    .imageList(
                                            addFlowerImages(request, flower).stream()
                                                    .map(image -> CreateFlowerResponse.FlowerInfo.Images.builder()
                                                            .link(image.getLink())
                                                            .build())
                                                    .toList()
                                    )
//                                    .flowerCategoryList(
//                                            addFlowerCategories(request, flower).stream()
//                                                    .map(flowerCategory -> FlowerCategory.builder()
//                                                            .category(flowerCategory.getCategory())
//                                                            .build())
//                                                    .toList()
//                                    )
                                    .build()
                    )
                    .build();
        }
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
                .createDate(LocalDateTime.now())
                .quantity(request.getQuantity())
                .soldQuantity(0)
                .status(Status.FLOWER_STATUS_AVAILABLE)
                .build();
        flowerRepo.save(flower);
        addFlowerImages(request, flower);
//        addFlowerCategories(request, flower);
        return flower;
    }


    private List<FlowerImage> addFlowerImages(CreateFlowerRequest request, Flower flower) {
        List<String> imgList = (request.getImageList() == null || request.getImageList().isEmpty())
                ? List.of("/img/noImg.png")
                : request.getImageList();

        List<FlowerImage> flowerImages = imgList.stream()
                .map(link -> FlowerImage.builder()
                        .flower(flower)
                        .link(link)
                        .build())
                .toList();

        // Save and return the list of FlowerImage objects
        return flowerImageRepo.saveAll(flowerImages);
    }

//    private List<FlowerCategory> addFlowerCategories(CreateFlowerRequest request, Flower flower) {
//        List<FlowerCategory> flowerCategories = request.getCategoryIdList().stream()
//                .map(categoryId -> FlowerCategory.builder()
//                        .flower(flower)
//                        .category(categoryRepo.findById(categoryId).orElse(null))
//                        .build())
//                .toList();
//        return flowerCategoryRepo.saveAll(flowerCategories);
//    }

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
            session.removeAttribute("status");
            return "viewOrderList";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderList";
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
                .image(order.getOrderDetailList().stream()
                        .map(detail -> detail.getFlower().getFlowerImageList().get(0).getLink())
                        .toList())
                .buyerName(order.getBuyerName())
                .createDate(order.getCreatedDate().toLocalDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDetailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderListResponse.Item> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderListResponse.Item.builder()
                        .image(detail.getFlower().getFlowerImageList().get(0).getLink())
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-----------------------------------CHANGE ORDER STATUS----------------------------------------//

    @Override
    public String changeOrderStatus(ChangeOrderStatusRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        Object output = changeOrderStatusLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            model.addAttribute("msg", (ChangeOrderStatusResponse) output);
            return "redirect:" + referer;
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "redirect:/seller/order/detail";
    }

    private Object changeOrderStatusLogic(ChangeOrderStatusRequest request) {
        Map<String, String> error = ChangeOrderStatusValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, request.getStatus(), orderRepo);
        if (request.getStatus().equals(Status.ORDER_STATUS_PACKED)) {
            sendPackedOrderEmail(order, order.getUser());
        } else {
            sendCancelOrderEmail(order, order.getUser());
        }
        return ChangeOrderStatusResponse.builder()
                .status("200")
                .message("Change order status successful")
                .build();
    }


    private void sendPackedOrderEmail(Order order, User user) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vannhuquynhp@gmail.com");

            helper.setTo(user.getAccount().getEmail());

            helper.setSubject(Const.EMAIL_SUBJECT_ORDER);

            String emailContent = FileReaderUtil.readFile2(order);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCancelOrderEmail(Order order, User user) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vannhuquynhp@gmail.com");

            helper.setTo(user.getAccount().getEmail());

            helper.setSubject(Const.EMAIL_SUBJECT_ORDER);

            String emailContent = FileReaderUtil.readFile3(order);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------------VIEW FLOWER LIST FOR SELLER---------------------------------------//

    @Override
    public String viewFlowerListForSeller(HttpSession session, Model model) {
        model.addAttribute("msg", viewFlowerListForSellerLogic(Role.getCurrentLoggedAccount(session).getUser().getSeller().getId()));
        return "manageFlower";
    }

    public ViewFlowerListForSellerResponse viewFlowerListForSellerLogic(int sellerId) {
        List<Flower> flowers = flowerRepo.findAllBySeller_Id(sellerId);

        //Sắp xếp hoa mới nhất lên đầu
        // có thể sắp xếp theo id, ID của đối tượng mới thường lớn hơn
        flowers.sort(Comparator.comparing(Flower::getId).reversed());
        return ViewFlowerListForSellerResponse.builder()
                .status("200")
                .allCategory(
                        categoryRepo.findAll().stream()
                                .map(cat -> ViewFlowerListForSellerResponse.AllCategoryDetail.builder()
                                        .id(cat.getId())
                                        .name(cat.getName())
                                        .build())
                                .toList()
                )
                .flowerList(viewFlowerList(flowers))
                .build();
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
                        .status(item.getStatus())
                        .categoryList(viewCategoryList(item.getFlowerCategoryList()))
                        .categoryIdList(item.getFlowerCategoryList().stream()
                                .map(cat -> cat.getCategory().getId())
                                .toList())
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

    private List<ViewFlowerListForSellerResponse.CategoryDetail> viewCategoryList(List<FlowerCategory> categoryList) {
        return categoryList.stream()
                .map(cat -> ViewFlowerListForSellerResponse.CategoryDetail.builder()
                        .id(cat.getCategory().getId())
                        .name(cat.getCategory().getName())
                        .build())
                .toList();
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

    private Object viewBuyerListLogic(int sellerId) {
        Map<String, String> error = ViewBuyerListValidation.validate(sellerId, userRepo);
        if (!error.isEmpty()) {
            return error;
        }
        return ViewBuyerListResponse.builder()
                .status("200")
                .message("")
                .buyerList(getBuyerList(sellerId).stream()
                        .map(seller -> ViewBuyerListResponse.Buyer.builder()
                                .id(seller.getId())
                                .name(seller.getName())
                                .email(seller.getAccount().getEmail())
                                .phone(seller.getPhone())
                                .avatar(seller.getAvatar())
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
            model.addAttribute("error", ViewOrderDetailForSellerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailForSellerResponse.class)) {
            model.addAttribute("msg", (ViewOrderDetailForSellerResponse) output);
            return "viewOrderDetail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderDetail";
    }

    private Object viewOrderDetailLogic(ViewOrderDetailRequest request) {
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Map<String, String> error = ViewOrderDetailValidation.validate(request, order);
        if (!error.isEmpty()) {
            return error;
        }

        List<ViewOrderDetailForSellerResponse.Detail> detailList = viewOrderDetailLists(order.getOrderDetailList());

        String sellerName = order.getOrderDetailList().stream()
                .findFirst()
                .map(detail -> detail.getFlower().getSeller().getUser().getName())
                .orElse("Unknown Seller");

        return ViewOrderDetailForSellerResponse.builder()
                .status("200")
                .message("Order details retrieved successfully")
                .orderId(order.getId())
                .sellerName(sellerName)
                .buyerName(order.getUser().getName())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .createdDate(order.getCreatedDate().toLocalDate())
                .detailList(detailList)
                .build();
    }

    private List<ViewOrderDetailForSellerResponse.Detail> viewOrderDetailLists(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> {
                    List<String> categories = detail.getFlower().getFlowerCategoryList().stream()
                            .map(flowerCategory -> flowerCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return ViewOrderDetailForSellerResponse.Detail.builder()
                            .image(detail.getFlower().getFlowerImageList().stream()
                                    .findFirst()
                                    .map(FlowerImage::getLink)
                                    .orElse("Unknown Image"))
                            .description(detail.getFlower().getDescription())
                            .categories(categories)
                            .flowerName(detail.getFlowerName())
                            .quantity(detail.getQuantity())
                            .price(detail.getPrice())
                            .build();
                })
                .toList();
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
            session.setAttribute("status", request.getStatus());
            return "viewOrderList";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderList";
    }

    private Object filterOrderLogic(FilterOrderRequest request) {
        Seller seller = sellerRepo.findById(request.getSellerId()).orElse(null);
        Map<String, String> error = FilterOrderValidation.validate(request);
        if (!error.isEmpty()) {
            return error;
        }
        assert seller != null;
        List<Order> orders = getOrdersBySeller(seller.getId());

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            orders = orders.stream()
                    .filter(order -> order.getStatus().equalsIgnoreCase(request.getStatus()))
                    .toList();
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
                .createDate(order.getCreatedDate().toLocalDate())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .orderDetailList(viewFilterOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<FilterOrderResponse.Item> viewFilterOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> FilterOrderResponse.Item.builder()
                        .image(detail.getFlower().getFlowerImageList().get(0).getLink())
                        .description(detail.getFlower().getDescription())
                        .name(detail.getFlower().getName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //----------------------------------SORT ORDER BY CREATE DATE-------------------------------------//

    @Override
    public String sortOrder(FilterOrderRequest filterOrderRequest, HttpSession session, Model model) {
        model.addAttribute("msg", sortOrderLogic(filterOrderRequest));
        return "viewOrderList";
    }
//    private SortOrderResponse sortOrderLogic(FilterOrderRequest filterRequest) {
//        FilterOrderResponse response = (FilterOrderResponse) filterOrderLogic(filterRequest);
//        List<FilterOrderResponse.OrderBill> orders = new ArrayList<>(response.getOrderList());
//        if ("asc".equalsIgnoreCase(filterRequest.getSortDirection())) {
//            orders.sort(Comparator.comparing(FilterOrderResponse.OrderBill::getCreateDate));
//        } else if ("desc".equalsIgnoreCase(filterRequest.getSortDirection())) {
//            orders.sort(Comparator.comparing(FilterOrderResponse.OrderBill::getCreateDate).reversed());
//        }
//
//        return SortOrderResponse.builder()
//                .status("200")
//                .message("Sort order successfully")
//                .orderList(orders)
//                .build();
//    }

    private SortOrderResponse sortOrderLogic(FilterOrderRequest filterRequest) {
        // Gọi filterOrderLogic để lấy các đơn hàng đã được lọc theo status
        FilterOrderResponse filteredResponse = (FilterOrderResponse) filterOrderLogic(filterRequest);
        List<FilterOrderResponse.OrderBill> orders = new ArrayList<>(filteredResponse.getOrderList());

        // Thực hiện sắp xếp theo sortDirection
        if ("asc".equalsIgnoreCase(filterRequest.getSortDirection())) {
            orders.sort(Comparator.comparing(FilterOrderResponse.OrderBill::getCreateDate));
        } else if ("desc".equalsIgnoreCase(filterRequest.getSortDirection())) {
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
    public String updateFlower(UpdateFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            redirectAttributes.addFlashAttribute("error", UpdateFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("response", updateFlowerLogic(request));
        return "redirect:/manageFlower";
    }

    private Object updateFlowerLogic(UpdateFlowerRequest request) {
        Map<String, String> error = UpdateFlowerValidation.validate(request, flowerRepo, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        flower.setName(request.getName());
        flower.setPrice(request.getPrice());
        flower.setDescription(request.getDescription());
        flower.setFlowerAmount(request.getFlowerAmount());
        flower.setQuantity(request.getQuantity());
        if (request.getQuantity()> 0) {
            flower.setStatus(Status.FLOWER_STATUS_AVAILABLE);
        }

//        List<FlowerCategory> existingCategories = flower.getFlowerCategoryList();
//        flowerCategoryRepo.deleteAll(existingCategories);
//
//        List<FlowerCategory> newCategories = request.getCategoryIdList().stream()
//                .map(categoryId -> FlowerCategory.builder()
//                        .flower(flower)
//                        .category(categoryRepo.findById(categoryId).orElse(null))
//                        .build())
//                .collect(Collectors.toList());
//
//        flowerCategoryRepo.saveAll(newCategories);

        flowerRepo.save(flower);
        return UpdateFlowerResponse.builder()
                .status("200")
                .message("Update flower successfully")
                .build();

    }

    //----------------------------------------DELETE FLOWER--------------------------------------------//

    @Override
    public String deleteFlower(DeleteFlowerRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            redirectAttributes.addFlashAttribute("error", DeleteFlowerResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("msg", deleteFlowerLogic(request));
        return "redirect:/manageFlower";
    }

    private DeleteFlowerResponse deleteFlowerLogic(DeleteFlowerRequest request) {
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        flower.setStatus(Status.FLOWER_STATUS_OUT_OF_STOCK);
        flower.setQuantity(0);
        flowerRepo.save(flower);
        return DeleteFlowerResponse.builder()
                .status("200")
                .message(flower.getName() + " has been deleted" + "(" + flower.getStatus() + ")")
                .build();
    }


    //----------------------------------------VIEW FLOWER IMAGE----------------------------------------------//

    @Override
    public String viewFlowerImage(ViewFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            model.addAttribute("error", ViewFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }
        model.addAttribute("msg", viewFlowerImageLogic(request));
        return "redirect:/seller/view/flower";
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
    public String addFlowerImage(AddFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            redirectAttributes.addFlashAttribute("error", AddFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("msg", (Map<String, String>) addFlowerImageLogic(request));
        return "redirect:/seller/view/flower";
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

    public String deleteFlowerImage(DeleteFlowerImageRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsSeller(account)) {
            redirectAttributes.addFlashAttribute("error", AddFlowerImageResponse.builder()
                    .status("400")
                    .message("Please login a seller account to do this action")
                    .build());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("msg", (DeleteFlowerImageResponse) deleteFlowerImageLogic(request.getImageId()));
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

    //----------------------------------------GET FLOWER CATEGORY----------------------------------------------//

    @Override
    public String getFlowerCategory(HttpSession session, Model model, int flowerId, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "You must log in");
            return "redirect:/login";
        }
        Object output = getFlowerCategoryLogic(flowerId);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerCategoryResponse.class)) {
            model.addAttribute("msg", (ViewFlowerCategoryResponse) output);
            return "viewFlowerCategory";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewFlowerCategory";
    }

    private Object getFlowerCategoryLogic(int flowerId) {
        Flower flower = flowerRepo.findById(flowerId).orElse(null);
        if (flower == null) {
            return Map.of("error", "Flower not found");
        }

        List<ViewFlowerCategoryResponse.CategoryDetail> categories = flower.getFlowerCategoryList().stream()
                .map(flowerCategory -> ViewFlowerCategoryResponse.CategoryDetail.builder()
                        .id(flowerCategory.getCategory().getId())
                        .name(flowerCategory.getCategory().getName())
                        .build())
                .collect(Collectors.toList());

        return ViewFlowerCategoryResponse.builder()
                .status("200")
                .message("Flower categories retrieved successfully")
                .categoryList(categories)
                .build();
    }

    //----------------------------------------UPDATE FLOWER CATEGORY----------------------------------------------//

    @Override
    public String updateFlowerCategory(UpdateFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "You must log in");
            return "redirect:/login";
        }
        Object output = updateFlowerCategoryLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateFlowerCategoryResponse.class)) {
            model.addAttribute("msg", (UpdateFlowerCategoryResponse) output);
            return "updateFlowerCategory";
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "updateFlowerCategory";
    }

    private Object updateFlowerCategoryLogic(UpdateFlowerCategoryRequest request) {
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        if (flower == null) {
            return Map.of("error", "Flower not found");
        }

        flowerCategoryRepo.deleteAll(flower.getFlowerCategoryList());

        List<FlowerCategory> newFlowerCategories = request.getCategoryId().stream()
                .map(categoryId -> {
                    Category category = categoryRepo.findById(categoryId).orElse(null);
                    return category != null ? FlowerCategory.builder()
                            .flower(flower)
                            .category(category)
                            .build() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        flowerCategoryRepo.saveAll(newFlowerCategories);

        return UpdateFlowerCategoryResponse.builder()
                .status("200")
                .message("Flower categories updated successfully")
                .build();
    }

    //----------------------------------------REMOVE FLOWER CATEGORY----------------------------------------------//

    @Override
    public String removeFlowerCategory(RemoveFlowerCategoryRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "You must log in");
            return "redirect:/login";
        }
        Object output = removeFlowerCategoryLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, RemoveFlowerCategoryResponse.class)) {
            model.addAttribute("msg", (RemoveFlowerCategoryResponse) output);
            return "removeFlowerCategory";
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "removeFlowerCategory";
    }

    private Object removeFlowerCategoryLogic(RemoveFlowerCategoryRequest request) {
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        if (flower == null) {
            return Map.of("error", "Flower not found");
        }

        FlowerCategory flowerCategory = flowerCategoryRepo.findByFlower_IdAndCategory_Id(request.getFlowerId(), request.getCategoryId());
        if (flowerCategory == null) {
            return Map.of("error", "Category not found for this flower");
        }

        flowerCategoryRepo.delete(flowerCategory);

        return RemoveFlowerCategoryResponse.builder()
                .status("200")
                .message("Category removed from flower successfully")
                .build();
    }

    //--------------------------------------------GET TOTAL NUMBER OF FLOWER---------------------------------------------//

    @Override
    public void getTotalNumberFlower(Model model) {
        model.addAttribute("totalNumberFlower", getTotalNumberFlowerLogic());
    }

    private GetTotalNumberFlowerResponse getTotalNumberFlowerLogic() {

        return GetTotalNumberFlowerResponse.builder()
                .message("200")
                .message("")
                .totalNumberFlowers(flowerRepo.findAll().stream()
                        .filter(
                                flower -> !flower.getStatus().equals(Status.FLOWER_STATUS_OUT_OF_STOCK) &&
                                LocalDate.now().getMonth().equals(flower.getCreateDate().getMonth())
                        )
                        .count()
                )
                .build();


    }

    //--------------------------------------------GET TOTAL NUMBER OF CANCELED ORDER---------------------------------------------//
    @Override
    public void getTotalNumberOfCanceledOrder(Model model) {
        model.addAttribute("totalNumberOfCanceledOrder", getTotalNumberOfCanceledOrderLogic());
    }

    private GetTotalNumberOfCanceledOrderResponse getTotalNumberOfCanceledOrderLogic() {
        return GetTotalNumberOfCanceledOrderResponse.builder()
                .status("200")
                .message("")
                .getTotalNumberOfCanceledOrder(orderRepo.findAll()
                        .stream()
                        .filter(
                                order -> order.getStatus().equals(Status.ORDER_STATUS_CANCELLED) &&
                                LocalDate.now().getMonth().equals(order.getCreatedDate().getMonth())
                        )
                        .count())
                .build();
    }

    //--------------------------------------------GET REVENUE---------------------------------------------//

    @Override
    public void getRevenue(Model model) {
        float totalRevenue = orderRepo.findAll().stream()
                .filter(order -> order.getStatus().equals(Status.ORDER_STATUS_COMPLETED) &&
                        LocalDate.now().getMonth().equals(order.getCreatedDate().getMonth())
                )
                .map(Order::getTotalPrice)
                .reduce(0f, Float::sum);
        model.addAttribute("revenue", totalRevenue);
    }


    //--------------------------------------------GET TOTAL NUMBER OF ORDER---------------------------------------------//
    @Override
    public void getTotalNumberOfOrder(Model model) {
        model.addAttribute("totalNumberOfOrder", getTotalNumberOfOrderLogic());
    }


    private GetTotalNumberOfOrderResponse getTotalNumberOfOrderLogic() {
        return GetTotalNumberOfOrderResponse.builder()
                .status("200")
                .message("")
                .totalTotalNumberOfOder(orderRepo.findAll().stream()
                        .filter(
                                order -> order.getStatus().equals(Status.ORDER_STATUS_COMPLETED) &&
                                LocalDate.now().getMonth().equals(order.getCreatedDate().getMonth())
                        )
                        .count())
                .build();
    }

    //--------------------------------------------GET SOLD QUANTITY CATEGORY---------------------------------------------//
    @Override
    public void getSoldQuantityCategory(Model model, HttpSession session) {
        model.addAttribute("soldQuantityCategory", getSoldQuantityCategoryLogic(session));
    }

    private GetSoldQuantityCategoryResponse getSoldQuantityCategoryLogic(HttpSession session) {
        return GetSoldQuantityCategoryResponse.builder()
                .status("200")
                .message("")
                .soldQuantityCategories(categoryRepo.findAll().stream()
                        .map(cate -> GetSoldQuantityCategoryResponse.soldQuantityCategory.builder()
                                .soldFlowerQuantity(calculateSoldQuantityInCategory(cate, session))
                                .category(cate.getName())
                                .build())
                        .toList())
                .build();
    }

    private Long calculateSoldQuantityInCategory(Category category, HttpSession session) {
        long total = 0;
        Seller seller = Role.getCurrentLoggedAccount(session).getUser().getSeller();
        List<Flower> flowers = categoryRepo.findAll().stream()
                .filter(cate -> cate.equals(category))
                .map(Category::getFlowerCategoryList)
                .flatMap(List::stream)
                .map(FlowerCategory::getFlower)
                .filter(flower -> flower.getSeller().getId().equals(seller.getId()))
                .distinct()
                .toList();

        for (Flower flower : flowers) {
            total += flower.getSoldQuantity();
        }
        return total;
    }

    //----------------------------------------GET ORDER IN DAILY---------------------------------------//
    @Override
    public void getOrderInDaily(Model model) {
        model.addAttribute("orderInDaily", getOrderInDailyLogic());
    }


    private GetOrderInDailyResponse getOrderInDailyLogic() {
        List<LocalDate> listTenDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 0; i < 20; i++) {
            listTenDates.add(LocalDate.now().minusDays(i));
        }
        Collections.reverse(listTenDates);

        return GetOrderInDailyResponse.builder()
                .status("200")
                .message("")
                .orderCounts(
                        listTenDates.stream()
                                .map(date -> GetOrderInDailyResponse.OrderCount.builder()
                                        .count(getQuantityOrder(date))
                                        .date(date.format(formatter))
                                        .build())
                                .toList()
                )
                .build();
    }

    private long getQuantityOrder(LocalDate date) {
        return orderRepo.findAll().stream()
                .filter(order -> order.getCreatedDate().toLocalDate().equals(date))
                .count();
    }

    //----------------------------------------VIEW FEEDBACK---------------------------------------//

    @Override
    public String viewFeedback(int sellerId, Model model, HttpSession session) {
        Object output = viewFeedbackLogic(sellerId);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFeedbackResponse.class)) {
            model.addAttribute("msg", (ViewFeedbackResponse) output);
            return "feedback";
        }

        model.addAttribute("error", (Map<String, String>) output);
        return "feedback";
    }

    @Override
    public ViewFeedbackResponse viewFeedbackAPI(int sellerId) {
        Object output = viewFeedbackLogic(sellerId);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFeedbackResponse.class)) {
            return (ViewFeedbackResponse) output;
        }
        return ViewFeedbackResponse.builder()
                .status("400")
                .message("Feedback not found")
                .build();
    }

    private Object viewFeedbackLogic(int sellerId) {
        Seller seller = sellerRepo.findById(sellerId).orElse(null);
        if (seller == null) {
            return Map.of("error", "Seller not found");
        }

        List<Feedback> feedbackList = seller.getFeedbackList();
        if (feedbackList.isEmpty()) {
            return ViewFeedbackResponse.builder()
                    .status("404")
                    .message("No feedback found for this seller")
                    .build();
        }

        List<ViewFeedbackResponse.FeedbackDetail> feedbackDetails = feedbackList.stream()
                .map(this::mapToFeedbackDetail)
                .sorted(Comparator.comparing(ViewFeedbackResponse.FeedbackDetail::getId).reversed())
                .collect(Collectors.toList());

        return ViewFeedbackResponse.builder()
                .status("200")
                .message("Feedback found")
                .id(seller.getId())
                .name(seller.getUser().getName())
                .email(seller.getUser().getAccount().getEmail())
                .phone(seller.getUser().getPhone())
                .avatar(seller.getUser().getAvatar())
                .background(seller.getUser().getBackground())
                .totalFlower(seller.getFlowerList().size())
                .sellerRating(seller.getRating())
                .feedbackList(feedbackDetails)
                .build();
    }

    private ViewFeedbackResponse.FeedbackDetail mapToFeedbackDetail(Feedback feedback) {
        List<String> flowerNameList = feedback.getOrder().getOrderDetailList().stream()
                .map(orderDetail -> orderDetail.getFlower().getName())
                .toList();

        return ViewFeedbackResponse.FeedbackDetail.builder()
                .id(feedback.getId())
                .name(feedback.getUser().getName())
                .avatar(feedback.getUser().getAvatar())
                .content(feedback.getContent())
                .rating(feedback.getRating())
                .createDate(feedback.getCreateDate().toLocalDate())
                .flowerNameList(flowerNameList)
                .build();
    }
}


