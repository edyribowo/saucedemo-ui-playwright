package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.config.TestData;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.components.HeaderComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Feature: Navigation Menu")
class NavigationTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void loginBeforeEachTest() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("TC-NAV-001 — Open and close the burger menu")
    void openAndCloseBurgerMenu() {
        HeaderComponent header = inventory.header();

        header.openMenu();
        assertTrue(header.isMenuOpen(), "Menu should be open");
        assertTrue(header.isAboutLinkVisible(), "About link should be visible in the menu");

        header.closeMenu();
        assertFalse(header.isMenuOpen(), "Menu should be closed after clicking X");
    }

    @Test
    @DisplayName("TC-NAV-002 — Reset App State clears the cart")
    void resetAppStateClearsCart() {
        inventory.addProductToCart(TestData.BACKPACK)
                .addProductToCart(TestData.BIKE_LIGHT);
        assertEquals(2, inventory.header().getCartBadgeCount());

        inventory.header().resetAppState();

        // The badge clears immediately after reset.
        assertFalse(inventory.header().isCartBadgeVisible(), "Cart badge should disappear");

        // saucedemo only relabels the inventory buttons on the next page load,
        // so reload to observe the reset (cleared) state.
        inventory.reload();
        assertTrue(inventory.isAddButtonVisible(TestData.BACKPACK),
                "Buttons should be back to 'Add to cart' after reset");
        assertFalse(inventory.isRemoveButtonVisible(TestData.BACKPACK),
                "No Remove buttons should remain after reset");
    }
}
