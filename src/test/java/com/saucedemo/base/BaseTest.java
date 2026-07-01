package com.saucedemo.base;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.saucedemo.config.TestConfig;
import com.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Lifecycle base for all test classes.
 *
 * <p>A single {@link Playwright} + {@link Browser} instance is shared per test
 * class, while each test gets a fresh, isolated {@link BrowserContext} and
 * {@link Page} so tests never leak session/cart state between each other.
 */
public abstract class BaseTest {

    private static Playwright playwright;
    private static Browser browser;

    private BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(TestConfig.headless());

        browser = switch (TestConfig.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        context.setDefaultTimeout(TestConfig.timeout());
        // Navigations get a longer budget than element actions: the demo site can
        // be slow to fully load, and we only wait for the DOM, not every image.
        context.setDefaultNavigationTimeout(TestConfig.timeout() * 2);
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    // ==========================================
    // Shared helpers
    // ==========================================

    /** Navigates to a URL, waiting only for the DOM to be ready (not all assets). */
    protected void navigate(String url) {
        page.navigate(url, new com.microsoft.playwright.Page.NavigateOptions()
                .setWaitUntil(com.microsoft.playwright.options.WaitUntilState.DOMCONTENTLOADED));
    }

    /** Opens the login page and returns its Page Object. */
    protected LoginPage openLoginPage() {
        navigate(TestConfig.baseUrl());
        return new LoginPage(page);
    }

    /** Logs in with the given credentials and returns the inventory Page Object. */
    protected com.saucedemo.pages.InventoryPage loginAs(String username, String password) {
        return openLoginPage().login(username, password);
    }

    /** Logs in as the standard user, the most common precondition. */
    protected com.saucedemo.pages.InventoryPage loginAsStandardUser() {
        return loginAs(TestConfig.standardUser(), TestConfig.password());
    }
}
