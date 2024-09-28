package com.team1.efep;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.Seller;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.entity_models.Wishlist;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.SellerRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.repositories.WishlistRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class EfepApplication {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;
    private final WishlistRepo wishlistRepo;


    public static void main(String[] args) {
        SpringApplication.run(EfepApplication.class, args);
    }


    @Bean
    public CommandLineRunner initData() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {

                sellerRepo.save(
                        Seller.builder()
                                .businessPlan(null)
                                .user(
                                        userRepo.save(User.builder()
                                                .avatar("")
                                                .background("")
                                                .name("Seller1")
                                                .phone("")
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
                                .phone("")
                                .avatar("")
                                .background("")
                                .orderList(null)
                                .wishlist(null)
                                .seller(null)
                                .build()
                );

                wishlistRepo.save(
                        Wishlist.builder()
                                .wishlistItemList(null)
                                .user(user1)
                                .build()
                );

            }
        };
    }

}
