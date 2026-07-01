package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.config.TestData;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutCompletePage;
import com.saucedemo.pages.CheckoutInformationPage;
import com.saucedemo.pages.CheckoutOverviewPage;
import com.saucedemo.pages.InventoryPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Feature: Checkout")
class CheckoutTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void loginAndAddItem() {
        inventory = loginAsStandardUser();
        inventory.addProductToCart(TestData.BACKPACK);
    }

    /** Convenience: open cart then start checkout on the information page. */
    private CheckoutInformationPage startCheckout() {
        return inventory.header().openCart().checkout();
    }

    @Test
    @DisplayName("TC-CHK-001 — Complete checkout with valid information")
    void completeCheckoutWithValidInformation() {
        CheckoutCompletePage complete = startCheckout()
                .fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE)
                .clickContinue()
                .finish();

        assertEquals(TestData.ORDER_CONFIRMATION, complete.getConfirmationMessage());
        assertTrue(complete.isPonyExpressImageVisible(), "Pony express image should be shown");
    }

    @Test
    @DisplayName("TC-CHK-002 — Checkout with empty First Name")
    void checkoutWithEmptyFirstName() {
        CheckoutInformationPage info = startCheckout()
                .fillInformation("", TestData.LAST_NAME, TestData.POSTAL_CODE)
                .clickContinueExpectingError();

        assertEquals(TestData.ERR_FIRST_NAME_REQUIRED, info.getErrorMessage());
    }

    @Test
    @DisplayName("TC-CHK-003 — Checkout with empty Last Name")
    void checkoutWithEmptyLastName() {
        CheckoutInformationPage info = startCheckout()
                .fillInformation(TestData.FIRST_NAME, "", TestData.POSTAL_CODE)
                .clickContinueExpectingError();

        assertEquals(TestData.ERR_LAST_NAME_REQUIRED, info.getErrorMessage());
    }

    @Test
    @DisplayName("TC-CHK-004 — Checkout with empty Zip/Postal Code")
    void checkoutWithEmptyPostalCode() {
        CheckoutInformationPage info = startCheckout()
                .fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, "")
                .clickContinueExpectingError();

        assertEquals(TestData.ERR_POSTAL_CODE_REQUIRED, info.getErrorMessage());
    }

    @Test
    @DisplayName("TC-CHK-005 — Verify price totals on Checkout Overview")
    void verifyPriceTotalsOnOverview() {
        CheckoutOverviewPage overview = startCheckout()
                .fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE)
                .clickContinue();

        double expectedTotal = overview.getSubtotal() + overview.getTax();
        // Compare with a small delta to avoid floating-point rounding noise.
        assertEquals(expectedTotal, overview.getTotal(), 0.001,
                "Total should equal item subtotal plus tax");
    }

    @Test
    @DisplayName("TC-CHK-006 — Cancel checkout and return to cart")
    void cancelCheckoutReturnsToCart() {
        CartPage cart = startCheckout().cancel();

        assertTrue(cart.containsProduct(TestData.BACKPACK), "Cart items should remain unchanged");
    }

    @Test
    @DisplayName("TC-CHK-007 — Cancel on Checkout Overview and return to inventory")
    void cancelOnOverviewReturnsToInventory() {
        InventoryPage returned = startCheckout()
                .fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE)
                .clickContinue()
                .cancel();

        assertTrue(returned.isLoaded(), "Should return to the inventory page");
        assertEquals(1, returned.header().getCartBadgeCount(), "Cart items should still persist");
    }

    @Test
    @DisplayName("TC-CHK-008 — Cart is empty after successful order completion")
    void cartEmptyAfterOrderCompletion() {
        InventoryPage returned = startCheckout()
                .fillInformation(TestData.FIRST_NAME, TestData.LAST_NAME, TestData.POSTAL_CODE)
                .clickContinue()
                .finish()
                .backHome();

        assertTrue(returned.isLoaded(), "Should be on the inventory page");
        assertFalse(returned.header().isCartBadgeVisible(), "Cart badge should be gone");
        assertEquals(0, returned.header().getCartBadgeCount());
    }
}
