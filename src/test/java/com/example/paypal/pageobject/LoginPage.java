package com.example.paypal.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.paypal.base.BrowserBasePage;

public class LoginPage extends BrowserBasePage{

    private WebDriver driver;

    // ================= CONSTRUCTOR =================

    public LoginPage(WebDriver driver) {
    	super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // =================================================
    // =============== EMAIL STEP ======================
    // =================================================

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "btnNext")
    private WebElement nextButton;

    @FindBy(id = "forgotEmail")
    private WebElement forgotEmailLink;

    @FindBy(id = "backToInputEmailLink")
    private WebElement changeEmailLink;
    
    
    // =================================================
    // ============== PASSWORD STEP ====================
    // =================================================

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "btnLogin")
    private WebElement loginButton;

    @FindBy(id = "forgotPassword")
    private WebElement forgotPasswordLink;

    @FindBy(id = "setupPassword")
    private WebElement setupPasswordLink;

    // Show / Hide password
    @FindBy(id = "Show password")
    private WebElement showPasswordButton;

    @FindBy(id = "Hide")
    private WebElement hidePasswordButton;

    // =================================================
    // ============== ERROR / ALERTS ===================
    // =================================================

    @FindBy(id = "keychainErrorMessage")
    private WebElement keychainErrorContainer;

    @FindBy(css = "#keychainErrorMessage .notification-warning")
    private WebElement warningMessage;

    @FindBy(css = "#keychainErrorMessage .notification-critical")
    private WebElement criticalMessage;

    // =================================================
    // ============== OTHER OPTIONS ====================
    // =================================================

    @FindBy(id = "createAccount")
    private WebElement payWithCardButton;

    @FindBy(id = "beginOtpLogin")
    private WebElement otpLoginButton;
    
    @FindBy(xpath="//div[@class ='profileBar']//a[contains(text(),'Not you')]")
    private WebElement ChangeEmail;
    
    @FindBy(xpath="//a[contains(text(),'Log in with a password instead')]")
    private WebElement loginWithPassword;

    // =================================================
    // ============== ACTION METHODS ===================
    // =================================================

    /* ---------- EMAIL ---------- */

    public void enterEmail(String email) {
    	
        emailInput.clear();
//        emailInput.sendKeys(email);
        safeSendKeys(emailInput,email);
    }

    public void clickNext() {
//        nextButton.click();
    	safeClick(nextButton);
    }

    public void loginWithEmail(String email) {
        enterEmail(email);
        clickNext();
    }

    public void clickForgotEmail() {
        forgotEmailLink.click();
    }

    public void clickChangeEmail() {
        changeEmailLink.click();
    }

    /* ---------- PASSWORD ---------- */

    public void enterPassword(String password) {
//        passwordInput.clear();
//        passwordInput.sendKeys(password);
    	safeSendKeys(passwordInput,password);
    }

    public void clickLogin() {
        loginButton.click();
    }

    public void loginWithPassword(String password) {
        enterPassword(password);
        clickLogin();
    }

    public void showPassword() {
        showPasswordButton.click();
    }

    public void hidePassword() {
        hidePasswordButton.click();
    }

    public void clickForgotPassword() {
        forgotPasswordLink.click();
    }

    public void clickSetupPassword() {
        setupPasswordLink.click();
    }

    /* ---------- OTHER FLOWS ---------- */

    public void clickPayWithCard() {
        payWithCardButton.click();
    }

    public void clickLoginWithOtp() {
        otpLoginButton.click();
    }
    public void clickChangeEmailLink() {
    	safeClick(ChangeEmail);
    }
    
    public void loginWithPasswordInstead() {
    	safeClick(loginWithPassword);
    }

    // =================================================
    // ============== VALIDATIONS ======================
    // =================================================

    public boolean isEmailPageDisplayed() {
        return emailInput.isDisplayed() && nextButton.isDisplayed();
    }

    public boolean isPasswordPageDisplayed() {
        return passwordInput.isDisplayed() && loginButton.isDisplayed();
    }

    public boolean isKeychainErrorDisplayed() {
        return keychainErrorContainer.isDisplayed();
    }

    public String getWarningMessage() {
        return warningMessage.isDisplayed() ? warningMessage.getText() : null;
    }

    public String getCriticalMessage() {
        return criticalMessage.isDisplayed() ? criticalMessage.getText() : null;
    }
}
