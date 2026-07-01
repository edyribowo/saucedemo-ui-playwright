package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;
import com.saucedemo.pages.components.HeaderComponent;

import java.util.List;

/**
 * The product inventory (listing) page shown after a successful login.
 */
public class InventoryPage extends BasePage {

    public static final String PATH = "/inventory.html";

    // Product sort option labels (as shown in the dropdown).
    public static final String SORT_NAME_A_TO_Z = "Name (A to Z)";
    public static final String SORT_NAME_Z_TO_A = "Name (Z to A)";
    public static final String SORT_PRICE_LOW_TO_HIGH = "Price (low to high)";
    public static final String SORT_PRICE_HIGH_TO_LOW = "Price (high to low)";

    // ==========================================
    // Locators
    // ==========================================
    private final Locator inventoryContainer;
    private final Locator inventoryItems;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator sortDropdown;

    private final HeaderComponent header;

    // ==========================================
    // Constructor
    // ==========================================
    public InventoryPage(Page page) {
        super(page);
        // saucedemo renders a duplicate id="inventory_container"; the data-test
        // attribute resolves unambiguously (avoids Playwright strict-mode errors).
        this.inventoryContainer = page.locator("[data-test='inventory-container']");
        this.inventoryItems = page.locator(".inventory_item");
        this.itemNames = page.locator(".inventory_item_name");
        this.itemPrices = page.locator(".inventory_item_price");
        this.sortDropdown = page.locator("[data-test='product-sort-container']");
        this.header = new HeaderComponent(page);
    }

    public HeaderComponent header() {
        return header;
    }

    // ==========================================
    // Actions
    // ==========================================

    public boolean isLoaded() {
        return isDisplayed(inventoryContainer);
    }

    /** Reloads the current page and returns this page object. */
    public InventoryPage reload() {
        page.reload();
        return this;
    }

    public int getProductCount() {
        inventoryItems.first().waitFor();
        return inventoryItems.count();
    }

    public InventoryPage sortBy(String optionLabel) {
        selectByLabel(sortDropdown, optionLabel);
        return this;
    }

    /** Clicks "Add to cart" for the given product display name. */
    public InventoryPage addProductToCart(String productName) {
        click(addButton(productName));
        return this;
    }

    /** Clicks "Remove" for the given product display name from the listing. */
    public InventoryPage removeProductFromCart(String productName) {
        click(removeButton(productName));
        return this;
    }

    public boolean isRemoveButtonVisible(String productName) {
        return isElementVisible(removeButton(productName));
    }

    public boolean isAddButtonVisible(String productName) {
        return isElementVisible(addButton(productName));
    }

    /** Opens the product detail page by clicking the product's name link. */
    public ProductDetailPage openProductDetail(String productName) {
        click(page.locator(".inventory_item_name", new Page.LocatorOptions().setHasText(productName)));
        return new ProductDetailPage(page);
    }

    // ==========================================
    // Data readers
    // ==========================================

    public List<String> getProductNames() {
        itemNames.first().waitFor();
        return itemNames.allTextContents();
    }

    /** Product prices in listing order as doubles (e.g. 29.99). */
    public List<Double> getProductPrices() {
        itemPrices.first().waitFor();
        return itemPrices.allTextContents().stream()
                .map(text -> Double.parseDouble(text.replace("$", "").trim()))
                .toList();
    }

    // ==========================================
    // Internal helpers
    // ==========================================

    private Locator addButton(String productName) {
        return page.locator("[data-test='add-to-cart-" + slug(productName) + "']");
    }

    private Locator removeButton(String productName) {
        return page.locator("[data-test='remove-" + slug(productName) + "']");
    }

    /** Converts a product display name to the data-test id slug used by the app. */
    private static String slug(String productName) {
        return productName.toLowerCase().replace(" ", "-");
    }
}
