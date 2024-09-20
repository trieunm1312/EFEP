package com.team1.efep;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.repositories.WishlistRepo;
import com.team1.efep.service_implementors.AccountServiceImpl;
import com.team1.efep.services.AccountService;
import com.team1.efep.utils.GoogleLoginGeneratorUtil;
import lombok.RequiredArgsConstructor;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MockingTest {

    private AccountService accountService;

    @Mocked
    AccountRepo accountRepo;

    @Mocked
    UserRepo userRepo;

    @Mocked
    WishlistRepo wishlistRepo;

    @Mocked
    GoogleLoginGeneratorUtil googleLoginGeneratorUtil;

    @BeforeEach
    public void setUp() {
        accountService = new AccountServiceImpl(accountRepo, userRepo, wishlistRepo, googleLoginGeneratorUtil);
    }

    @Test
    public void testLogin(){

        new Expectations() {{
           accountRepo.findByEmailAndPassword("test@gmail.com", "123");
           result = Account.builder().email("test@gmail.com").password("123").status(Status.ACCOUNT_STATUS_ACTIVE).role(Role.BUYER).build();
        }};

        System.out.println(accountService.loginAPI(LoginRequest.builder().email("test@gmail.com").password("123").build()).getMessage());
    }
}
