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
                                .link("https://eventmetier.com.au/wp-content/uploads/2023/12/Tulip-Flower-White.jpg")
                                .build()
                );

                FlowerImage flowerImage4 = flowerImageRepo.save(
                        FlowerImage.builder()
                                .flower(flower2)
                                .link("https://eventmetier.com.au/wp-content/uploads/2023/12/Tulip-Flower-White.jpg")
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
                                .totalPrice(100)
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
                                .order(order1)
                                .quantity(1)
                                .build()
                );

                OrderDetail orderDetail2 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower2)
                                .flowerName(flower2.getName())
                                .order(order1)
                                .quantity(1)
                                .build()
                );

                order1.setOrderDetailList(List.of(orderDetail1, orderDetail2));

                OrderDetail orderDetail3 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower1)
                                .flowerName(flower1.getName())
                                .order(order2)
                                .quantity(1)
                                .build()
                );

                order2.setOrderDetailList(List.of(orderDetail3));

                OrderDetail orderDetail4 = orderDetailRepo.save(
                        OrderDetail.builder()
                                .flower(flower2)
                                .flowerName(flower2.getName())
                                .order(order3)
                                .quantity(1)
                                .build()
                );

                order3.setOrderDetailList(List.of(orderDetail4));

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
