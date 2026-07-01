package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;
import com.saucedemo.pages.components.HeaderComponent;

/**
 * A single product's detail page (reached by clicking a product name/image).
 */
public class ProductDetailPage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator productName;
    private final Locator productPrice;
    private final Locator productDescription;
    private final Locator backToProductsButton;

    private final HeaderComponent header;

    public ProductDetailPage(Page page) {
        super(page);
        this.productName = page.locator(".inventory_details_name");
        this.productPrice = page.locator(".inventory_details_price");
        this.productDescription = page.locator(".inventory_details_desc");
        this.backToProductsButton = page.locator("#back-to-products");
        this.header = new HeaderComponent(page);
    }

    public HeaderComponent header() {
        return header;
    }

    // ==========================================
    // State
    // ==========================================

    public boolean isLoaded() {
        return isDisplayed(productName);
    }

    public String getProductName() {
        return getText(productName);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean isDescriptionVisible() {
        return isElementVisible(productDescription);
    }

    public boolean isAddToCartVisible() {
        return isElementVisible(page.locator("button", new Page.LocatorOptions().setHasText("Add to cart")));
    }

    // ==========================================
    // Actions
    // ==========================================

    public InventoryPage backToProducts() {
        click(backToProductsButton);
        return new InventoryPage(page);
    }
}
