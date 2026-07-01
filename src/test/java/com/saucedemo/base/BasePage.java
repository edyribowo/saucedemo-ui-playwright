package com.saucedemo.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Central interaction wrapper that all Page Objects extend.
 *
 * <p>Child Page Objects never call raw Playwright commands such as
 * {@code locator.click()} directly. Instead they delegate to the helpers below,
 * which automatically wait for the element to reach the correct state before
 * acting. This keeps explicit-wait handling consistent across the whole
 * framework and removes duplication, reducing flakiness.
 */
public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    // ==========================================
    // Interactions (wait-then-act)
    // ==========================================

    /** Waits for the element to be visible, then clicks it. */
    protected void click(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.click();
    }

    /** Waits for the element to be visible, clears it, then types the given text. */
    protected void type(Locator locator, String text) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.fill(text);
    }

    /** Waits for the element to be visible, then returns its trimmed text. */
    protected String getText(Locator locator) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return locator.textContent() == null ? "" : locator.textContent().trim();
    }

    /** Selects an option in a native {@code <select>} by its visible label. */
    protected void selectByLabel(Locator locator, String label) {
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        locator.selectOption(new com.microsoft.playwright.options.SelectOption().setLabel(label));
    }

    // ==========================================
    // State checks
    // ==========================================

    /**
     * Quick presence check for optional/negative assertions (e.g. "is the cart
     * badge gone?"). Uses a short timeout so absence is confirmed fast, and
     * never throws on a missing element.
     */
    protected boolean isElementVisible(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(3000));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Positive "did this element render?" check that waits up to the full
     * default timeout. Use for page-load assertions that immediately follow a
     * navigation, where rendering may take longer than the quick check allows.
     */
    protected boolean isDisplayed(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /** Waits until the page URL contains the given fragment. */
    protected void waitForUrlContains(String fragment) {
        page.waitForURL("**" + fragment + "**");
    }

    // ==========================================
    // Navigation
    // ==========================================

    /** Navigates to a path relative to the configured base URL. */
    protected void navigateTo(String url) {
        page.navigate(url);
    }
}
