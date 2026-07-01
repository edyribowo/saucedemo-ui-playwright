package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;

/**
 * Checkout step one: "Your Information" (/checkout-step-one.html).
 */
public class CheckoutInformationPage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator firstNameField;
    private final Locator lastNameField;
    private final Locator postalCodeField;
    private final Locator continueButton;
    private final Locator cancelButton;
    private final Locator errorMessage;

    public CheckoutInformationPage(Page page) {
        super(page);
        this.firstNameField = page.locator("#first-name");
        this.lastNameField = page.locator("#last-name");
        this.postalCodeField = page.locator("#postal-code");
        this.continueButton = page.locator("#continue");
        this.cancelButton = page.locator("#cancel");
        this.errorMessage = page.locator("[data-test='error']");
    }

    // ==========================================
    // Actions
    // ==========================================

    public CheckoutInformationPage enterFirstName(String firstName) {
        type(firstNameField, firstName);
        return this;
    }

    public CheckoutInformationPage enterLastName(String lastName) {
        type(lastNameField, lastName);
        return this;
    }

    public CheckoutInformationPage enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
        return this;
    }

    public CheckoutInformationPage fillInformation(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        return this;
    }

    /** Clicks Continue expecting success; returns the overview page. */
    public CheckoutOverviewPage clickContinue() {
        click(continueButton);
        return new CheckoutOverviewPage(page);
    }

    /** Clicks Continue expecting a validation error; stays on this page. */
    public CheckoutInformationPage clickContinueExpectingError() {
        click(continueButton);
        return this;
    }

    public CartPage cancel() {
        click(cancelButton);
        return new CartPage(page);
    }

    // ==========================================
    // State
    // ==========================================

    public boolean isErrorDisplayed() {
        return isElementVisible(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
