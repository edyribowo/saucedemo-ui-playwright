package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.base.BasePage;

/**
 * Checkout step two: "Overview" (/checkout-step-two.html) with price totals.
 */
public class CheckoutOverviewPage extends BasePage {

    // ==========================================
    // Locators
    // ==========================================
    private final Locator subtotalLabel;
    private final Locator taxLabel;
    private final Locator totalLabel;
    private final Locator finishButton;
    private final Locator cancelButton;

    public CheckoutOverviewPage(Page page) {
        super(page);
        this.subtotalLabel = page.locator(".summary_subtotal_label");
        this.taxLabel = page.locator(".summary_tax_label");
        this.totalLabel = page.locator(".summary_total_label");
        this.finishButton = page.locator("#finish");
        this.cancelButton = page.locator("#cancel");
    }

    // ==========================================
    // State (price parsing)
    // ==========================================

    /** Item subtotal, e.g. from "Item total: $29.99". */
    public double getSubtotal() {
        return parseAmount(getText(subtotalLabel));
    }

    /** Tax amount, e.g. from "Tax: $2.40". */
    public double getTax() {
        return parseAmount(getText(taxLabel));
    }

    /** Grand total, e.g. from "Total: $32.39". */
    public double getTotal() {
        return parseAmount(getText(totalLabel));
    }

    // ==========================================
    // Actions
    // ==========================================

    public CheckoutCompletePage finish() {
        click(finishButton);
        return new CheckoutCompletePage(page);
    }

    public InventoryPage cancel() {
        click(cancelButton);
        return new InventoryPage(page);
    }

    // ==========================================
    // Helpers
    // ==========================================

    /** Extracts the numeric value that follows the "$" in a summary label. */
    private static double parseAmount(String label) {
        int dollarIndex = label.indexOf('$');
        return Double.parseDouble(label.substring(dollarIndex + 1).trim());
    }
}
