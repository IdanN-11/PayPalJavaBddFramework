package com.example.paypal.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.paypal.base.BrowserBasePage;

public class AccountsPage extends BrowserBasePage {

    public AccountsPage(WebDriver driver) {
        super(driver);
    }

    // ================= PAGE LOAD METHODS=================

    public void waitForAccountsPage() {
        waitForPresence(AccountsPageOR.payWithHeader);
    }

    // ================= ACCOUNT ACTIONS =================

    /**
     * Select account by visible account name
     * Example: "PayPal balance", "Visa", "CREDIT UNION 1 (AK)"
     */
    public void selectAccountByName(String accountName) {

        By radio = AccountsPageOR.accountRadioByName(accountName);
        System.out.println("Clicking");
        WebElement radioBtn = waitForPresence(radio);
        System.out.println("Clicking");
        safeClick(radioBtn);
        System.out.println("Clicking");
    }

    /**
     * Validate account is visible
     */
    public boolean isAccountVisible(String accountName) {
        try {
            waitForPresence(AccountsPageOR.accountName(accountName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Optional validation using last digits
     */
    public boolean isAccountWithLastDigitsVisible(String digits) {
        try {
            waitForPresence(AccountsPageOR.accountLastDigits(digits));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= CHECKOUT =================

    public void clickCompletePurchase() {
        WebElement btn = waitForPresence(AccountsPageOR.completePurchaseBtn);
        safeClick(btn);
    }

    public void cancelAndReturn() {
        WebElement cancel = waitForPresence(AccountsPageOR.cancelLink);
        safeClick(cancel);
    }
}
