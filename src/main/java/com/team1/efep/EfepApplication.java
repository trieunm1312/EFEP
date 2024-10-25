package com.team1.efep;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.*;
import com.team1.efep.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class EfepApplication {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;
    private final WishlistRepo wishlistRepo;
    private final FlowerRepo flowerRepo;
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final WishlistItemRepo wishlistItemRepo;
    private final FlowerImageRepo flowerImageRepo;
    private final BusinessServiceRepo businessServiceRepo;
    private final BusinessPlanRepo businessPlanRepo;
    private final PlanServiceRepo planServiceRepo;
    private final CategoryRepo categoryRepo;
    private final FlowerCategoryRepo flowerCategoryRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final PurchasedPlanRepo purchasedPlanRepo;


    public static void main(String[] args) {
        SpringApplication.run(EfepApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                //init seller
                Seller seller1 = sellerRepo.save(
                        Seller.builder()
                                .businessPlan(null)
                                .user(
                                        userRepo.save(User.builder()
                                                .avatar("https://www.avatar.com")
                                                .background("https://www.background.com")
                                                .name("Seller1")
                                                .phone("0808080808")
                                                .account(
                                                        accountRepo.save(Account.builder()
                                                                .email("seller1@efep.com")
                                                                .status(Status.ACCOUNT_STATUS_ACTIVE)
                                                                .password("123")
                                                                .role(Role.SELLER)
                                                                .build())
                                                )
                                                .build())
                                )
                                .planPurchaseDate(null)
                                .build()
                );

                //init buyer
                User user1 = userRepo.save(
                        User.builder()
                                .account(
                                        accountRepo.save(Account.builder()
                                                .email("buyer1@efep.com")
                                                .status(Status.ACCOUNT_STATUS_ACTIVE)
                                                .password("123")
                                                .role(Role.BUYER)
                                                .build())
                                )
                                .name("buyer1")
                                .phone("0909090909")
                                .avatar("https://www.avatar.com")
                                .background("https://www.background.com")
                                .orderList(null)
                                .wishlist(null)
                                .seller(null)
                                .build()
                );

                //init admin
                User admin = userRepo.save(
                        User.builder()
                                .account(
                                        accountRepo.save(Account.builder()
                                                .email("admin@efep.com")
                                                .status(Status.ACCOUNT_STATUS_ACTIVE)
                                                .password("123")
                                                .role(Role.ADMIN)
                                                .build())
                                )
                                .name("admin")
                                .phone("0909090909")
                                .avatar("https://www.avatar.com")
                                .background("https://www.background.com")
                                .orderList(null)
                                .wishlist(null)
                                .seller(null)
                                .build()
                );

                //init wishlist
                Wishlist wishlist1 = wishlistRepo.save(
                        Wishlist.builder()
                                .user(user1)
                                .build()
                );


                //init flower
                Flower flower1 = flowerRepo.save(
                        Flower.builder()
                                .description("Red Rose")
                                .flowerAmount(10)
                                .name("Rose")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower2 = flowerRepo.save(
                        Flower.builder()
                                .description("Evelyn")
                                .flowerAmount(10)
                                .name("Evelyn")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower3 = flowerRepo.save(
                        Flower.builder()
                                .description("Lily")
                                .flowerAmount(10)
                                .name("Lily")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower4 = flowerRepo.save(
                        Flower.builder()
                                .description("Hibiscus")
                                .flowerAmount(10)
                                .name("Hibiscus")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower5 = flowerRepo.save(
                        Flower.builder()
                                .description("Daisy")
                                .flowerAmount(10)
                                .name("Daisy")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower6 = flowerRepo.save(
                        Flower.builder()
                                .description("Poppy")
                                .flowerAmount(10)
                                .name("Poppy")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower7 = flowerRepo.save(
                        Flower.builder()
                                .description("Lotus")
                                .flowerAmount(10)
                                .name("Lotus")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower8 = flowerRepo.save(
                        Flower.builder()
                                .description("Jasmine")
                                .flowerAmount(10)
                                .name("Jasmine")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower9 = flowerRepo.save(
                        Flower.builder()
                                .description("Violet")
                                .flowerAmount(10)
                                .name("Violet")
                                .price(100)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );

                Flower flower10 = flowerRepo.save(
                        Flower.builder()
                                .description("Sunflower")
                                .flowerAmount(10)
                                .name("Sunflower")
                                .price(20)
                                .quantity(10)
                                .seller(seller1)
                                .soldQuantity(0)
                                .status(Status.FLOWER_STATUS_AVAILABLE)
                                .flowerCategoryList(null)
                                .orderDetailList(null)
                                .wishlistItemList(null)
                                .build()
                );


                //init flower Image
                FlowerImage flowerImage1 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower1)
                                .link("https://iiflorist.com/wp-content/uploads/2023/08/12-redpink-rosegraduation-balloon-bouquet_iiflorist.jpg")
                                .build()
                );

                FlowerImage flowerImage2 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower1)
                                .link("https://iiflorist.com/wp-content/uploads/2023/08/12-redpink-rosegraduation-balloon-bouquet_iiflorist.jpg")
                                .build()
                );

                flower1.setFlowerImageList(List.of(flowerImage1, flowerImage2));

                FlowerImage flowerImage3 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower2)
                                .link("https://www.rayanesbhomes.com/wp-content/uploads/2024/01/White-Artificial-Tulip-Flower-Bouquet-for-Home-Decor-Wedding-Event-Party-Office-6.jpg")
                                .build()
                );

                FlowerImage flowerImage4 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower2)
                                .link("https://www.rayanesbhomes.com/wp-content/uploads/2024/01/White-Artificial-Tulip-Flower-Bouquet-for-Home-Decor-Wedding-Event-Party-Office-6.jpg")
                                .build()
                );

                flower2.setFlowerImageList(List.of(flowerImage3, flowerImage4));

                FlowerImage flowerImage5 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower3)
                                .link("https://50gram.com.my/wp-content/uploads/2022/04/Hand-Bouquet-Product-Page-2023-07-11T014520.034.png.webp")
                                .build()
                );

                FlowerImage flowerImage6 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower3)
                                .link("https://50gram.com.my/wp-content/uploads/2022/04/Hand-Bouquet-Product-Page-2023-07-11T014520.034.png.webp")
                                .build()
                );

                flower3.setFlowerImageList(List.of(flowerImage5, flowerImage6));

                FlowerImage flowerImage7 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower4)
                                .link("https://i.etsystatic.com/17286005/r/il/de821f/5737577164/il_570xN.5737577164_aj8b.jpg")
                                .build()
                );

                FlowerImage flowerImage8 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower4)
                                .link("https://i.etsystatic.com/17286005/r/il/de821f/5737577164/il_570xN.5737577164_aj8b.jpg")
                                .build()
                );

                flower4.setFlowerImageList(List.of(flowerImage7, flowerImage8));

                FlowerImage flowerImage9 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower5)
                                .link("https://cdn.shopify.com/s/files/1/0108/9460/6436/files/White_Daisies_1024x1024.jpg?v=1553192523")
                                .build()
                );

                FlowerImage flowerImage10 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower5)
                                .link("https://cdn.shopify.com/s/files/1/0108/9460/6436/files/White_Daisies_1024x1024.jpg?v=1553192523")
                                .build()
                );

                flower5.setFlowerImageList(List.of(flowerImage9, flowerImage10));

                FlowerImage flowerImage11 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower6)
                                .link("https://www.magicflowerseventrentals.com/wp-content/uploads/2023/05/IMG-6302.jpg")
                                .build()
                );

                FlowerImage flowerImage12 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower6)
                                .link("https://www.magicflowerseventrentals.com/wp-content/uploads/2023/05/IMG-6302.jpg")
                                .build()
                );

                flower6.setFlowerImageList(List.of(flowerImage11, flowerImage12));

                FlowerImage flowerImage13 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower7)
                                .link("https://i.etsystatic.com/6282450/r/il/857e11/1548374167/il_570xN.1548374167_iz1c.jpg")
                                .build()
                );

                FlowerImage flowerImage14 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower7)
                                .link("https://i.etsystatic.com/6282450/r/il/857e11/1548374167/il_570xN.1548374167_iz1c.jpg")
                                .build()
                );

                flower7.setFlowerImageList(List.of(flowerImage13, flowerImage14));

                FlowerImage flowerImage15 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower8)
                                .link("https://images.squarespace-cdn.com/content/v1/615186c62b534b697dcceacd/1632736845035-HKXNXSZ8GN2OPY06OX0C/homepage-1.jpg")
                                .build()
                );

                FlowerImage flowerImage16 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower8)
                                .link("https://images.squarespace-cdn.com/content/v1/615186c62b534b697dcceacd/1632736845035-HKXNXSZ8GN2OPY06OX0C/homepage-1.jpg")
                                .build()
                );

                flower8.setFlowerImageList(List.of(flowerImage15, flowerImage16));

                FlowerImage flowerImage17 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower9)
                                .link("https://hillsflowers.net/cdn/shop/files/W36-4709_LOL_preset_mol-mx-tile-wide-sv-new_b7db6120-c324-47b5-afb5-e2e2caf803c2.jpg")
                                .build()
                );

                FlowerImage flowerImage18 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower9)
                                .link("https://hillsflowers.net/cdn/shop/files/W36-4709_LOL_preset_mol-mx-tile-wide-sv-new_b7db6120-c324-47b5-afb5-e2e2caf803c2.jpg")
                                .build()
                );

                flower9.setFlowerImageList(List.of(flowerImage17, flowerImage18));

                FlowerImage flowerImage19 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower10)
                                .link("https://the-little-rustic-farm.com/cdn/shop/products/il_794xN.3887270497_2p0e_1024x1024@2x.jpg")
                                .build()
                );

                FlowerImage flowerImage20 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower10)
                                .link("https://the-little-rustic-farm.com/cdn/shop/products/il_794xN.3887270497_2p0e_1024x1024@2x.jpg")
                                .build()
                );

                flower10.setFlowerImageList(List.of(flowerImage19, flowerImage20));

                //init wishlist item
                WishlistItem wishlistItem1 = wishlistItemRepo.save(
                        WishlistItem.builder()
                                .flower(flower1)
                                .wishlist(wishlist1)
                                .quantity(1)
                                .build()
                );

                WishlistItem wishlistItem2 = wishlistItemRepo.save(
                        WishlistItem.builder()
                                .flower(flower2)
                                .wishlist(wishlist1)
                                .quantity(2)
                                .build()
                );

                wishlist1.setWishlistItemList(List.of(wishlistItem1, wishlistItem2));

                //init order
                Order order1 = orderRepo.save(
                        Order.builder()
                                .status(Status.ORDER_STATUS_PROCESSING)
                                .user(user1)
                                .buyerName(user1.getName())
                                .createdDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                                .totalPrice(200)
                                .build()
                );

                Order order2 = orderRepo.save(
                        Order.builder()
                                .status(Status.ORDER_STATUS_PROCESSING)
                                .user(user1)
                                .buyerName(user1.getName())
                                .createdDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                                .totalPrice(100)
                                .build()
                );

                Order order3 = orderRepo.save(
                        Order.builder()
                                .status(Status.ORDER_STATUS_PROCESSING)
                                .user(user1)
                                .buyerName(user1.getName())
                                .createdDate(LocalDateTime.of(2022, 1, 1, 0, 0))
                                .totalPrice(100)
                                .build()
                );

                //init order detail
                OrderDetail orderDetail1 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower1)
                                .flowerName(flower1.getName())
                                .price(flower1.getPrice())
                                .order(order1)
                                .quantity(1)
                                .build()
                );

                OrderDetail orderDetail2 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower2)
                                .flowerName(flower2.getName())
                                .price(flower2.getPrice())
                                .order(order1)
                                .quantity(1)
                                .build()
                );

                order1.setOrderDetailList(List.of(orderDetail1, orderDetail2));

                OrderDetail orderDetail3 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower1)
                                .flowerName(flower1.getName())
                                .price(flower1.getPrice())
                                .order(order2)
                                .quantity(1)
                                .build()
                );

                order2.setOrderDetailList(List.of(orderDetail3));

                OrderDetail orderDetail4 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower2)
                                .flowerName(flower2.getName())
                                .price(flower2.getPrice())
                                .order(order3)
                                .quantity(1)
                                .build()
                );

                order3.setOrderDetailList(List.of(orderDetail4));


                //init payment method
                PaymentMethod COD = PaymentMethod.builder()
                        .name("COD")
                        .build();

                PaymentMethod VNPay = PaymentMethod.builder()
                        .name("VNPay")
                        .build();

                paymentMethodRepo.save(COD);
                paymentMethodRepo.save(VNPay);

                order1.setPaymentMethod(COD);
                order2.setPaymentMethod(VNPay);
                order3.setPaymentMethod(COD);
                orderRepo.saveAll(List.of(order1, order2, order3));

                // init business service
                BusinessService service1 = businessServiceRepo.save(
                        BusinessService.builder()
                                .name("Service 1")
                                .description("Description of Service 1")
                                .price(100.0f)
                                .build()
                );

                BusinessService service2 = businessServiceRepo.save(
                        BusinessService.builder()
                                .name("Service 2")
                                .description("Description of Service 2")
                                .price(150.0f)
                                .build()
                );

                // init Business Plan
                BusinessPlan plan1 = businessPlanRepo.save(
                        BusinessPlan.builder()
                                .name("Plan 1")
                                .description("Description of Plan 1")
                                .price(500.0f)
                                .duration(12)
                                .status(Status.BUSINESS_PLAN_STATUS_ACTIVE)
                                .build()
                );

                BusinessPlan plan2 = businessPlanRepo.save(
                        BusinessPlan.builder()
                                .name("Plan 2")
                                .description("Description of Plan 2")
                                .price(300.0f)
                                .duration(6)
                                .status(Status.BUSINESS_PLAN_STATUS_ACTIVE)
                                .build()
                );

                // init Plan Service
                PlanService planService1 = planServiceRepo.save(
                        PlanService.builder()
                                .businessPlan(plan1)
                                .businessService(service1)
                                .build()
                );

                PlanService planService2 = planServiceRepo.save(
                        PlanService.builder()
                                .businessPlan(plan1)
                                .businessService(service2)
                                .build()
                );

                PlanService planService3 = planServiceRepo.save(
                        PlanService.builder()
                                .businessPlan(plan2)
                                .businessService(service1)
                                .build()
                );

                plan1.setPlanServiceList(List.of(planService1, planService2));
                plan2.setPlanServiceList(List.of(planService3));
                businessPlanRepo.saveAll(List.of(plan1, plan2));

                //init purchase plan

                PurchasedPlan purchasePlan1 = PurchasedPlan.builder()
                        .seller(seller1)
                        .name("Plan 1")
                        .purchasedDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                        .price(500)
                        .status(Status.PURCHASED_PLAN_STATUS_PURCHASED)
                        .paymentMethod(VNPay)
                        .build();

                purchasedPlanRepo.save(purchasePlan1);

                //init category
                Category category1 = categoryRepo.save(
                        Category.builder()
                                .name("Birthday")
                                .build()
                );

                Category category2 = categoryRepo.save(
                        Category.builder()
                                .name("Wedding")
                                .build()
                );

                Category category3 = categoryRepo.save(
                        Category.builder()
                                .name("Anniversary")
                                .build()
                );

                Category category4 = categoryRepo.save(
                        Category.builder()
                                .name("Mother's Day")
                                .build()
                );

                Category category5 = categoryRepo.save(
                        Category.builder()
                                .name("Exhibition")
                                .build()
                );

                Category category6 = categoryRepo.save(
                        Category.builder()
                                .name("Funeral")
                                .build()
                );

                Category category7 = categoryRepo.save(
                        Category.builder()
                                .name("Graduation")
                                .build()
                );

                //init flower category
                FlowerCategory flowerCategory1 = flowerCategoryRepo.save(
                            FlowerCategory.builder()
                                    .flower(flower1)
                                    .category(category1)
                                    .build()
                    );

                FlowerCategory flowerCategory2 = flowerCategoryRepo.save(
                            FlowerCategory.builder()
                                    .flower(flower1)
                                    .category(category2)
                                    .build()
                    );

                FlowerCategory flowerCategory3 = flowerCategoryRepo.save(
                            FlowerCategory.builder()
                                    .flower(flower2)
                                    .category(category3)
                                    .build()
                    );

                FlowerCategory flowerCategory4 = flowerCategoryRepo.save(
                            FlowerCategory.builder()
                                    .flower(flower2)
                                    .category(category4)
                                    .build()
                    );

                FlowerCategory flowerCategory5 = flowerCategoryRepo.save(
                        FlowerCategory.builder()
                                .flower(flower3)
                                .category(category5)
                                .build()
                );

                FlowerCategory flowerCategory6 = flowerCategoryRepo.save(
                        FlowerCategory.builder()
                                .flower(flower3)
                                .category(category6)
                                .build()
                );

                FlowerCategory flowerCategory7 = flowerCategoryRepo.save(
                        FlowerCategory.builder()
                                .flower(flower3)
                                .category(category7)
                                .build()
                );


            };


        };
    }

}
