package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.config.TestData;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Feature: Shopping Cart")
class CartTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void loginBeforeEachTest() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("TC-CART-001 — Add a single product to cart")
    void addSingleProductToCart() {
        inventory.addProductToCart(TestData.BACKPACK);

        assertTrue(inventory.isRemoveButtonVisible(TestData.BACKPACK),
                "Button should switch to Remove");
        assertEquals(1, inventory.header().getCartBadgeCount(), "Cart badge should read 1");
    }

    @Test
    @DisplayName("TC-CART-002 — Add multiple products to cart")
    void addMultipleProductsToCart() {
        inventory.addProductToCart(TestData.BACKPACK)
                .addProductToCart(TestData.BIKE_LIGHT);

        assertEquals(2, inventory.header().getCartBadgeCount(), "Cart badge should read 2");

        CartPage cart = inventory.header().openCart();
        assertEquals(2, cart.getItemCount(), "Cart should list 2 items");
        assertTrue(cart.containsProduct(TestData.BACKPACK));
        assertTrue(cart.containsProduct(TestData.BIKE_LIGHT));
    }

    @Test
    @DisplayName("TC-CART-003 — Remove a product from the inventory page")
    void removeProductFromInventoryPage() {
        inventory.addProductToCart(TestData.BACKPACK);
        assertEquals(1, inventory.header().getCartBadgeCount());

        inventory.removeProductFromCart(TestData.BACKPACK);

        assertTrue(inventory.isAddButtonVisible(TestData.BACKPACK),
                "Button should revert to Add to cart");
        assertEquals(0, inventory.header().getCartBadgeCount(), "Cart badge should disappear");
    }

    @Test
    @DisplayName("TC-CART-004 — Remove a product from the cart page")
    void removeProductFromCartPage() {
        inventory.addProductToCart(TestData.BACKPACK);
        CartPage cart = inventory.header().openCart();
        assertEquals(1, cart.getItemCount());

        cart.removeProduct(TestData.BACKPACK);

        assertTrue(cart.isEmpty(), "Cart should be empty after removing the only item");
        assertFalse(cart.header().isCartBadgeVisible(), "Cart badge should be gone");
    }

    @Test
    @DisplayName("TC-CART-005 — Continue shopping from cart page")
    void continueShoppingFromCart() {
        inventory.addProductToCart(TestData.BACKPACK);
        CartPage cart = inventory.header().openCart();

        InventoryPage returned = cart.continueShopping();

        assertTrue(returned.isLoaded(), "Should return to the inventory page");
        assertEquals(1, returned.header().getCartBadgeCount(),
                "Previously added item should still be in the cart");
    }

    @Test
    @DisplayName("TC-CART-006 — View empty cart")
    void viewEmptyCart() {
        CartPage cart = inventory.header().openCart();

        assertTrue(cart.isEmpty(), "Cart should contain no items");
        assertTrue(cart.isCheckoutButtonVisible(), "Checkout button should still be visible");
    }
}
