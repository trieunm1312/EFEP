package com.team1.efep;

import com.team1.efep.enums.Role;
import com.team1.efep.enums.Status;
import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.ChangePasswordRequest;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.UpdateProfileRequest;
import com.team1.efep.models.request_models.ViewProfileRequest;
import com.team1.efep.models.response_models.SearchBuyerListResponse;
import com.team1.efep.models.response_models.UpdateProfileResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
           result = Account.builder()
                   .email("test@gmail.com")
                   .password("123")
                   .status(Status.ACCOUNT_STATUS_ACTIVE)
                   .role(Role.BUYER)
                   .build();
        }};

        System.out.println(accountService.loginAPI(LoginRequest.builder().email("test@gmail.com").password("123").build()).getMessage());
    }


    @Test
    public void testChangePassword(){

        new Expectations() {{
            accountRepo.findByIdAndPassword(5, "123");
            result = Account.builder()
                    .id(5)
                    .password("123")
                    .build();
        }};

        ChangePasswordRequest request = ChangePasswordRequest.builder().id(5).currentPassword("123").newPassword("0407@Q").confirmPassword("0407@Q").build();

        System.out.println(accountService.changePasswordAPI(request));
    }


    @Test
    public void testViewProfile() {
        new Expectations() {{
         userRepo.findById(2);
            result = User.builder()
                    .id(2)
                    .name("seller")
                    .phone("090912344")
                    .avatar("")
                    .background("")
                    .build();
        }};

        ViewProfileRequest request = ViewProfileRequest.builder().id(2).build();
        ViewProfileResponse response = accountService.viewProfileAPI(request);
        System.out.println(response.getMessage());
    }


    @Test
    public void testUpdateProfile_Success() {
        new Expectations() {{
            accountRepo.findById(1);
            result = Account.builder()
                    .id(1)
                    .user(User.builder()
                            .name("seller")
                            .phone("0902455400")
                            .avatar("")
                            .background("")
                            .build())
                    .build();
            userRepo.save((User)any);
            result = User.builder()
                    .id(1)
                    .name("newBuyer")
                    .phone("0912345678")
                    .avatar("newAvatar.png")
                    .background("newBackground.jpg")
                    .build();
        }};

        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .id(1)
                .name("newBuyer")
                .phone("0912345678")
                .avatar("newAvatar.png")
                .background("newBackground.jpg")
                .build();

        UpdateProfileResponse response = accountService.updateProfileAPI(request);

        // Check for success response
        System.out.println(response.getMessage());
        assertEquals("200", response.getStatus());
        assertEquals("Update profile successfully", response.getMessage());
        assertEquals("newBuyer", response.getName());
    }

}
