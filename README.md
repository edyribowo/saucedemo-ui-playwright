# saucedemo-ui-playwright

End-to-end UI automation for **Swag Labs** (https://www.saucedemo.com/) built with
**Java 21 + Playwright + JUnit 5**, following the Page Object Model (POM) with a
`BasePage` interaction-wrapper abstraction for consistent, low-flakiness waits.

> Framework: **Playwright Standalone** (pure Playwright — no Cucumber/Gherkin).

## Tech stack

| Layer | Choice |
|---|---|
| Language | Java 21 |
| Browser automation | Playwright for Java |
| Test runner | JUnit 5 (Jupiter) |
| Build | Maven |

## Project structure

```
src/test/java/com/saucedemo/
├── base/
│   ├── BasePage.java          # Wait-then-act wrapper (click/type/getText/visibility)
│   └── BaseTest.java          # Playwright/browser/context lifecycle + login helpers
├── config/
│   ├── TestConfig.java        # Loads config.properties (overridable via -D)
│   └── TestData.java          # Product names & expected messages
├── pages/
│   ├── LoginPage.java
│   ├── InventoryPage.java
│   ├── ProductDetailPage.java
│   ├── CartPage.java
│   ├── CheckoutInformationPage.java
│   ├── CheckoutOverviewPage.java
│   ├── CheckoutCompletePage.java
│   └── components/HeaderComponent.java   # Shared cart badge + burger menu
└── tests/
    ├── AuthenticationTest.java   # TC-AUTH-001..009
    ├── InventoryTest.java        # TC-INV-001..006
    ├── CartTest.java             # TC-CART-001..006
    ├── CheckoutTest.java         # TC-CHK-001..008
    └── NavigationTest.java       # TC-NAV-001..002
```

## Design: Page Object Model + BasePage

Page Objects contain **only locators and business flows**. All UI interactions go
through `BasePage`, which waits for the element to reach the correct state before
acting (e.g. `click` waits for visibility, `type` waits then fills). This keeps
explicit waits consistent everywhere and avoids duplication.

`BaseTest` shares one browser per test class but gives **each test a fresh
`BrowserContext`**, so session and cart state never leak between tests.

## Running the tests

```bash
# Install browsers once (downloads the Playwright browser binaries)
mvn compile
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Run the full suite (headless by default)
mvn test

# Run headed / with a specific browser
mvn test -Dheadless=false -Dbrowser=chromium

# Run a single feature
mvn test -Dtest=CheckoutTest
```

### Configuration

Defaults live in `src/test/resources/config.properties` and can be overridden at
runtime with `-D` flags: `base.url`, `browser` (`chromium`/`firefox`/`webkit`),
`headless`, `timeout`.

### Code coverage (JaCoCo)

`mvn test` records coverage via the JaCoCo agent and generates a report during the
same `test` phase:

```bash
mvn test
open target/site/jacoco/index.html   # HTML report; XML at target/site/jacoco/jacoco.xml
```

> Scope: JaCoCo instruments **this framework's own code** (page objects, helpers,
> test support) — not the application under test (saucedemo.com is an external web
> app). Read the number as "how much of our automation code the suite exercises",
> not as product coverage.

## Coverage

31 tests across 5 features — Authentication (9), Inventory (6), Cart (6),
Checkout (8), Navigation (2) — mapped 1:1 to the cases in `docs/test-cases.md`.

### Notes on real app behaviour

A couple of test cases document expectations the live saucedemo app does not
actually honour; the tests assert the **real** behaviour and call this out:

- **TC-INV-006** — the sort selection is *not* persisted across the product
  detail round trip; it resets to the default Name (A→Z).
- **TC-NAV-002** — *Reset App State* clears the cart badge immediately but only
  relabels the inventory buttons after a page reload.
