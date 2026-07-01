package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;

/**
 * The Swag Labs login page (https://www.saucedemo.com/).
 */
public class LoginPage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator errorMessage;

    // ==========================================
    // Constructor
    // ==========================================
    public LoginPage(Page page) {
        super(page);
        this.usernameField = page.locator("#user-name");
        this.passwordField = page.locator("#password");
        this.loginButton = page.locator("#login-button");
        this.errorMessage = page.locator("[data-test='error']");
    }

    // ==========================================
    // Actions
    // ==========================================

    public LoginPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public LoginPage clickLogin() {
        click(loginButton);
        return this;
    }

    /**
     * Fills credentials and submits, returning the resulting inventory page.
     * Use only for the happy path where login is expected to succeed.
     */
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(page);
    }

    /** Submits credentials but stays on the login page (for negative cases). */
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return this;
    }

    // ==========================================
    // State
    // ==========================================

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginButtonVisible() {
        return isElementVisible(loginButton);
    }
}
