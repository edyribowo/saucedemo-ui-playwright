package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.config.TestConfig;
import com.saucedemo.config.TestData;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static java.util.regex.Pattern.compile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Feature: Authentication")
class AuthenticationTest extends BaseTest {

    @Test
    @DisplayName("TC-AUTH-001 — Successful login with valid credentials")
    void successfulLoginWithValidCredentials() {
        InventoryPage inventory = loginAsStandardUser();

        assertThat(page).hasURL(compile(".*/inventory.html"));
        assertTrue(inventory.isLoaded(), "Inventory product list should be displayed");
    }

    @Test
    @DisplayName("TC-AUTH-002 — Login with invalid password")
    void loginWithInvalidPassword() {
        LoginPage login = openLoginPage()
                .loginExpectingFailure(TestConfig.standardUser(), "wrongpassword");

        assertTrue(login.isErrorDisplayed(), "Error message should be shown");
        assertEquals(TestData.ERR_CREDENTIALS, login.getErrorMessage());
        assertThat(page).hasURL(compile(".*/$|.*saucedemo.com/?$"));
    }

    @Test
    @DisplayName("TC-AUTH-003 — Login with invalid username")
    void loginWithInvalidUsername() {
        LoginPage login = openLoginPage()
                .loginExpectingFailure("unknown_user", TestConfig.password());

        assertEquals(TestData.ERR_CREDENTIALS, login.getErrorMessage());
    }

    @Test
    @DisplayName("TC-AUTH-004 — Login with empty username field")
    void loginWithEmptyUsername() {
        LoginPage login = openLoginPage()
                .loginExpectingFailure("", TestConfig.password());

        assertEquals(TestData.ERR_USERNAME_REQUIRED, login.getErrorMessage());
        assertTrue(login.isLoginButtonVisible(), "User should remain on the login page");
    }

    @Test
    @DisplayName("TC-AUTH-005 — Login with empty password field")
    void loginWithEmptyPassword() {
        LoginPage login = openLoginPage()
                .loginExpectingFailure(TestConfig.standardUser(), "");

        assertEquals(TestData.ERR_PASSWORD_REQUIRED, login.getErrorMessage());
    }

    @Test
    @DisplayName("TC-AUTH-006 — Login with both fields empty")
    void loginWithBothFieldsEmpty() {
        LoginPage login = openLoginPage().loginExpectingFailure("", "");

        assertEquals(TestData.ERR_USERNAME_REQUIRED, login.getErrorMessage());
    }

    @Test
    @DisplayName("TC-AUTH-007 — Login attempt with locked-out user")
    void loginWithLockedOutUser() {
        LoginPage login = openLoginPage()
                .loginExpectingFailure(TestConfig.lockedUser(), TestConfig.password());

        assertEquals(TestData.ERR_LOCKED_OUT, login.getErrorMessage());
        assertTrue(login.isLoginButtonVisible(), "User should remain on the login page");
    }

    @Test
    @DisplayName("TC-AUTH-008 — Successful logout")
    void successfulLogout() {
        InventoryPage inventory = loginAsStandardUser();

        LoginPage login = inventory.header().logout();
        assertThat(page).hasURL(compile(".*saucedemo.com/?$"));
        assertTrue(login.isLoginButtonVisible(), "Login button should be visible after logout");

        // Back navigation to a protected page must redirect to login.
        navigate(TestConfig.baseUrl() + "inventory.html");
        assertTrue(login.isErrorDisplayed(), "Protected page should redirect with an error");
    }

    @Test
    @DisplayName("TC-AUTH-009 — Access inventory page without authentication")
    void accessInventoryWithoutAuthentication() {
        navigate(TestConfig.baseUrl() + "inventory.html");

        LoginPage login = new LoginPage(page);
        assertTrue(login.isErrorDisplayed(), "Direct access should be blocked");
        assertEquals(TestData.ERR_NO_LOGIN_ACCESS, login.getErrorMessage());
    }
}
