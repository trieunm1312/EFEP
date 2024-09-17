package com.team1.efep.service_implementors;

import com.team1.efep.enums.Role;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.models.request_models.AddToCartRequest;
import com.team1.efep.models.request_models.DeleteCartItemRequest;
import com.team1.efep.models.request_models.ForgotRequest;
import com.team1.efep.models.request_models.RenewPasswordRequest;
import com.team1.efep.models.response_models.*;
import com.team1.efep.repositories.*;
import com.team1.efep.services.BuyerService;
import com.team1.efep.utils.ConvertMapIntoStringUtil;
import com.team1.efep.utils.FileReaderUtil;
import com.team1.efep.utils.OTPGeneratorUtil;
import com.team1.efep.utils.OutputCheckerUtil;
import com.team1.efep.validations.AddToCartValidation;
import com.team1.efep.validations.DeleteCartItemValidation;
import com.team1.efep.validations.ViewFlowerListValidation;
import com.team1.efep.validations.ViewOrderHistoryValidation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements BuyerService {

    private final JavaMailSenderImpl mailSender;
    private final AccountRepo accountRepo;
    private final WishlistRepo wishlistRepo;
    private final FlowerRepo flowerRepo;
    private final WishlistItemRepo wishlistItemRepo;
    private final OrderRepo orderRepo;

    @Override
    public String sendEmail(ForgotRequest request, Model model) {
        return "";
    }


    //---------------------------------------VIEW CART------------------------------------------//
    @Override
    public String viewCart(HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        model.addAttribute("cart", viewCartItemList(account.getId()));
        return "viewCart";
    }

    @Override
    public ViewCartResponse viewCartAPI(int id) {
        Account account = Role.getCurrentLoggedAccount(id, accountRepo);
        if (account == null) {
            return ViewCartResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        return ViewCartResponse.builder()
                .status("200")
                .message("View cart successfully")
                .id(account.getUser().getWishlist().getId())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .cartItemList(viewCartItemList(account.getId()))
                .build();
    }

    private Wishlist viewCartLogic(int accountId) {
        Account account = Role.getCurrentLoggedAccount(accountId, accountRepo);
        assert account != null;
        return account.getUser().getWishlist();
    }

    private List<ViewCartResponse.CartItems> viewCartItemList(int accountId) {
        return viewCartLogic(accountId).getWishlistItemList().stream()
                .map(item -> new ViewCartResponse.CartItems(item.getId(), item.getFlower().getName(), item.getQuantity(), item.getFlower().getPrice()))
                .toList();
    }

    //-------------------------------------------------ADD TO CART-----------------------------------------------------//

    @Override
    public String addToCart(AddToCartRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = addToCartLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToCartResponse.class)) {
            model.addAttribute("msg", (AddToCartResponse) output);
            return "base";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "base";
    }

    @Override
    public AddToCartResponse addToCartAPI(AddToCartRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return AddToCartResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = addToCartLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, AddToCartResponse.class)) {
            return (AddToCartResponse) output;
        }
        return AddToCartResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }

    private Object addToCartLogic(AddToCartRequest request) {
        Map<String, String> errors = AddToCartValidation.validate(request);
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
        return AddToCartResponse.builder()
                .status("200")
                .message("Your flower has been added to cart successfully")
                .build();
    }

    private boolean checkExistedItem(AddToCartRequest request, Wishlist wishlist) {
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

    //-----------------------------------------------------------------------------------------------------------//
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

    @Override
    public String renewPass(RenewPasswordRequest request, Model model) {
        return "";
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
        Object output = viewFlowerListLogic();
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerListResponse.class)) {
            model.addAttribute("msg", (ViewFlowerListResponse) output);
            return "home";
        }
        model.addAttribute("error", ConvertMapIntoStringUtil.convert((Map<String, String>) output));
        return "home";
    }

    @Override
    public ViewFlowerListResponse viewFlowerListAPI() {
        Object output = viewFlowerListLogic();
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, ViewFlowerListResponse.class)) {
            return (ViewFlowerListResponse) output;
        }
        return ViewFlowerListResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object viewFlowerListLogic() {
        Map<String, String> errors = ViewFlowerListValidation.validate();
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
    public String deleteCartItem(DeleteCartItemRequest request, HttpSession session, Model model) {
        Account account = Role.getCurrentLoggedAccount(session);
        if (account == null) {
            model.addAttribute("error", "You are not logged in");
            return "redirect:/login";
        }
        Object output = deleteCartItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteCartItemResponse.class)) {
            model.addAttribute("msg", (DeleteCartItemResponse) output);
            return "viewCart";
        }
        model.addAttribute("error", (Map<String, String>) output);
        return "";
    }

    @Override
    public DeleteCartItemResponse deleteCartItemAPI(DeleteCartItemRequest request) {
        Account account = Role.getCurrentLoggedAccount(request.getAccountId(), accountRepo);
        if (account == null) {
            return DeleteCartItemResponse.builder()
                    .status("400")
                    .message("You are not logged in")
                    .build();
        }
        Object output = deleteCartItemLogic(request);
        if (OutputCheckerUtil.checkIfThisIsAResponseObject(output, DeleteCartItemResponse.class)) {
            return (DeleteCartItemResponse) output;
        }
        return DeleteCartItemResponse.builder()
                .status("400")
                .message(ConvertMapIntoStringUtil.convert((Map<String, String>) output))
                .build();
    }


    private Object deleteCartItemLogic(DeleteCartItemRequest request) {
        Account account = accountRepo.findById(request.getAccountId()).orElse(null);
        assert account != null;
        Map<String, String> errors = DeleteCartItemValidation.validate(request);
        if (!errors.isEmpty()) {
            return errors;
        }
        Wishlist wishlist = account.getUser().getWishlist();
        Optional<WishlistItem> cartItemOptional = wishlist.getWishlistItemList().stream()
                .filter(item -> Objects.equals(item.getId(), request.getCartItemId()))
                .findFirst();

        WishlistItem wishlistItem = cartItemOptional.get();
        wishlist.getWishlistItemList().remove(wishlistItem);
        wishlistItemRepo.delete(wishlistItem);

        accountRepo.save(account);
        return DeleteCartItemResponse.builder()
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
        return null;
    }

    private ViewOrderHistoryResponse.Order viewOrderList(Order order) {
        return ViewOrderHistoryResponse.Order.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getOrderStatus().getStatus())
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
}

