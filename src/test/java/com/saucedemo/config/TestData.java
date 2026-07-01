package com.saucedemo.config;

/**
 * Shared, non-secret test data: product display names and expected UI messages.
 */
public final class TestData {

    private TestData() {
    }

    // Products
    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    // Checkout customer data
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String POSTAL_CODE = "12345";

    // Expected messages
    public static final String ERR_CREDENTIALS =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String ERR_LOCKED_OUT =
            "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String ERR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    public static final String ERR_NO_LOGIN_ACCESS =
            "Epic sadface: You can only access '/inventory.html' when you are logged in.";
    public static final String ERR_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String ERR_LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String ERR_POSTAL_CODE_REQUIRED = "Error: Postal Code is required";

    public static final String ORDER_CONFIRMATION = "Thank you for your order!";
}
