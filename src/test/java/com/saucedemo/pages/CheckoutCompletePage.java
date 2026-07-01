package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;

/**
 * Checkout completion / order confirmation page (/checkout-complete.html).
 */
public class CheckoutCompletePage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator completeHeader;
    private final Locator ponyExpressImage;
    private final Locator backHomeButton;

    public CheckoutCompletePage(Page page) {
        super(page);
        this.completeHeader = page.locator(".complete-header");
        this.ponyExpressImage = page.locator(".pony_express");
        this.backHomeButton = page.locator("#back-to-products");
    }

    // ==========================================
    // State
    // ==========================================

    public String getConfirmationMessage() {
        return getText(completeHeader);
    }

    public boolean isPonyExpressImageVisible() {
        return isElementVisible(ponyExpressImage);
    }

    // ==========================================
    // Actions
    // ==========================================

    public InventoryPage backHome() {
        click(backHomeButton);
        return new InventoryPage(page);
    }
}
