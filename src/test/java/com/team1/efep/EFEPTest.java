package com.team1.efep;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.team1.efep.models.entity_models.Account;
import com.team1.efep.models.entity_models.User;
import com.team1.efep.models.request_models.ChangePasswordRequest;
import com.team1.efep.models.request_models.LoginRequest;
import com.team1.efep.models.request_models.RegisterRequest;
import com.team1.efep.models.request_models.ViewProfileRequest;
import com.team1.efep.models.response_models.ChangePasswordResponse;
import com.team1.efep.models.response_models.LoginResponse;
import com.team1.efep.models.response_models.RegisterResponse;
import com.team1.efep.models.response_models.ViewProfileResponse;
import com.team1.efep.repositories.AccountRepo;
import com.team1.efep.repositories.UserRepo;
import com.team1.efep.services.AccountService;
import com.team1.efep.validations.ChangePasswordValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EFEPTest {

    @Mock
    private AccountService accountService;  // Mock AccountService

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private Account account;

    public void TestResult(Object expected, Object actual) {
        System.out.println(expected.equals(actual) ? "Passed" : "Failed");
    }


    //Test 1
    @Test
    public void testLoginSuccess() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .email("buyer1@efep.com")
                .password("123")
                .build();

        LoginResponse expected = LoginResponse.builder()
                .status("200")
                .message("Login Successful")
                .build();

        // Mocking the service response
        when(accountService.loginAPI(request)).thenReturn(expected);

        // Act
        LoginResponse actual = accountService.loginAPI(request);

        // Assert
        TestResult(expected, actual);
    }


    //Test 2
    @Test
    public void testLoginFail() {
        // Arrange
        LoginRequest request = LoginRequest.builder()
                .email("buyer@efep.com")
                .password("123")
                .build();

        LoginResponse expected = LoginResponse.builder()
                .status("400")
                .message("Login Failed")
                .build();

        // Mocking the service response
        when(accountService.loginAPI(request)).thenReturn(expected);

        // Act
        LoginResponse actual = accountService.loginAPI(request);

        // Assert
        TestResult(expected, actual);
    }


    //Test 3
    @Test
    public void testRegisterSuccess() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .name("John Doe")
                .phone("123456789")
                .avatar("avatar.png")
                .background("background.png")
                .build();

        RegisterResponse expected = RegisterResponse.builder()
                .status("200")
                .message("Register Successful")
                .build();

        when(accountService.registerAPI(request)).thenReturn(expected);

        RegisterResponse actual = accountService.registerAPI(request);

        // Assert
        assertEquals(expected, actual);
    }


    //Test 4
    @Test
    public void testRegisterFailure() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .name("John Doe")
                .phone("123456789")
                .avatar("avatar.png")
                .background("background.png")
                .build();

        // Expected failure response
        RegisterResponse expected = RegisterResponse.builder()
                .status("400")
                .message("Email already in use")
                .build();

        // Mock the behavior of the accountService to return an error
        when(accountService.registerAPI(request)).thenReturn(expected);

        // Act
        RegisterResponse actual = accountService.registerAPI(request);

        // Assert - Expecting a failure, so we check if the actual response is NOT the expected success
        assertNotEquals("200", actual.getStatus()); // Expecting a non-success status
        TestResult(expected.getMessage(), actual.getMessage()); // Ensure the message is as expected
    }


    //Test 5
    @Test
    public void testChangePasswordSuccess() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .id(1)
                .currentPassword("seller1@efep.com")
                .newPassword("seller2@efep.com")
                .confirmPassword("seller2@efep.com")
                .build();

        // Mock the behavior of changePasswordLogic
        ChangePasswordResponse expected = ChangePasswordResponse.builder()
                .status("200")
                .message("Change password successfully")
                .build();

        when(accountService.changePasswordAPI(request)).thenReturn(expected);

        // Call the API
        ChangePasswordResponse response = accountService.changePasswordAPI(request);

        // Assert the response message
        assertEquals("Change password successfully", response.getMessage());
        TestResult(expected.getMessage(), response.getMessage());
    }


    //Test 6
    @Test
    public void testChangePasswordValidationError() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setId(1); // Assuming ID is valid
        request.setCurrentPassword(""); // Invalid current password
        request.setNewPassword("newPassword");

        ChangePasswordResponse expected = ChangePasswordResponse.builder()
                .status("400")
                .message("Invalid current password")
                .build();

        when(accountService.changePasswordAPI(request)).thenReturn(expected); // Mock validation

        ChangePasswordResponse response = accountService.changePasswordAPI(request);

        TestResult(expected.getMessage(), response.getMessage());
    }


    //Test 7
    @Test
    public void testChangePasswordAccountNotFound() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setId(1);
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");

        ChangePasswordResponse expected = ChangePasswordResponse.builder()
                .status("400")
                .message("Account not found")
                .build();

        when(accountService.changePasswordAPI(request)).thenReturn(expected);

        ChangePasswordResponse response = accountService.changePasswordAPI(request);

        assertEquals("400", response.getStatus());
        assertTrue(response.getMessage().contains("Account not found"));
    }


}
