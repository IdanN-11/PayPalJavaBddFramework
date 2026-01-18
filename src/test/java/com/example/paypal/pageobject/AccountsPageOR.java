package com.example.paypal.pageobject;

import org.openqa.selenium.By;

public class AccountsPageOR {

    // ===== Account / Funding Instrument =====

    // Account name text (PayPal balance, Visa, Bank name)
    public static By accountName(String name) {
        return By.xpath(
            "//span[@data-testid='c3-fi-details-name' and normalize-space()='" + name + "']"
        );
    }

    // Radio button for the account (relative to name)
    public static By accountRadioByName(String name) {
        return By.xpath(
            "//span[@data-testid='c3-fi-details-name' and normalize-space()='" + name + "']" +
            		"//ancestor::label//span[contains(@class,'check_icon_container')]"
        );
    }

    // Last digits (optional validation)
    public static By accountLastDigits(String digits) {
        return By.xpath(
            "//span[contains(@class,'FiDetails_lastDigits') and contains(text(),'" + digits + "')]"
        );
    }

    // ===== Checkout buttons =====

    public static final By completePurchaseBtn =
        By.xpath("//button[@data-testid='submit-button-initial']");

    public static final By cancelLink =
        By.xpath("//a[@data-testid='cancel-link']");

    // ===== Page validation =====

    public static final By payWithHeader =
        By.xpath("//h2[@data-testid='paywith-title']");
}
