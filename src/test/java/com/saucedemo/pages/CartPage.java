package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;
import com.saucedemo.pages.components.HeaderComponent;

import java.util.List;

/**
 * The shopping cart page (/cart.html).
 */
public class CartPage extends BasePage {

    public static final String PATH = "/cart.html";

    // ==========================================
    // Locators
    // ==========================================
    private final Locator cartItems;
    private final Locator itemNames;
    private final Locator continueShoppingButton;
    private final Locator checkoutButton;

    private final HeaderComponent header;

    public CartPage(Page page) {
        super(page);
        this.cartItems = page.locator(".cart_item");
        this.itemNames = page.locator(".inventory_item_name");
        this.continueShoppingButton = page.locator("#continue-shopping");
        this.checkoutButton = page.locator("#checkout");
        this.header = new HeaderComponent(page);
    }

    public HeaderComponent header() {
        return header;
    }

    // ==========================================
    // State
    // ==========================================

    public int getItemCount() {
        return cartItems.count();
    }

    public boolean isEmpty() {
        return cartItems.count() == 0;
    }

    public List<String> getItemNames() {
        return itemNames.allTextContents();
    }

    public boolean containsProduct(String productName) {
        return getItemNames().contains(productName);
    }

    public boolean isCheckoutButtonVisible() {
        return isElementVisible(checkoutButton);
    }

    // ==========================================
    // Actions
    // ==========================================

    public CartPage removeProduct(String productName) {
        click(page.locator("[data-test='remove-" + slug(productName) + "']"));
        return this;
    }

    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage(page);
    }

    public CheckoutInformationPage checkout() {
        click(checkoutButton);
        return new CheckoutInformationPage(page);
    }

    private static String slug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }
}
