package com.saucedemo.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;

/**
 * The persistent top header shared across the inventory, cart and product
 * detail pages: the cart icon/badge and the hamburger (burger) menu.
 */
public class HeaderComponent extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator cartLink;
    private final Locator cartBadge;
    private final Locator burgerButton;
    private final Locator closeMenuButton;
    private final Locator menuWrap;
    private final Locator allItemsLink;
    private final Locator aboutLink;
    private final Locator logoutLink;
    private final Locator resetAppStateLink;

    public HeaderComponent(Page page) {
        super(page);
        this.cartLink = page.locator(".shopping_cart_link");
        this.cartBadge = page.locator(".shopping_cart_badge");
        this.burgerButton = page.locator("#react-burger-menu-btn");
        this.closeMenuButton = page.locator("#react-burger-cross-btn");
        this.menuWrap = page.locator(".bm-menu-wrap");
        this.allItemsLink = page.locator("#inventory_sidebar_link");
        this.aboutLink = page.locator("#about_sidebar_link");
        this.logoutLink = page.locator("#logout_sidebar_link");
        this.resetAppStateLink = page.locator("#reset_sidebar_link");
    }

    // ==========================================
    // Cart
    // ==========================================

    public CartPage openCart() {
        click(cartLink);
        return new CartPage(page);
    }

    /** Returns the cart badge count, or 0 when no badge is shown. */
    public int getCartBadgeCount() {
        if (!isElementVisible(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(getText(cartBadge));
    }

    public boolean isCartBadgeVisible() {
        return isElementVisible(cartBadge);
    }

    // ==========================================
    // Burger menu
    // ==========================================

    public HeaderComponent openMenu() {
        click(burgerButton);
        // The sidebar slides in via a CSS transform; the menu links stay in the
        // DOM even when closed, so wait on the wrap's aria-hidden state rather
        // than element visibility.
        page.waitForCondition(this::menuIsExpanded);
        return this;
    }

    public HeaderComponent closeMenu() {
        click(closeMenuButton);
        page.waitForCondition(() -> !menuIsExpanded());
        return this;
    }

    public boolean isMenuOpen() {
        return menuIsExpanded();
    }

    /** True when the burger menu is expanded (aria-hidden="false"). */
    private boolean menuIsExpanded() {
        return "false".equals(menuWrap.getAttribute("aria-hidden"));
    }

    public InventoryPage clickAllItems() {
        click(allItemsLink);
        return new InventoryPage(page);
    }

    public boolean isAboutLinkVisible() {
        return isElementVisible(aboutLink);
    }

    public LoginPage logout() {
        openMenu();
        click(logoutLink);
        return new LoginPage(page);
    }

    public void resetAppState() {
        openMenu();
        click(resetAppStateLink);
    }
}
