No specific feature was provided, so I'll generate comprehensive test cases covering all major features of Swag Labs (saucedemo.com) — Login, Inventory, Cart, and Checkout.

---

# Test Cases — Swag Labs (saucedemo.com)

> **App URL:** https://www.saucedemo.com/
> **Test Accounts:** All users share password `secret_sauce`
> | Username | Type |
> |---|---|
> | `standard_user` | Normal user |
> | `locked_out_user` | Blocked user |
> | `problem_user` | UI bug simulation |
> | `performance_glitch_user` | Slow response simulation |

---

## Feature 1: Authentication

| Field | Value |
|---|---|
| **Module** | Login |
| **URL** | https://www.saucedemo.com/ |

---

### TC-AUTH-001 — Successful login with valid credentials
- **Type:** Positive
- **Pre-conditions:** User is on the login page and not logged in.
- **Test Steps:**
  1. Navigate to `https://www.saucedemo.com/`
  2. Enter `standard_user` in the **Username** field.
  3. Enter `secret_sauce` in the **Password** field.
  4. Click the **Login** button.
- **Test Data:** Username: `standard_user` | Password: `secret_sauce`
- **Expected Result:** User is redirected to the inventory page (`/inventory.html`). The product list is displayed.

---

### TC-AUTH-002 — Login with invalid password
- **Type:** Negative
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Enter `standard_user` in the **Username** field.
  2. Enter `wrongpassword` in the **Password** field.
  3. Click the **Login** button.
- **Test Data:** Username: `standard_user` | Password: `wrongpassword`
- **Expected Result:** An error message is displayed: *"Username and password do not match any user in this service."* User remains on the login page.

---

### TC-AUTH-003 — Login with invalid username
- **Type:** Negative
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Enter `unknown_user` in the **Username** field.
  2. Enter `secret_sauce` in the **Password** field.
  3. Click the **Login** button.
- **Test Data:** Username: `unknown_user` | Password: `secret_sauce`
- **Expected Result:** Error message displayed: *"Username and password do not match any user in this service."*

---

### TC-AUTH-004 — Login with empty username field
- **Type:** Negative
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Leave the **Username** field empty.
  2. Enter `secret_sauce` in the **Password** field.
  3. Click the **Login** button.
- **Test Data:** Username: *(empty)* | Password: `secret_sauce`
- **Expected Result:** Error message: *"Username is required."* Login is blocked.

---

### TC-AUTH-005 — Login with empty password field
- **Type:** Negative
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Enter `standard_user` in the **Username** field.
  2. Leave the **Password** field empty.
  3. Click the **Login** button.
- **Test Data:** Username: `standard_user` | Password: *(empty)*
- **Expected Result:** Error message: *"Password is required."* Login is blocked.

---

### TC-AUTH-006 — Login with both fields empty
- **Type:** Negative / Edge Case
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Leave both **Username** and **Password** fields empty.
  2. Click the **Login** button.
- **Test Data:** Username: *(empty)* | Password: *(empty)*
- **Expected Result:** Error message: *"Username is required."*

---

### TC-AUTH-007 — Login attempt with locked-out user
- **Type:** Negative
- **Pre-conditions:** User is on the login page.
- **Test Steps:**
  1. Enter `locked_out_user` in the **Username** field.
  2. Enter `secret_sauce` in the **Password** field.
  3. Click the **Login** button.
- **Test Data:** Username: `locked_out_user` | Password: `secret_sauce`
- **Expected Result:** Error message: *"Sorry, this user has been locked out."* User remains on the login page.

---

### TC-AUTH-008 — Successful logout
- **Type:** Positive
- **Pre-conditions:** User is logged in as `standard_user` and on the inventory page.
- **Test Steps:**
  1. Click the hamburger menu (☰) in the top-left corner.
  2. Click **Logout**.
- **Test Data:** N/A
- **Expected Result:** User is redirected to the login page. Attempting to navigate back to `/inventory.html` redirects to login.

---

### TC-AUTH-009 — Access inventory page without authentication
- **Type:** Security / Negative
- **Pre-conditions:** User is NOT logged in.
- **Test Steps:**
  1. Directly navigate to `https://www.saucedemo.com/inventory.html`.
- **Test Data:** N/A
- **Expected Result:** User is redirected to the login page with an error: *"You can only access '/inventory.html' when you are logged in."*

---

## Feature 2: Product Inventory

---

### TC-INV-001 — Verify product list loads after login
- **Type:** Positive
- **Pre-conditions:** User is logged in as `standard_user`.
- **Test Steps:**
  1. Observe the inventory page after login.
- **Expected Result:** 6 products are displayed, each with an image, name, description, price, and **Add to cart** button.

---

### TC-INV-002 — Sort products by price (low to high)
- **Type:** Positive
- **Pre-conditions:** User is on the inventory page.
- **Test Steps:**
  1. Click the sort dropdown (default: *"Name (A to Z)"*).
  2. Select **Price (low to high)**.
- **Expected Result:** Products are re-ordered with the lowest-priced item first (`$7.99`).

---

### TC-INV-003 — Sort products by price (high to low)
- **Type:** Positive
- **Pre-conditions:** User is on the inventory page.
- **Test Steps:**
  1. Click the sort dropdown.
  2. Select **Price (high to low)**.
- **Expected Result:** Products are ordered with the highest-priced item first (`$49.99`).

---

### TC-INV-004 — Sort products by name (Z to A)
- **Type:** Positive
- **Pre-conditions:** User is on the inventory page.
- **Test Steps:**
  1. Click the sort dropdown.
  2. Select **Name (Z to A)**.
- **Expected Result:** Products are listed in reverse alphabetical order.

---

### TC-INV-005 — Navigate to product detail page
- **Type:** Positive
- **Pre-conditions:** User is on the inventory page.
- **Test Steps:**
  1. Click on the product name or image of any product (e.g., *Sauce Labs Backpack*).
- **Expected Result:** User is navigated to the product detail page showing the full description, price, and **Add to cart** button.

---

### TC-INV-006 — Navigate back from product detail to inventory
- **Type:** Positive
- **Pre-conditions:** User is on a product detail page.
- **Test Steps:**
  1. Click the **Back to products** button.
- **Expected Result:** User is returned to the inventory page. Previously selected sort order is maintained.

---

## Feature 3: Shopping Cart

---

### TC-CART-001 — Add a single product to cart
- **Type:** Positive
- **Pre-conditions:** User is logged in and on the inventory page.
- **Test Steps:**
  1. Click **Add to cart** on the *Sauce Labs Backpack* product.
- **Expected Result:** The button changes to **Remove**. The cart icon in the top-right shows badge count `1`.

---

### TC-CART-002 — Add multiple products to cart
- **Type:** Positive
- **Pre-conditions:** User is logged in and on the inventory page.
- **Test Steps:**
  1. Click **Add to cart** on the *Sauce Labs Backpack*.
  2. Click **Add to cart** on the *Sauce Labs Bike Light*.
  3. Click the cart icon.
- **Test Data:** Products: Sauce Labs Backpack, Sauce Labs Bike Light
- **Expected Result:** Cart page displays 2 items. Cart badge shows `2`.

---

### TC-CART-003 — Remove a product from the inventory page
- **Type:** Positive
- **Pre-conditions:** User has added *Sauce Labs Backpack* to the cart.
- **Test Steps:**
  1. Click the **Remove** button on the *Sauce Labs Backpack* product card.
- **Expected Result:** Button reverts to **Add to cart**. Cart badge decrements by 1 (or disappears if cart is empty).

---

### TC-CART-004 — Remove a product from the cart page
- **Type:** Positive
- **Pre-conditions:** User has at least 1 item in the cart and is on the cart page.
- **Test Steps:**
  1. Click the cart icon to open the cart.
  2. Click **Remove** next to a product.
- **Expected Result:** The item is removed from the cart. If no items remain, the cart is empty.

---

### TC-CART-005 — Continue shopping from cart page
- **Type:** Positive
- **Pre-conditions:** User is on the cart page.
- **Test Steps:**
  1. Click the **Continue Shopping** button.
- **Expected Result:** User is navigated back to the inventory page. Previously added cart items are still in the cart.

---

### TC-CART-006 — View empty cart
- **Type:** Edge Case
- **Pre-conditions:** User is logged in with no items added to cart.
- **Test Steps:**
  1. Click the cart icon.
- **Expected Result:** Cart page is displayed with no items listed. The **Checkout** button is visible but cart item list is empty.

---

## Feature 4: Checkout

---

### TC-CHK-001 — Complete checkout with valid information
- **Type:** Positive
- **Pre-conditions:** User has at least 1 item in the cart and is on the cart page.
- **Test Steps:**
  1. Click the **Checkout** button.
  2. Enter `John` in **First Name**.
  3. Enter `Doe` in **Last Name**.
  4. Enter `12345` in **Zip/Postal Code**.
  5. Click **Continue**.
  6. Review the order summary on the Overview page.
  7. Click **Finish**.
- **Test Data:** First Name: `John` | Last Name: `Doe` | Zip: `12345`
- **Expected Result:** User sees the order confirmation page with the message *"Thank you for your order!"* and a pony express image.

---

### TC-CHK-002 — Checkout with empty First Name
- **Type:** Negative
- **Pre-conditions:** User is on the Checkout Step 1 (Your Information) page.
- **Test Steps:**
  1. Leave **First Name** empty.
  2. Enter `Doe` in **Last Name**.
  3. Enter `12345` in **Zip/Postal Code**.
  4. Click **Continue**.
- **Test Data:** First Name: *(empty)* | Last Name: `Doe` | Zip: `12345`
- **Expected Result:** Error message: *"First Name is required."* User remains on Step 1.

---

### TC-CHK-003 — Checkout with empty Last Name
- **Type:** Negative
- **Pre-conditions:** User is on the Checkout Step 1 page.
- **Test Steps:**
  1. Enter `John` in **First Name**.
  2. Leave **Last Name** empty.
  3. Enter `12345` in **Zip/Postal Code**.
  4. Click **Continue**.
- **Test Data:** First Name: `John` | Last Name: *(empty)* | Zip: `12345`
- **Expected Result:** Error message: *"Last Name is required."*

---

### TC-CHK-004 — Checkout with empty Zip/Postal Code
- **Type:** Negative
- **Pre-conditions:** User is on the Checkout Step 1 page.
- **Test Steps:**
  1. Enter `John` in **First Name**.
  2. Enter `Doe` in **Last Name**.
  3. Leave **Zip/Postal Code** empty.
  4. Click **Continue**.
- **Test Data:** First Name: `John` | Last Name: `Doe` | Zip: *(empty)*
- **Expected Result:** Error message: *"Postal Code is required."*

---

### TC-CHK-005 — Verify price totals on Checkout Overview
- **Type:** Positive
- **Pre-conditions:** User has completed Checkout Step 1 with at least 1 item in cart.
- **Test Steps:**
  1. On Checkout Step 2 (Overview), review the **Item total** and **Tax** values.
  2. Manually verify: `Item total + Tax = Total`.
- **Expected Result:** The displayed **Total** equals the sum of the item subtotal and tax. No price discrepancy exists.

---

### TC-CHK-006 — Cancel checkout and return to cart
- **Type:** Positive
- **Pre-conditions:** User is on the Checkout Step 1 page.
- **Test Steps:**
  1. Click the **Cancel** button.
- **Expected Result:** User is returned to the cart page. Items in cart remain unchanged.

---

### TC-CHK-007 — Cancel on Checkout Overview and return to inventory
- **Type:** Positive
- **Pre-conditions:** User is on the Checkout Overview (Step 2) page.
- **Test Steps:**
  1. Click the **Cancel** button.
- **Expected Result:** User is returned to the inventory page. Cart items are still persisted.

---

### TC-CHK-008 — Cart is empty after successful order completion
- **Type:** Positive
- **Pre-conditions:** User has just completed a successful order and sees the confirmation page.
- **Test Steps:**
  1. Click **Back Home**.
  2. Observe the cart icon badge.
- **Expected Result:** User is on the inventory page. The cart icon shows no badge (cart is empty).

---

## Feature 5: Navigation Menu

---

### TC-NAV-001 — Open and close the burger menu
- **Type:** Positive
- **Pre-conditions:** User is logged in on any page.
- **Test Steps:**
  1. Click the hamburger menu (☰) icon.
  2. Observe the menu items displayed.
  3. Click the **X** close button.
- **Expected Result:** Menu opens showing: *All Items*, *About*, *Logout*, *Reset App State*. Clicking X closes the menu.

---

### TC-NAV-002 — Reset App State clears the cart
- **Type:** Positive
- **Pre-conditions:** User is logged in with items in the cart.
- **Test Steps:**
  1. Add 2+ items to the cart.
  2. Click the hamburger menu (☰).
  3. Click **Reset App State**.
- **Expected Result:** Cart badge disappears. All **Add to cart** buttons on the inventory page are reset (no **Remove** buttons remain).

---

*Total: 26 test cases across 5 features.*

---

The feature field was left blank, so I generated 26 test cases covering all main flows: **Authentication (9)**, **Inventory (6)**, **Cart (6)**, **Checkout (8)**, and **Navigation (2)**. If you want me to generate BDD/Gherkin format, automate any of these with Playwright + JUnit 5, or focus on a specific feature, just let me know.
