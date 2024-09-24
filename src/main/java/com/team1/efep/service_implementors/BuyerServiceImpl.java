package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.AddToWishlistValidation;
import com.team1.efep.validations.DeleteWishlistItemValidation;
import com.team1.efep.validations.ViewFlowerDetailValidation;
import com.team1.efep.validations.ViewOrderHistoryValidation;
import com.team1.efep.validations.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final JavaMailSenderImpl mailSender;
    private final AccountRepo accountRepo;
    private final FlowerRepo flowerRepo;
    private final WishlistItemRepo wishlistItemRepo;
    private final OrderRepo orderRepo;
    private final WishlistRepo wishlistRepo;

    @Override
    public String sendEmail(ForgotRequest request, Model model) {
        return "";
    }


    //---------------------------------------VIEW CART------------------------------------------//
    @Override
    public String viewWishlist(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        model.addAttribute("response", viewWishlistItemList(account.getId()));
        return "viewWishlist";
    }

    @Override
    public ViewWishlistResponse viewWishlistAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null) {
            return ViewWishlistResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        return ViewWishlistResponse.builder()
                .status("200")
                .message("View wishlist successfully")
                .id(account.getUser().getWishlist().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .wishlistItemList(viewWishlistItemList(account.getId()))
                .build();
    }

    private Wishlist viewWishlistLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        assert account != null;
        return account.getUser().getWishlist();
    }

    private List<ViewWishlistResponse.WishlistItems> viewWishlistItemList(int accountId) {
        return viewWishlistLogic(accountId).getWishlistItemList().stream()
                .map(item -> new ViewWishlistResponse.WishlistItems(item.getId(), item.getFlower().getName(), item.getQuantity(), item.getFlower().getPrice()))
                .toList();
    }

    //-------------------------------------------------ADD TO CART-----------------------------------------------------//

    @Override
    public String addToWishlist(AddToWishlistRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = addToCartLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToWishlistResponse.class)) {
            model.addAttribute("response", (AddToWishlistResponse) output);
            return "base";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "base";
    }

    @Override
    public AddToWishlistResponse addToWishlistAPI(AddToWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return AddToWishlistResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = addToCartLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToWishlistResponse.class)) {
            return (AddToWishlistResponse) output;
        }
        return AddToWishlistResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object addToCartLogic(AddToWishlistRequest request) {
        Map<String, String> errors = AddToWishlistValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Wishlist wishlist = account.getUser().getWishlist();
        if (checkExistedItem(request, wishlist)) {
            WishlistItem wishlistItem = wishlistItemRepo.findByFlower_Id(request.getFlowerId()).orElse(null);
            assert wishlistItem != null;
            wishlistItem.setQuantity(wishlistItem.getQuantity() + request.getQuantity());
            wishlistItemRepo.save(wishlistItem);
        } else {
            wishlist.getWishlistItemList().add(
                    wishlistItemRepo.save(
                            WishlistItem.builder()
                                    .wishlist(wishlist)
                                    .flower(flower)
                                    .quantity(request.getQuantity())
                                    .build()));
            accountRepo.save(account);
        }
        return AddToWishlistResponse.builder()
                .status("200")
                .message("Your flower has been added to wishlist successfully")
                .build();
    }

    private boolean checkExistedItem(AddToWishlistRequest request, Wishlist wishlist) {
        return wishlist.getWishlistItemList().stream()
                .anyMatch(item -> Objects.equals(item.getFlower().getId(), request.getFlowerId()));
    }
//
//    private Flower checkAvailableFlower(int flowerId) {
//        Flower flower = flowerRepo.findById(flowerId).orElse(null);
//        assert flower != null;
//        if (flower.getFlowerStatus().getStatus().equals(Const.FLOWER_STATUS_AVAILABLE)) {
//            return flower;
//        }
//        return null;
//    }

    //-----------------------------------------------FORGOT PASSWORD------------------------------------------------------------//
    @Override
    public ForgotResponse sendEmailAPI(ForgotRequest request) {
        try {
            return sendEmailLogic(request);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private ForgotResponse sendEmailLogic(ForgotRequest request) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("quynhpvnse182895@fpt.edu.vn");
//        message.setTo(request.getToEmail());
//        String otp = OTPGeneratorUtil.generateOTP(6);
//        message.setText(FileReaderUtil.readFile(otp));
//        message.setSubject(request.getSubject());
//        message.;
//        mailSender.send(message);
//        return ForgotResponse.builder()
//                .status("200")
//                .message("Send email successfully")
//                .build();

        // Generate the OTP
        String otp = OTPGeneratorUtil.generateOTP(6);

        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Helper to set the attributes for the MimeMessage
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Set the email attributes
        helper.setFrom("quynhpvnse182895@fpt.edu.vn");
        helper.setTo(request.getToEmail());
        helper.setSubject(request.getSubject());

        // Read HTML content from a file and replace placeholders (e.g., OTP)
        String emailContent = FileReaderUtil.readFile(otp); // Assuming readFile returns HTML content as a String

        // Set the email content as HTML
        helper.setText(emailContent, true);  // 'true' indicates that the text is HTML

        // Send the email
        mailSender.send(message);

        // Return response
        return ForgotResponse.builder()
                .status("200")
                .message("Send email successfully")
                .build();
    }

    //-----------------------------------------------RENEW PASSWORD------------------------------------------------------------//

    @Override
    public String renewPass(RenewPasswordRequest request, Model model) {
        return "home";
    }

    @Override
    public RenewPasswordResponse renewPassAPI(RenewPasswordRequest request) {
        return renewPassLogic(request);
    }

    private RenewPasswordResponse renewPassLogic(RenewPasswordRequest request) {
        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc != null && request.getPassword().equals(request.getConfirmPassword())) {
            acc.setPassword(request.getPassword());
            accountRepo.save(acc);
            return RenewPasswordResponse.builder()
                    .status("200")
                    .message("Renew password successfully")
                    .build();
        }

        return RenewPasswordResponse.builder()
                .status("400")
                .message("Invalid email or password")
                .build();
    }

    //-------------------------------------------VIEW BUYER FLOWER LIST---------------------------------------//
    @Override
    public String viewFlowerList(HttpSession session, Model model) {
        ViewFlowerListResponse output = viewFlowerListLogic();
        model.addAttribute("msg", output);
        return "category";
    }

    @Override
    public ViewFlowerListResponse viewFlowerListAPI() {
        ViewFlowerListResponse output = viewFlowerListLogic();
        return output;
    }

    private ViewFlowerListResponse viewFlowerListLogic() {
        List<Flower> flowers = flowerRepo.findAll();
        // if find -> print size of flower
        return ViewFlowerListResponse.builder()
                .status("200")
                .message("Number of flowers: " + flowers.size())
                .flowerList(viewFlowerList(flowers))
                .build();
    }

    private List<ViewFlowerListResponse.Flower> viewFlowerList(List<Flower> flowers) {
        return flowers.stream()
                .map(item -> ViewFlowerListResponse.Flower.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .rating(item.getRating())
                        .images(viewImageList(item.getFlowerImageList()))
                        .build()
                ).toList();
    }

    private List<ViewFlowerListResponse.Image> viewImageList(List<FlowerImage> imageList) {
        return imageList.stream()
                .map(img -> ViewFlowerListResponse.Image.builder()
                        .link(img.getLink())
                        .build())
                .toList();
    }

    //-------------------------------------------VIEW BUYER SLIDE BAR---------------------------------------//

    @Override
    public void viewSlideBar(Model model) {
        model.addAttribute("msg", viewSlideBarLogic());
    }

    @Override
    public ViewSlideBarResponse viewSlideBarAPI() {
        return viewSlideBarLogic();
    }


    public ViewSlideBarResponse viewSlideBarLogic() {
        List<String> flowerImageLinkList = new ArrayList<>();

        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/003/110/648/original/spring-sale-banner-season-floral-discount-poster-with-flowers-vector.jpg");
        flowerImageLinkList.add("https://as2.ftcdn.net/v2/jpg/02/44/86/81/1000_F_244868120_ZDcYjdJ6NMJHumrT6FQQQDiiEkX9h427.jpg");
        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/003/110/679/large_2x/summer-sale-promo-web-banner-multicolour-editable-floral-flower-frame-vector.jpg");
        flowerImageLinkList.add("https://static.vecteezy.com/system/resources/previews/021/600/647/large_2x/3d-rendering-spring-sale-banner-with-beautiful-colorful-flower-can-be-used-for-template-banners-wallpaper-flyers-invitation-posters-brochure-voucher-discount-photo.jpg");
        flowerImageLinkList.add("https://as1.ftcdn.net/v2/jpg/02/40/86/86/1000_F_240868665_0HcnhSG2uUOvAvCdRrHnnTIDsCAGTUqK.jpg");

        return ViewSlideBarResponse.builder()
                .status("200")
                .message("")
                .imageList(flowerImageLinkList)
                .build();
    }

    //----------------------------------------------DELETE CART ITEM----------------------------------------------//


    @Override
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = deleteCartItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistItemResponse.class)) {
            model.addAttribute("msg", (DeleteWishlistItemResponse) output);
            return "viewCart";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "";
    }

    @Override
    public DeleteWishlistItemResponse deleteWishlistItemAPI(DeleteWishlistItemRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return DeleteWishlistItemResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = deleteCartItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistItemResponse.class)) {
            return (DeleteWishlistItemResponse) output;
        }
        return DeleteWishlistItemResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object deleteCartItemLogic(DeleteWishlistItemRequest request) {
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Map<String, String> errors = DeleteWishlistItemValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        Wishlist wishlist = account.getUser().getWishlist();
        Optional<WishlistItem> cartItemOptional = wishlist.getWishlistItemList().stream()
                .filter(item -> Objects.equals(item.getId(), request.getWishlistItemId()))
                .findFirst();

        WishlistItem wishlistItem = cartItemOptional.get();
        wishlist.getWishlistItemList().remove(wishlistItem);
        wishlistItemRepo.delete(wishlistItem);

        accountRepo.save(account);
        return DeleteWishlistItemResponse.builder()
                .status("200")
                .message("Flower is deleted")
                .build();
    }

    //--------------------------------------VIEW ORDER HISTORY------------------------------------------//

    @Override
    public String viewOrderHistory(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)) {
            model.addAttribute("response", (ViewOrderHistoryResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ViewOrderHistoryResponse viewOrderHistoryAPI(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)) {
            return (ViewOrderHistoryResponse) output;
        }
        return ViewOrderHistoryResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object viewOrderHistoryLogic(int accountId) {
        List<Order> orderList = orderRepo.findAllByUser_Id(accountId);
        Map<String, String> errors = ViewOrderHistoryValidation.orderHistoryValidation();
        if (!errors.isEmpty()) {
            return errors;
        }
        if (!orderList.isEmpty()) {
            List<ViewOrderHistoryResponse.Order> orders = orderList.stream()
                    .map(this::viewOrderList)
                    .collect(Collectors.toList());
            return ViewOrderHistoryResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orders)
                    .build();
        }
        return errors;
    }

    private ViewOrderHistoryResponse.Order viewOrderList(Order order) {
        return ViewOrderHistoryResponse.Order.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .detailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderHistoryResponse.Detail> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderHistoryResponse.Detail.builder()
                        .sellerName(detail.getFlower().getSeller().getUser().getName())
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //-------------------------------VIEW ORDER DETAIL--------------------------------------//

    @Override
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ViewOrderHistoryResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            model.addAttribute("response", (ViewOrderDetailResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "seller";
    }

    @Override
    public ViewOrderDetailResponse viewOrderDetailAPI(ViewOrderDetailRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderDetailResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
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
        Map<String, String> errors = ViewOrderDetailValidation.validate(request);
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        if (!errors.isEmpty()) {
            return errors;
        }

        List<ViewOrderDetailResponse.Detail> detailList = viewOrderDetailLists(order.getOrderDetailList());

        return ViewOrderDetailResponse.builder()
                .status("200")
                .message("Order details retrieved successfully")
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .detailList(detailList)
                .build();
    }

    private List<ViewOrderDetailResponse.Detail> viewOrderDetailLists(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderDetailResponse.Detail.builder()
                        .sellerName(detail.getFlower().getSeller().getUser().getName())
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    //--------------------------------------VIEW FLOWER TOP LIST------------------------------------------//

    @Override
    public void viewFlowerTopList(int top, Model model) {
        model.addAttribute("msg", viewFlowerTopListLogic(top));
    }

    @Override
    public ViewFlowerTopListResponse viewFlowerTopListAPI(int top) {
        return viewFlowerTopListLogic(top);
    }


    public ViewFlowerTopListResponse viewFlowerTopListLogic(int top) {

        return ViewFlowerTopListResponse.builder()
                .status("200")
                .message("")
                .flowerList(
                        flowerRepo.findAll()
                                .stream()
                                .limit(top)
                                .map(
                                        flower -> ViewFlowerTopListResponse.Flower.builder()
                                                .id(flower.getId())
                                                .name(flower.getName())
                                                .price(flower.getPrice())
                                                .images(
                                                        flower.getFlowerImageList().stream()
                                                                .map(img -> ViewFlowerTopListResponse.Image.builder()
                                                                        .link(img.getLink())
                                                                        .build())
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    //--------------------------------------SEARCH FLOWER------------------------------------------//

    @Override
    public String searchFlower(SearchFlowerRequest request) {
        return "category";
    }

    @Override
    public SearchFlowerResponse searchFlowerAPI(SearchFlowerRequest request) {
        return searchFlowerLogic(request);
    }

    public SearchFlowerResponse searchFlowerLogic(SearchFlowerRequest request) {
        return SearchFlowerResponse.builder()
                .status("200")
                .message("")
                .flowers(
                        flowerRepo.findAll()
                                .stream()
                                .filter(flower -> flower.getName().contains(request.getName()))
                                .map(
                                        flower -> SearchFlowerResponse.Flower.builder()
                                                .id(flower.getId())
                                                .name(flower.getName())
                                                .price(flower.getPrice())
                                                .images(
                                                        flower.getFlowerImageList().stream()
                                                                .map(img -> SearchFlowerResponse.Image.builder()
                                                                        .link(img.getLink())
                                                                        .build())
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }

    //--------------------------------------VIEW FLOWER DETAIL(CHUA CHAC FE)------------------------------------------//

    @Override
    public String viewFlowerDetail(ViewFlowerDetailRequest request, Model model) {
        Object output = viewFlowerDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerDetailResponse.class)) {
            model.addAttribute("msg", (ViewFlowerDetailResponse) output);
            return "flowerDetail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    @Override
    public ViewFlowerDetailResponse viewFlowerDetailAPI(ViewFlowerDetailRequest request) {
        Object output = viewFlowerDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerDetailResponse.class)) {
            return (ViewFlowerDetailResponse) output;
        }
        return ViewFlowerDetailResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    public Object viewFlowerDetailLogic(ViewFlowerDetailRequest request) {
        Map<String, String> errors = ViewFlowerDetailValidation.validate();
        if (errors.isEmpty()) {
            Flower flower = flowerRepo.findById(request.getId()).orElse(null);
            assert flower != null;
            return ViewFlowerDetailResponse.builder()
                    .status("200")
                    .message("")
                    .id(flower.getId())
                    .name(flower.getName())
                    .price(flower.getPrice())
                    .flowerAmount(flower.getFlowerAmount())
                    .quantity(flower.getQuantity())
                    .soldQuantity(flower.getSoldQuantity())
                    .imageList(
                            flower.getFlowerImageList().stream()
                                    .map(
                                            img -> ViewFlowerDetailResponse.Image.builder()
                                                    .link(img.getLink())
                                                    .build()
                                    )
                                    .toList()
                    )
                    .build();
        }
        ;
        return errors;
    }

    //-----------------------------------VIEW ORDER STATUS-------------------------------------------//


    @Override
    public String viewOrderStatus(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }

        int orderId = (int) session.getAttribute("orderId");
        ViewOrderStatusResponse statusResponse = viewOrderStatusLogic(orderId);

        if (statusResponse != null) {
            model.addAttribute("orderStatus", statusResponse.getOrderStatus());
            return "orderPage";
        } else {
            model.addAttribute("error", "Order not found");
            return "errorPage";
        }
    }

    @Override
    public ViewOrderStatusResponse viewOrderStatusAPI(ViewOrderStatusRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return ViewOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
            return viewOrderStatusLogic(request.getOrderId());
    }


    private ViewOrderStatusResponse viewOrderStatusLogic(int orderId){
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            return ViewOrderStatusResponse.builder()
                    .status("200")
                    .message("View Order Status successful")
                    .orderStatus(order.getStatus())
                    .build();
        }
        return ViewOrderStatusResponse.builder()
                .status("Order not found")
                .build();
    }

    //------------------------------UPDATE WISHLIST--------------------------------------//

    @Override
    public String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = updateWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateWishlistResponse.class)) {
            model.addAttribute("response", (UpdateWishlistResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "wishlist";
    }

    @Override
    public UpdateWishlistResponse updateWishlistAPI(UpdateWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return UpdateWishlistResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = updateWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateWishlistResponse.class)) {
            return (UpdateWishlistResponse) output;
        }
        return UpdateWishlistResponse.builder()
                .status("400")
                .message("Wishlist updated fail")
                .build();
    }


    private Object updateWishlistLogic(UpdateWishlistRequest request) {
        Map<String, String> errors = UpdateWishlistValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }

        Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
        assert wishlist != null;

        WishlistItem wishlistItem = wishlistItemRepo.findById(Integer.parseInt(request.getWishlistItemId())).orElse(null);
        assert wishlistItem != null;

        if ("plus".equals(request.getRequest())) {
            wishlistItem.setQuantity(wishlistItem.getQuantity() + 1);
        } else if ("minus".equals(request.getRequest())) {
            if (wishlistItem.getQuantity() > 1) {
                wishlistItem.setQuantity(wishlistItem.getQuantity() - 1);
            } else {
                wishlistItemRepo.delete(wishlistItem);
            }
        }

        wishlistItemRepo.save(wishlistItem);
        return UpdateWishlistResponse.builder()
                .status("200")
                .message("Wishlist updated successfully")
                .build();
    }

    //--------------------------------DELETE WISHLIST-----------------------------------//


    @Override
    public String deleteWishlist(DeleteWishlistRequest request,HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }

        Object output = deleteWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistResponse.class)) {
            model.addAttribute("response", (DeleteWishlistResponse) output);
        }

        model.addAttribute("response", (Map<String, String>) output);
        return "wishlist";
    }

    @Override
    public DeleteWishlistResponse deleteWishlistAPI(DeleteWishlistRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            return DeleteWishlistResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }

        Object output = deleteWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistResponse.class)) {
            return (DeleteWishlistResponse) output;
        }

        return DeleteWishlistResponse.builder()
                .message("400")
                .message("Wishlist deleted fail")
                .build();
    }

    private Object deleteWishlistLogic(DeleteWishlistRequest request) {
        Map<String, String> errors = DeleteWishlistValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }

        Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
        assert wishlist != null;

        wishlistItemRepo.deleteAll(wishlist.getWishlistItemList());

        wishlist.getWishlistItemList().clear();
        wishlistRepo.save(wishlist);

        return DeleteWishlistResponse.builder()
                .status("200")
                .message("Wishlist deleted successfully")
                .build();
    }

    //--------------------------------CANCEL ORDER------------------------------------------//

    @Override
    public String cancelOrder(CancelOrderRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "login";
        }
        Object output = cancelOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ChangeOrderStatusResponse.class)) {
            model.addAttribute("response", (ChangeOrderStatusResponse) output);
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "buyer";
    }

    @Override
    public CancelOrderResponse cancelOrderAPI(CancelOrderRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            ChangeOrderStatusResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build();
        }
        Object output = cancelOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelOrderResponse.class)) {
            return (CancelOrderResponse) output;
        }
        return CancelOrderResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object cancelOrderLogic(CancelOrderRequest request) {
        Map<String, String> errors = CancelOrderValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, "cancelled", orderRepo);

        return CancelOrderResponse.builder()
                .status("200")
                .message("Cancel order successfully")
                .build();
    }

    //--------------------------------VIEW CATEGORY------------------------------------------//

    @Override
    public String viewCategory(ViewCategoryListRequest request, Model model) {
        return "";
    }

    @Override
    public ViewCategoryListResponse viewCategoryAPI(ViewCategoryListRequest request) {
        return null;
    }

    public Object viewCategoryLogic(ViewCategoryListRequest request) {
        return null;
    }

}

