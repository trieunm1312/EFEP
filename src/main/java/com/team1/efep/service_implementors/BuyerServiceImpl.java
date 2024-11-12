package com.team1.efep.service_implementors;

import com.team1.efep.VNPay.BuyNowVNPAYConfig;
import com.team1.efep.VNPay.VNPayConfig;
import com.team1.efep.configurations.AllPage;
import com.team1.efep.configurations.MapConfig;
import com.team1.efep.enums.Const;
import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.*;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    private final OrderDetailRepo orderDetailRepo;
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final FeedbackRepo feedbackRepo;
    private final SellerRepo sellerRepo;

    //---------------------------------------VIEW WISHLIST------------------------------------------//
    @Override
    public String viewWishlist(HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute(MapConfig.buildMapKey(error, "You are not logged in"));
            return "redirect:/login";
        }
        Object output = viewWishlistLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewWishlistResponse.class)) {
            model.addAttribute("msg", (ViewWishlistResponse) output);
            System.out.println(account.getUser().getWishlist().getWishlistItemList().size());
            return "viewWishlist";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewWishlist";
    }

    private Object viewWishlistLogic(int accountId) {
        Map<String, String> error = ViewWishlistValidation.validate(accountId, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Account account = accountRepo.findById(accountId).orElse(null);
        assert account != null;

        List<WishlistItem> wishlistItemList = account.getUser().getWishlist().getWishlistItemList();
        List<ViewWishlistResponse.WishlistItems> wishlistItems = viewWishlistItemList(accountId);

        float totalPrice = wishlistItems.stream()
                .map(item -> item.getPrice() * item.getQuantity())
                .reduce(0f, Float::sum);

        for (ViewWishlistResponse.WishlistItems item : wishlistItems) {
            if (item.getStatus().equals(Status.FLOWER_STATUS_OUT_OF_STOCK)) {
                wishlistItemList.remove(wishlistItemRepo.findById(item.getId()).orElse(null));
            }
        }
        wishlistRepo.save(account.getUser().getWishlist());


        return ViewWishlistResponse.builder()
                .status("200")
                .id(account.getUser().getWishlist().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .totalPrice(totalPrice)
                .wishlistItemList(viewWishlistItemList(accountId))
                .build();
    }

    private List<ViewWishlistResponse.WishlistItems> viewWishlistItemList(int accountId) {
        Account account = accountRepo.findById(accountId).orElse(null);
        assert account != null;
        return account.getUser().getWishlist().getWishlistItemList().stream()
                .map(item -> ViewWishlistResponse.WishlistItems.builder()
                        .id(item.getId())
                        .imgList(
                                viewImageList(
                                        item.getFlower().getFlowerImageList().stream()
                                                .map(FlowerImage::getLink).toList()
                                )
                        )
                        .name(item.getFlower().getName())
                        .quantity(Math.min(item.getQuantity(), item.getFlower().getQuantity()))
                        .price(item.getFlower().getPrice())
                        .stockQuantity(item.getFlower().getQuantity())
                        .description(item.getFlower().getDescription())
                        .status(item.getFlower().getStatus())
                        .build())
                .toList();
    }

    //-------------------------------------------------ADD TO WISHLIST-----------------------------------------------------//

    @Override
    public String addToWishlist(AddToWishlistRequest request, HttpServletRequest httpServletRequest, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute(MapConfig.buildMapKey(error, "You are not logged in"));
            return "redirect:/login";
        }
        Object output = addToWishlistLogic(request, account);

        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToWishlistResponse.class)) {
            account = accountRepo.findById(request.getAccountId()).orElse(null);
            session.setAttribute("acc", account);
            if(httpServletRequest.getHeader("Referer").contains("category")){
                Category category = categoryRepo.findByName(request.getKeyword());
                ((AddToWishlistResponse) output).setKeyword(category.getId() + "");
            }
            redirectAttributes.addFlashAttribute("msg1", (AddToWishlistResponse) output);
            return "redirect:" + httpServletRequest.getHeader("Referer");
        }
        redirectAttributes.addFlashAttribute("error", output);
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    private Object addToWishlistLogic(AddToWishlistRequest request, Account account) {
        Map<String, String> error = AddToWishlistValidation.validate(request, accountRepo, flowerRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Flower flower = flowerRepo.findById(request.getFlowerId()).orElse(null);
        assert flower != null;
        Wishlist wishlist = account.getUser().getWishlist();
        if (checkExistedItem(request, wishlist)) {
            WishlistItem wishlistItem = wishlistItemRepo.findByFlower_Id(request.getFlowerId()).orElse(null);
            assert wishlistItem != null;
            if(wishlistItem.getQuantity() >= wishlistItem.getFlower().getQuantity() || wishlistItem.getQuantity() + request.getQuantity() > wishlistItem.getFlower().getQuantity()){
                wishlistItem.setQuantity(Math.min(wishlistItem.getQuantity() + request.getQuantity(), wishlistItem.getFlower().getQuantity()));
                wishlistItemRepo.save(wishlistItem);
                return AddToWishlistResponse.builder()
                        .status("200")
                        .message("You have add all available quantity of this flower to wishlist")
                        .build();
            }
            wishlistItem.setQuantity(wishlistItem.getQuantity() + request.getQuantity());
            wishlistItemRepo.save(wishlistItem);
            accountRepo.save(account);
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

    //----------------------------------------------UPDATE WISHLIST----------------------------------------------//

    @Override
    public String updateWishlist(UpdateWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            return "redirect:/login";
        }
        request.setAccountId(account.getId());
        Object output = updateWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, UpdateWishlistResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            redirectAttributes.addFlashAttribute("msg", (UpdateWishlistResponse) output);
            AllPage.allConfig(model, this);
            return viewWishlist(session, model, redirectAttributes);
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        AllPage.allConfig(model, this);
        return viewWishlist(session, model, redirectAttributes);
    }

    private Object updateWishlistLogic(UpdateWishlistRequest request) {
        Map<String, String> error = UpdateWishlistValidation.validate(request, wishlistItemRepo, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }

        Wishlist wishlist = wishlistRepo.findById(request.getWishlistId()).orElse(null);
        assert wishlist != null;

        WishlistItem wishlistItem = wishlistItemRepo.findById(request.getWishlistItemId()).orElse(null);
        assert wishlistItem != null;

        if ("asc".equals(request.getRequest())) {
            wishlistItem.setQuantity(wishlistItem.getQuantity() + 1);
        } else if ("desc".equals(request.getRequest())) {
            if (wishlistItem.getQuantity() > 1) {
                wishlistItem.setQuantity(wishlistItem.getQuantity() - 1);
            } else {
                wishlistItemRepo.delete(wishlistItem);
            }
        }

        wishlistItemRepo.save(wishlistItem);
        wishlistRepo.save(wishlist);
        return UpdateWishlistResponse.builder()
                .status("200")
                .message("Wishlist updated successfully")
                .build();
    }

    //--------------------------------DELETE WISHLIST-----------------------------------//

    @Override
    public String deleteWishlist(DeleteWishlistRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Object output = deleteWishlistLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            redirectAttributes.addFlashAttribute("msg", (DeleteWishlistResponse) output);
            return "redirect:/buyer/wishlist";
        }

        model.addAttribute("response", (Map<String, String>) output);
        session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
        return "redirect:/buyer/wishlist";
    }

    private Object deleteWishlistLogic(DeleteWishlistRequest request) {
        Map<String, String> error = DeleteWishlistValidation.validate(request, accountRepo, wishlistRepo);
        if (!error.isEmpty()) {
            System.out.println(error);
            return error;
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

    //----------------------------------------------DELETE WISHLIST ITEM----------------------------------------------//

    @Override
    public String deleteWishlistItem(DeleteWishlistItemRequest request, HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute(MapConfig.buildMapKey(error, "You are not logged in"));
            return "redirect:/login";
        }
        Object output = deleteWishlistItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteWishlistItemResponse.class)) {
            session.setAttribute("acc", accountRepo.findById(request.getAccountId()).orElse(null));
            redirectAttributes.addFlashAttribute("msg", (DeleteWishlistItemResponse) output);
            return "redirect:/buyer/wishlist";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "home";
    }

    private Object deleteWishlistItemLogic(DeleteWishlistItemRequest request) {
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Map<String, String> error = DeleteWishlistItemValidation.validate(request, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Wishlist wishlist = account.getUser().getWishlist();
        Optional<WishlistItem> wishlistItemOptional = wishlist.getWishlistItemList().stream()
                .filter(item -> Objects.equals(item.getId(), request.getWishlistItemId()))
                .findFirst();

        WishlistItem wishlistItem = wishlistItemOptional.get();
        wishlist.getWishlistItemList().remove(wishlistItem);
        wishlistItemRepo.delete(wishlistItem);

        accountRepo.save(account);
        return DeleteWishlistItemResponse.builder()
                .status("200")
                .message("Flower is deleted")
                .build();
    }


    //-----------------------------------------------FORGOT PASSWORD------------------------------------------------------------//
    @Override
    public String sendEmail(ForgotPasswordRequest request, Model model, HttpSession session,  RedirectAttributes redirectAttributes) {
        Object output = sendEmailLogic(request);
        if (!OutputCheckerUtil.checkIfThisIsAResponseObject(output, ForgotPasswordResponse.class)) {
            redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
            return "redirect:/login";
        }
        ForgotPasswordResponse response = (ForgotPasswordResponse) output;
        session.setAttribute("mail", request.getToEmail());
        session.setAttribute("otp" + request.getToEmail(), response.getExtraInfo());
        redirectAttributes.addFlashAttribute("email", request.getToEmail());
        return "redirect:/login";
    }

    private Object sendEmailLogic(ForgotPasswordRequest request) {
        Map<String, String> error = ForgotPasswordValidation.validate(request, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        // Generate the OTP
        String otp = Const.OTP_LINK + OTPGeneratorUtil.generateOTP(6);

        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        // Helper to set the attributes for the MimeMessage
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            // Set the email attributes
            helper.setFrom("vannhuquynhp@gmail.com");
            helper.setTo(request.getToEmail());
            helper.setSubject(Const.EMAIL_SUBJECT);

            // Read HTML content from a file and replace placeholders (e.g., OTP)
            String emailContent = FileReaderUtil.readFile(otp); // Assuming readFile returns HTML content as a String

            // Set the email content as HTML
            helper.setText(emailContent, true);  // 'true' indicates that the text is HTML

            // Send the email
            mailSender.send(message);

            // Return response
            return ForgotPasswordResponse.builder()
                    .status("200")
                    .message("Send email successfully")
                    .extraInfo(otp)
                    .build();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //-----------------------------------------------Verify OTP------------------------------------------------------------//

    @Override
    public String handleOTP(String code, Model model, HttpSession session) {
        return handleOTPLogic(code, session);
    }

    private String handleOTPLogic(String code, HttpSession session) {
        System.out.println(code);
        System.out.println(session.getAttribute("mail"));
        System.out.println(session.getAttribute("otp" + session.getAttribute("mail")));

        String sessionLink = session.getAttribute("otp" + session.getAttribute("mail")).toString();
        String sessionCode = sessionLink.substring(sessionLink.indexOf("=") + 1);

        session.removeAttribute("otp" + session.getAttribute("mail"));


        return code.trim().equals(sessionCode.trim()) ? "redirect:/password/renew" : "redirect:/login";
    }

    //-----------------------------------------------RENEW PASSWORD------------------------------------------------------------//

    @Override
    public String renewPass(RenewPasswordRequest request, Model model, HttpSession session,  RedirectAttributes redirectAttributes) {
        request.setEmail(session.getAttribute("mail").toString());
        Object output = renewPassLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, RenewPasswordResponse.class)) {
            session.removeAttribute("mail");
            redirectAttributes.addFlashAttribute("msg", (RenewPasswordResponse) output);
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error",  output);
        return "redirect:/password/renew";
    }

    private Object renewPassLogic(RenewPasswordRequest request) {

        Map<String, String> error = RenewPasswordValidation.validate(request, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }

        Account acc = accountRepo.findByEmail(request.getEmail()).orElse(null);
        if (acc != null && request.getPassword().equals(request.getConfirmPassword())) {
            acc.setPassword(request.getPassword());
            accountRepo.save(acc);
        }
        return RenewPasswordResponse.builder()
                .status("200")
                .message("Renew password successfully")
                .build();
    }

    //-------------------------------------------VIEW FLOWER LIST---------------------------------------//
    @Override
    public String viewFlowerList(HttpSession session, Model model) {
        ViewFlowerListResponse output = viewFlowerListLogic();
        model.addAttribute("msg", output);
        return "category";
    }

    private ViewFlowerListResponse viewFlowerListLogic() {
        List<Flower> flowers = flowerRepo.findByStatus(Status.FLOWER_STATUS_AVAILABLE);
        // if find -> print size of flower
        return ViewFlowerListResponse.builder()
                .status("200")
                .keyword("")
                .flowerList(viewFlowerList(flowers))
                .build();
    }

    private List<ViewFlowerListResponse.Flower> viewFlowerList(List<Flower> flowers) {
        return flowers.stream()
                .map(item -> ViewFlowerListResponse.Flower.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .price(item.getPrice())
                        .description(item.getDescription())
                        .quantity(item.getQuantity())
                        .soldQuantity(item.getSoldQuantity())
                        .images(viewImageList(item.getFlowerImageList().stream().map(FlowerImage::getLink).toList()))
                        .build()
                ).toList();
    }

    private List<ViewFlowerListResponse.Image> viewImageList(List<String> imageList) {
        return imageList.stream()
                .map(img -> ViewFlowerListResponse.Image.builder()
                        .link(img)
                        .build())
                .toList();
    }

    //-------------------------------------------VIEW BUYER SLIDE BAR---------------------------------------//

    @Override
    public void viewSlideBar(Model model) {
        model.addAttribute("msg1", viewSlideBarLogic());
    }

    private ViewSlideBarResponse viewSlideBarLogic() {
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

    //--------------------------------------VIEW ORDER HISTORY------------------------------------------//

    @Override
    public String viewOrderHistory(HttpSession session, Model model) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute(MapConfig.buildMapKey(error,"Please login a buyer account to do this action"));
            return "redirect:/login";
        }
        Object output = viewOrderHistoryLogic(account.getId());
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderHistoryResponse.class)) {
            model.addAttribute("msg", (ViewOrderHistoryResponse) output);
            return "viewOrderHistory";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderHistory";
    }

    private Object viewOrderHistoryLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);

        List<Order> orderList = orderRepo.findAllByUser_Id(account.getUser().getId());

        Map<String, String> error = ViewOrderHistoryValidation.orderHistoryValidation(account, orderList);
        if (!error.isEmpty()) {
            return error;
        }

        if (!orderList.isEmpty()) {
            List<ViewOrderHistoryResponse.Order> orders = orderList.stream()
                    .map(this::viewOrderList)
                    .sorted(Comparator.comparing(ViewOrderHistoryResponse.Order::getCreateDate).reversed())
                    .collect(Collectors.toList());

            // Trả về kết quả
            return ViewOrderHistoryResponse.builder()
                    .status("200")
                    .message("Orders found")
                    .orderList(orders)
                    .build();
        }

        // Trả về thông báo không có đơn hàng
        return ViewOrderHistoryResponse.builder()
                .status("404")
                .message("No orders found for User")
                .build();
    }

    private ViewOrderHistoryResponse.Order viewOrderList(Order order) {
        String sellerName = order.getOrderDetailList().stream()
                .findFirst()
                .map(detail -> detail.getFlower().getSeller().getUser().getName())
                .orElse("Unknown Seller");

        return ViewOrderHistoryResponse.Order.builder()
                .orderId(order.getId())
                .sellerName(sellerName)
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createDate(order.getCreatedDate().toLocalDate())
                .detailList(viewOrderDetailList(order.getOrderDetailList()))
                .build();
    }

    private List<ViewOrderHistoryResponse.Detail> viewOrderDetailList(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> ViewOrderHistoryResponse.Detail.builder()
                        .image(detail.getFlower().getFlowerImageList().stream()
                                .findFirst()
                                .map(FlowerImage::getLink)
                                .orElse("Unknown Image"))
                        .description(detail.getFlower().getDescription())
                        .flowerName(detail.getFlowerName())
                        .quantity(detail.getQuantity())
                        .price(detail.getPrice() * detail.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    //-------------------------------VIEW ORDER DETAIL--------------------------------------//

    @Override
    public String viewOrderDetail(ViewOrderDetailRequest request, HttpSession session, Model model) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute(MapConfig.buildMapKey(error,"Please login a buyer account to do this action"));
            return "redirect:/login";
        }
        Object output = viewOrderDetailLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewOrderDetailResponse.class)) {
            model.addAttribute("msg", (ViewOrderDetailResponse) output);
            return "viewOrderStatusDetail";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "viewOrderStatusDetail";
    }

    private Object viewOrderDetailLogic(ViewOrderDetailRequest request) {
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        List<ViewOrderDetailResponse.Detail> detailList = viewOrderDetailLists(order.getOrderDetailList());

        String sellerName = order.getOrderDetailList().stream()
                .findFirst()
                .map(detail -> detail.getFlower().getSeller().getUser().getName())
                .orElse("Unknown Seller");

        return ViewOrderDetailResponse.builder()
                .status("200")
                .message("Order details retrieved successfully")
                .sellerId(order.getOrderDetailList().stream()
                        .findFirst()
                        .map(detail -> detail.getFlower().getSeller().getId())
                        .orElse(0))
                .orderId(order.getId())
                .sellerName(sellerName)
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getStatus())
                .paymentMethod(order.getPaymentMethod().getName())
                .detailList(detailList)
                .isFeedback(order.isFeedback())
                .build();
    }

    private List<ViewOrderDetailResponse.Detail> viewOrderDetailLists(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(detail -> {
                    List<String> categories = detail.getFlower().getFlowerCategoryList().stream()
                            .map(flowerCategory -> flowerCategory.getCategory().getName())
                            .collect(Collectors.toList());

                    return ViewOrderDetailResponse.Detail.builder()
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

    //--------------------------------------VIEW SELLER TOP LIST------------------------------------------//

    @Override
    public void viewSellerTopList(int top, Model model) {
        model.addAttribute("msg2", viewSellerTopListLogic(top));
    }

    public ViewSellerTopListResponse viewSellerTopListLogic(int top) {
        List<ViewSellerTopListResponse.Seller> sellers = sellerRepo.findAll()
                .stream()
                .map(seller -> {
                    List<Feedback> feedbackList = seller.getFeedbackList();
                    double averageRating = feedbackList != null && !feedbackList.isEmpty()
                            ? feedbackList.stream().mapToDouble(Feedback::getRating).average().orElse(0.0)
                            : 0.0;

                    return ViewSellerTopListResponse.Seller.builder()
                            .id(seller.getId())
                            .name(seller.getUser().getName())
                            .avatar(seller.getUser().getAvatar())
                            .averageRating(averageRating)
                            .build();
                })
                .sorted((s1, s2) -> Double.compare(s2.getAverageRating(), s1.getAverageRating()))
                .limit(top)
                .collect(Collectors.toList());

        return ViewSellerTopListResponse.builder()
                .status("200")
                .message("Top Sellers by Rating")
                .sellerList(sellers)
                .build();
    }

    //--------------------------------------SEARCH FLOWER------------------------------------------//

    @Override
    public String searchFlower(SearchFlowerRequest request, Model model) {
        model.addAttribute("msg", searchFlowerLogic(request));
        return "category";
    }

    private SearchFlowerResponse searchFlowerLogic(SearchFlowerRequest request) {
        return SearchFlowerResponse.builder()
                .status("200")
                .message("")
                .keyword(request.getKeyword())
                .flowerList(
                        flowerRepo.findAll()
                                .stream()
                                .filter(flower -> !flower.getStatus().equals(Status.FLOWER_STATUS_OUT_OF_STOCK))
                                .filter(flower -> flower.getName().toUpperCase().contains(request.getKeyword().toUpperCase()))
                                .map(
                                        flower -> SearchFlowerResponse.Flower.builder()
                                                .id(flower.getId())
                                                .name(flower.getName())
                                                .price(flower.getPrice())
                                                .description(flower.getDescription())
                                                .quantity(flower.getQuantity())
                                                .soldQuantity(flower.getSoldQuantity())
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

    //--------------------------------------------VIEW FLOWER DETAIL---------------------------------------------//

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

    public Object viewFlowerDetailLogic(ViewFlowerDetailRequest request) {
        Map<String, String> error = ViewFlowerDetailValidation.validate(request, flowerRepo);
        if (error.isEmpty()) {
            Flower flower = flowerRepo.findById(request.getId()).orElse(null);
            assert flower != null;
            return ViewFlowerDetailResponse.builder()
                    .status("200")
                    .message("")
                    .flower(
                            ViewFlowerDetailResponse.Flower.builder()
                                    .id(flower.getId())
                                    .name(flower.getName())
                                    .price(flower.getPrice())
                                    .quantity(flower.getQuantity())
                                    .flowerAmount(flower.getFlowerAmount())
                                    .description(flower.getDescription())

                                    .seller(ViewFlowerDetailResponse.Seller.builder()
                                            .id(flower.getSeller().getId())
                                            .name(flower.getSeller().getUser().getName())
                                            .email(flower.getSeller().getUser().getAccount().getEmail())
                                            .phone(flower.getSeller().getUser().getPhone())
                                            .avatar(flower.getSeller().getUser().getAvatar())
                                            .build())
                                    .categoryList(flower.getFlowerCategoryList().stream().map(
                                                            category -> ViewFlowerDetailResponse.Category.builder()
                                                                    .id(category.getCategory().getId())
                                                                    .name(category.getCategory().getName())
                                                                    .build()
                                                    )

                                                    .toList()
                                    )
                                    .imageList(flower.getFlowerImageList().stream()
                                            .map(
                                                    flowers -> ViewFlowerDetailResponse.Image.builder()
                                                            .link(flowers.getLink())
                                                            .build()
                                            )
                                            .toList())
                                    .build()
                    ).build();

        }

        return error;
    }

    //-----------------------------------VIEW ORDER STATUS-------------------------------------------//

    @Override
    public String viewOrderStatus(HttpSession session, Model model,  RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "You are not logged in");
            return "redirect:/login";
        }

        int orderId = (int) session.getAttribute("orderId");
        ViewOrderStatusResponse statusResponse = viewOrderStatusLogic(orderId);

        if (statusResponse != null) {
            model.addAttribute("msg", statusResponse.getOrderStatus());
            return "viewOrderStatusDetail";
        } else {
            model.addAttribute("error", "Order not found");
            return "errorPage";
        }
    }

    private ViewOrderStatusResponse viewOrderStatusLogic(int orderId) {
        Order order = orderRepo.findById(orderId).orElse(null);
        if (order != null) {
            return ViewOrderStatusResponse.builder()
                    .status("200")
                    .message("View Order Status successful")
                    .orderStatus(order.getStatus())
                    .isFeedback(order.isFeedback())
                    .build();
        }
        return ViewOrderStatusResponse.builder()
                .status("Order not found")
                .build();
    }

    //-----------------------------------------------CANCEL ORDER--------------------------------------------------//

    @Override
    public String cancelOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes) {
        String referer = httpServletRequest.getHeader("Referer");
        Object output = cancelOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelOrderResponse.class)) {
            model.addAttribute("msg", (CancelOrderResponse) output);
            return "redirect:" + referer;
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "redirect:/buyer/order/detail";
    }

    private Object cancelOrderLogic(CancelOrderRequest request) {
        Map<String, String> error = CancelOrderValidation.validate(request, orderRepo, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, Status.ORDER_STATUS_CANCELLED, orderRepo);
        sendCancelOrderEmail(order, order.getOrderDetailList().get(0).getFlower().getSeller());

        return CancelOrderResponse.builder()
                .status("200")
                .message("Cancel order successfully")
                .build();
    }

    private void sendCancelOrderEmail(Order order, Seller seller) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vannhuquynhp@gmail.com");

            helper.setTo(seller.getUser().getAccount().getEmail());

            helper.setSubject(Const.EMAIL_SUBJECT_ORDER);

            String emailContent = FileReaderUtil.readFile1(order);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------CONFIRM ORDER------------------------------------------//

    @Override
    public String confirmOrder(CancelOrderRequest request, HttpSession session, Model model, HttpServletRequest httpServletRequest,  RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            model.addAttribute("error", CancelOrderResponse.builder()
                    .status("400")
                    .message("Please login a buyer account to do this action")
                    .build());
            return "redirect:/login";
        }
        String referer = httpServletRequest.getHeader("Referer");
        Object output = confirmOrderLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CancelOrderResponse.class)) {
            model.addAttribute("msg", (CancelOrderResponse) output);
            return "redirect:" + referer;
        }
        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "redirect:/buyer/order/detail";
    }

    private Object confirmOrderLogic(CancelOrderRequest request) {
        Map<String, String> error = CancelOrderValidation.validate(request, orderRepo, accountRepo);
        if (!error.isEmpty()) {
            return error;
        }
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;
        Status.changeOrderStatus(order, Status.ORDER_STATUS_COMPLETED, orderRepo);

        return CancelOrderResponse.builder()
                .status("200")
                .message("Confirm order successfully")
                .build();
    }

    //--------------------------------VIEW CATEGORY------------------------------------------//

    @Override
    public void viewCategory(Model model) {
        model.addAttribute("msg3", viewCategoryLogic());
    }

    private ViewCategoryListResponse viewCategoryLogic() {
        return ViewCategoryListResponse.builder()
                .status("200")
                .message("")
                .categoryList(getCategoryList(categoryRepo.findAll()))
                .build();
    }

    private List<ViewCategoryListResponse.Category> getCategoryList(List<Category> categories) {
        return categories.stream()
                .map(cate ->
                        ViewCategoryListResponse.Category.builder()
                                .id(cate.getId())
                                .name(cate.getName())
                                .build())
                .toList();

    }

    //--------------------------------VN Pay------------------------------------------//

    @Override
    public String createVNPayPaymentLink(VNPayRequest request, Model model, HttpServletRequest httpServletRequest) {
        VNPayResponse vnPayResponse = createVNPayPaymentLinkLogic(request, httpServletRequest);
        model.addAttribute("msg", vnPayResponse);
        return "redirect:" + vnPayResponse.getPaymentURL();
    }

    private VNPayResponse createVNPayPaymentLinkLogic(VNPayRequest request, HttpServletRequest httpServletRequest) {
        Map<String, String> paramList = new HashMap<>();

        long amount = getAmount(request);
        String txnRef = VNPayConfig.getRandomNumber(8);
        String ipAddress = VNPayConfig.getIpAddress(httpServletRequest);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        paramList.put("vnp_Version", getVersion());
        paramList.put("vnp_Command", getCommand());
        paramList.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        paramList.put("vnp_Amount", String.valueOf(amount));
        paramList.put("vnp_CurrCode", "VND");
        paramList.put("vnp_TxnRef", txnRef);
        paramList.put("vnp_OrderInfo", "Order payment No " + txnRef + ", Amount: " + amount);
        paramList.put("vnp_OrderType", getOrderType());
        paramList.put("vnp_Locale", "vn");
        paramList.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
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

    private Long getAmount(VNPayRequest request) {
        return (long) request.getAmount() * 100 * 25000;
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
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------GET PAYMENT RESULT-------------------------------------//

    @Override
    public String getPaymentResult(Map<String, String> params, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        Object output = getPaymentResultLogic(params, account.getId(), httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            model.addAttribute("msg", (VNPayResponse) output);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            return ((VNPayResponse) output).getPaymentURL();
        }
        model.addAttribute("error", (Map<String, String>) output);
        return ((VNPayResponse) output).getPaymentURL();
    }

    private Object getPaymentResultLogic(Map<String, String> params, int accountId, HttpServletRequest httpServletRequest) {
        User user = Role.getCurrentLoggedAccount(accountId, accountRepo).getUser();
        Map<String, String> error = VNPayValidation.validate(params, httpServletRequest);
        if (!error.isEmpty()) {
            return error;
        }
        String transactionStatus = params.get("vnp_TransactionStatus");
        if ("00".equals(transactionStatus)) {
            List<WishlistItem> items = wishlistItemRepo.findAllByWishlist_User_Id(user.getId());
            saveOrder(params, user, items, paymentMethodRepo.findById(2).orElse(null));
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

    private void saveOrder(Map<String, String> params, User user, List<WishlistItem> items, PaymentMethod paymentMethod) {

        Map<Seller, List<WishlistItem>> itemsBySeller = items.stream()
                .collect(Collectors.groupingBy(item -> item.getFlower().getSeller()));

        boolean sendEmailResult = false;

        for (Map.Entry<Seller, List<WishlistItem>> entry : itemsBySeller.entrySet()) {
            Seller seller = entry.getKey();
            List<WishlistItem> sellerItems = entry.getValue();

            float totalPrice = 0;

            for (WishlistItem item : sellerItems) {
                totalPrice += item.getFlower().getPrice() * item.getQuantity();
            }

            Order savedOrder = orderRepo.save(Order.builder()
                    .user(user)
                    .buyerName(user.getName())
                    .createdDate(LocalDateTime.now())
                    .totalPrice(totalPrice)
                    .status(Status.ORDER_STATUS_PROCESSING)
                    .paymentMethod(paymentMethod)
                    .build());

            List<OrderDetail> orderDetails = new ArrayList<>();
            for (WishlistItem item : entry.getValue()) {
                Flower flower = item.getFlower();
                flower.setSoldQuantity(flower.getSoldQuantity() + item.getQuantity());
                flower.setQuantity(flower.getQuantity() - item.getQuantity());

                OrderDetail orderDetail = OrderDetail.builder()
                        .order(savedOrder)
                        .flower(item.getFlower())
                        .flowerName(item.getFlower().getName())
                        .quantity(item.getQuantity())
                        .price(item.getFlower().getPrice())
                        .build();
                orderDetailRepo.save(orderDetail);
                orderDetails.add(orderDetail);
                flowerRepo.save(flower);
            }
            savedOrder.setOrderDetailList(orderDetails);
            orderRepo.save(savedOrder);
            sendEmailResult = sendOrderEmail(savedOrder, user);
            sendEmailResult = sendOrderSellerEmail(savedOrder, seller);
        }
        if(sendEmailResult) {
            wishlistItemRepo.deleteAll(items);
            user.getWishlist().setWishlistItemList(new ArrayList<>());
            userRepo.save(user);
        }
    }

    private boolean sendOrderEmail(Order order, User user) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vannhuquynhp@gmail.com");

            helper.setTo(user.getAccount().getEmail());

            helper.setSubject(Const.EMAIL_SUBJECT_ORDER);

            String emailContent = FileReaderUtil.readFile(order, user);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    private boolean sendOrderSellerEmail(Order order, Seller seller) {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("vannhuquynhp@gmail.com");

            helper.setTo(seller.getUser().getAccount().getEmail());

            helper.setSubject(Const.EMAIL_SUBJECT_ORDER);

            String emailContent = FileReaderUtil.readFile(order);

            helper.setText(emailContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    //-----------------------------------GET COD PAYMENT RESULT--------------------------------------//

    @Override
    public String getCODPaymentResult(Map<String, String> params, HttpSession session, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        User user = account.getUser();
        List<WishlistItem> items = wishlistItemRepo.findAllByWishlist_User_Id(user.getId());
        saveOrder(params, user, items, paymentMethodRepo.findById(1).orElse(null));
        session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
        CODPaymentResponse response = CODPaymentResponse.builder()
                .status("200")
                .message("Your order has been preparing...")
                .build();
        redirectAttributes.addFlashAttribute("msg", response);
        return "redirect:/viewOrderSummary";
    }

    //---------------------------------CHECK OUT---------------------------------//

    @Override
    public String confirmCheckoutOrder(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        model.addAttribute("msg", viewWishlistLogic(account.getId()));
        return "checkout";
    }

    //----------------------CREATE PAYMENT LINK FOR BUY NOW------------------------//


    @Override
    public String createVNPayPaymentLinkForBuyNow(VNPayRequest request, Model model, HttpServletRequest httpServletRequest, HttpSession session) {
        VNPayResponse vnPayResponse = createVNPayPaymentLinkForBuyNowLogic(request, httpServletRequest);
        session.setAttribute("flowerId", request.getFlowerId());
        session.setAttribute("quantity", request.getQuantity());
        model.addAttribute("msg", vnPayResponse);
        return "redirect:" + vnPayResponse.getPaymentURL();
    }

    private VNPayResponse createVNPayPaymentLinkForBuyNowLogic(VNPayRequest request, HttpServletRequest httpServletRequest) {
        Map<String, String> paramList = new HashMap<>();

        long amount = getAmount(request);
        String txnRef = VNPayConfig.getRandomNumber(8);
        String ipAddress = VNPayConfig.getIpAddress(httpServletRequest);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        paramList.put("vnp_Version", getVersion());
        paramList.put("vnp_Command", getCommand());
        paramList.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        paramList.put("vnp_Amount", String.valueOf(amount));
        paramList.put("vnp_CurrCode", "VND");
        paramList.put("vnp_TxnRef", txnRef);
        paramList.put("vnp_OrderInfo", "Order payment No " + txnRef + ", Amount: " + amount);
        paramList.put("vnp_OrderType", getOrderType());
        paramList.put("vnp_Locale", "vn");
        paramList.put("vnp_ReturnUrl", BuyNowVNPAYConfig.vnp_ReturnUrl);
        paramList.put("vnp_IpAddr", ipAddress);
        paramList.put("vnp_CreateDate", getCreateDate(calendar, formatter));
        paramList.put("vnp_ExpireDate", getExpiredDate(15, calendar, formatter));

        return VNPayResponse.builder()
                .status("200")
                .message("Create payment link successfully")
                .paymentURL(buildVNPayLinkForBuyNow(paramList))
                .build();
    }

    private String buildVNPayLinkForBuyNow(Map<String, String> paramList) {
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
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            return BuyNowVNPAYConfig.vnp_PayUrl + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------GET PAYMENT RESULT FOR BUY NOW-------------------------------------//

    @Override
    public String getPaymentResultForBuyNow(Map<String, String> params, BuyNowCODPayMentRequest request, HttpServletRequest httpServletRequest, Model model, HttpSession session) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        Object output = getPaymentResultForBuyNowLogic(params, Integer.parseInt(session.getAttribute("flowerId").toString()) , Integer.parseInt(session.getAttribute("quantity").toString()), account.getId(), httpServletRequest);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, VNPayResponse.class)) {
            model.addAttribute("msg", (VNPayResponse) output);
            Account account1 = accountRepo.findById(account.getId()).orElse(null);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            session.removeAttribute("flowerId");
            session.removeAttribute("quantity");
            return ((VNPayResponse) output).getPaymentURL();
        }
        model.addAttribute("error", (Map<String, String>) output);
        return ((VNPayResponse) output).getPaymentURL();
    }

    private Object getPaymentResultForBuyNowLogic(Map<String, String> params, int flowerId, int quantity, int accountId, HttpServletRequest httpServletRequest) {
        User user = Role.getCurrentLoggedAccount(accountId, accountRepo).getUser();
        Map<String, String> error = VNPayValidation.validate(params, httpServletRequest);
        if (!error.isEmpty()) {
            return error;
        }
        String transactionStatus = params.get("vnp_TransactionStatus");
        if ("00".equals(transactionStatus)) {
            saveOrderNow(flowerId, quantity, user, paymentMethodRepo.findById(2).orElse(null));
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

    private void saveOrderNow(int flowerId, int quantity, User user, PaymentMethod paymentMethod) {
        Flower flower = flowerRepo.findById(flowerId).orElse(null);
        assert flower != null;

        float totalPrice = flower.getPrice() * quantity;
        Order savedOrder = orderRepo.save(Order.builder()
                .user(user)
                .buyerName(user.getName())
                .createdDate(LocalDateTime.now())
                .totalPrice(totalPrice)
                .status(Status.ORDER_STATUS_PROCESSING)
                .paymentMethod(paymentMethod)
                .build());

        flower.setSoldQuantity(flower.getSoldQuantity() + quantity);
        flower.setQuantity(flower.getQuantity() - quantity);

        WishlistItem item = wishlistItemRepo.findByFlower_IdAndWishlist_User_Id(flowerId, user.getId()).orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() - quantity);
            wishlistItemRepo.save(item);
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = OrderDetail.builder()
                .order(savedOrder)
                .flower(flower)
                .flowerName(flower.getName())
                .quantity(quantity)
                .price(flower.getPrice())
                .build();

        orderDetailRepo.save(orderDetail);
        orderDetails.add(orderDetail);
        savedOrder.setOrderDetailList(orderDetails);
        orderRepo.save(savedOrder);

        flowerRepo.save(flower);
        sendOrderEmail(savedOrder, user);
        sendOrderSellerEmail(savedOrder, flower.getSeller());
    }

    //-----------------------------------GET COD PAYMENT RESULT--------------------------------------//

    @Override
    public String getCODPaymentResultForBuyNow(VNPayRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Account account = Role.getCurrentLoggedAccount(session);
        assert account != null;
        User user = account.getUser();
        saveOrderNow(request.getFlowerId(), request.getQuantity(), user, paymentMethodRepo.findById(1).orElse(null));
        session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
        CODPaymentResponse response = CODPaymentResponse.builder()
                .status("200")
                .message("Your order has been preparing...")
                .build();
        redirectAttributes.addFlashAttribute("msg", response);
        return "redirect:/viewOrderSummary";
    }

    //---------------------------------BUY NOW---------------------------------//

    @Override
    public String buyNow(ConfirmOrderRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You must log in");
            return "redirect:/login";
        }
        ViewConfirmNowResponse response = viewConfirmOrderNow(account.getId(), request.getFlowerId(), request.getQuantity());
        model.addAttribute("msg1", response);
        return "checkout";
    }

    private ViewConfirmNowResponse viewConfirmOrderNow(int accountId, int flowerId, int quantity) {

        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);

        Flower flower = flowerRepo.findById(flowerId).orElse(null);
        assert flower != null;

        float totalPrice = flower.getPrice() * quantity;

        return ViewConfirmNowResponse.builder()
                .status("200")
                .message("View Confirm Order Now successful")
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .totalPrice(totalPrice)
                .flower(
                        ViewConfirmNowResponse.FlowerInfo.builder()
                                .id(flower.getId())
                                .imgList(flower.getFlowerImageList().stream()
                                        .map(img -> ViewFlowerListResponse.Image.builder()
                                                .link(img.getLink())
                                                .build())
                                        .toList()
                                )
                                .name(flower.getName())
                                .price(flower.getPrice())
                                .quantity(quantity)
                                .description(flower.getDescription())
                                .stockQuantity(flower.getQuantity())
                                .build()
                )
                .build();
    }

    //---------------------------------FILTER CATEGORY---------------------------------//

    @Override
    public String filterCategory(FilterCategoryRequest request, RedirectAttributes redirectAttributes) {

//        // Kiểm tra điều kiện categoryId không hợp lệ hoặc null
//        model.addAttribute("msg", filterCategoryLogic(request));
        redirectAttributes.addFlashAttribute("msg", filterCategoryLogic(request));
        return "redirect:/category";
    }

    public FilterCategoryResponse filterCategoryLogic(FilterCategoryRequest request) {

        Category category = categoryRepo.findById(request.getCategoryId()).orElse(null);
        assert category != null;

        return FilterCategoryResponse.builder()
                .status("200")
                .message("")
                .categoryId(request.getCategoryId())
                .keyword(category.getName())
                .flowerList(
                        category.getFlowerCategoryList().stream()
                                .filter(flower -> flower.getFlower().getStatus().equals(Status.FLOWER_STATUS_AVAILABLE))
                                .map(flower -> FilterCategoryResponse.Flower.builder()
                                        .id(flower.getFlower().getId())
                                        .name(flower.getFlower().getName())
                                        .description(flower.getFlower().getDescription())
                                        .price(flower.getFlower().getPrice())
                                        .quantity(flower.getFlower().getQuantity())
                                        .soldQuantity(flower.getFlower().getSoldQuantity())
                                        .images(
                                                flower.getFlower().getFlowerImageList().stream()
                                                        .map(img -> FilterCategoryResponse.Image.builder()
                                                                .link(img.getLink())
                                                                .build())
                                                        .toList()
                                        )
                                        .build())
                                .toList()
                )
                .build();
    }

    //---------------------------------------VIEW FEEDBACK---------------------------------------//

    @Override
    public String viewFeedback(int sellerId, Model model, HttpSession session) {
        Object output = viewFeedbackLogic(sellerId);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFeedbackResponse.class)) {
            model.addAttribute("msg", (ViewFeedbackResponse) output);
            return "sellerInfo";
        }

        model.addAttribute("error", (Map<String, String>) output);
        return "sellerInfo";
    }

    private Object viewFeedbackLogic(int sellerId) {
        List<Flower> flowers = flowerRepo.findAllBySeller_Id(sellerId);
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
                .flowerList(viewFlowerList(flowers))
                .feedbackList(feedbackDetails)
                .build();
    }

    private ViewFeedbackResponse.FeedbackDetail mapToFeedbackDetail(Feedback feedback) {
        return ViewFeedbackResponse.FeedbackDetail.builder()
                .id(feedback.getId())
                .name(feedback.getUser().getName())
                .avatar(feedback.getUser().getAvatar())
                .content(feedback.getContent())
                .rating(feedback.getRating())
                .createDate(feedback.getCreateDate().toLocalDate())
                .build();
    }


    //---------------------------------CREATE FEEDBACK---------------------------------//

    @Override
    public String createFeedback(CreateFeedbackRequest request, HttpSession session, Model model, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        Map<String, String> error = new HashMap<>();
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null || !Role.checkIfThisAccountIsBuyer(account)) {
            MapConfig.buildMapKey(error, "Please login with a buyer account to leave feedback");
            return "redirect:/login";
        }
        Object output = createFeedbackLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFeedbackResponse.class)) {
            model.addAttribute("msg1", (CreateFeedbackResponse) output);
            session.setAttribute("acc", accountRepo.findById(account.getId()).orElse(null));
            return "redirect:" + httpServletRequest.getHeader("Referer");
        }

        redirectAttributes.addFlashAttribute("error", (Map<String, String>) output);
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @Override
    public CreateFeedbackResponse createFeedback(CreateFeedbackRequest request) {
        Object output = createFeedbackLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, CreateFeedbackResponse.class)) {
            return (CreateFeedbackResponse) output;
        }

        return CreateFeedbackResponse.builder()
                .status("400")
                .message("You have already left feedback for this order")
                .build();
    }

    private Object createFeedbackLogic(CreateFeedbackRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        Map<String, String> error = CreateFeedbackValidation.validate(request, orderRepo, account.getUser());

        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;

        boolean existFeedback = feedbackRepo.existsByOrder_IdAndSeller_Id(request.getOrderId(), request.getSellerId());

        if (order.isFeedback() || existFeedback) {
            return ViewFeedbackResponse.builder()
                    .status("400")
                    .message("You have already left feedback for this order")
                    .build();
        }

        if (error.isEmpty()) {
            Feedback feedback = createNewFeedback(request);
            calculateSellerRating(feedback.getSeller());
            return CreateFeedbackResponse.builder()
                    .status("200")
                    .message("Feedback submitted successfully")
                    .feedback(
                            CreateFeedbackResponse.FeedbackInfo.builder()
                                    .id(feedback.getId())
                                    .buyerName(feedback.getUser().getName())
                                    .content(feedback.getContent())
                                    .rating(feedback.getRating())
                                    .createDate(feedback.getCreateDate())
                                    .build()
                    )
                    .build();
        }
        return error;
    }

    private Feedback createNewFeedback(CreateFeedbackRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        assert account != null;
        Seller seller = sellerRepo.findById(request.getSellerId()).orElse(null);
        assert seller != null;
        Order order = orderRepo.findById(request.getOrderId()).orElse(null);
        assert order != null;

        Feedback feedback = Feedback.builder()
                .user(account.getUser())
                .seller(seller)
                .order(order)
                .content(request.getContent())
                .rating(request.getRating())
                .createDate(LocalDateTime.now())
                .build();

        //set isFeedback = true
        order.setFeedback(true);
        orderRepo.save(order);

        return feedbackRepo.save(feedback);

    }

    private void calculateSellerRating(Seller seller) {
        List<Feedback> feedbackList = seller.getFeedbackList();

        if (feedbackList == null || feedbackList.isEmpty()) {
            seller.setRating(0);
        } else {
            double averageRating = feedbackList.stream()
                    .mapToInt(Feedback::getRating)
                    .average()
                    .orElse(0.0);
            seller.setRating((float) averageRating);
        }

        sellerRepo.save(seller);
    }

    //---------------------------------SCHEDULE---------------------------------//

    @Scheduled(cron = "0 0 7 * * ?") // Runs daily at 8 AM
    public void sendWitheringNotifications() {
        List<WishlistItem> wishlistItems = wishlistItemRepo.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (WishlistItem item : wishlistItems) {
            Flower flower = item.getFlower();
            Wishlist wishlist = item.getWishlist();
            User user = wishlist.getUser();

        }
    }


}

