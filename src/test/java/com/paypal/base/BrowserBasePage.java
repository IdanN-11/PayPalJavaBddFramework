package com.paypal.base;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserBasePage {
         public static WebDriverWait wait;
         public WebDriver driver ;
         
         public BrowserBasePage(WebDriver driver) {
        	 this.driver=driver;
        	 this.wait = new WebDriverWait(driver, Duration.ofSeconds((Long.parseLong(System.getProperty("explicit.wait")))));
         }
        
         
         // ================= BASIC WAITS =================

         /** Wait until element is visible */
         public WebElement waitForVisible(WebElement element) {
             return wait.until(ExpectedConditions.visibilityOf(element));
         }

         /** Wait until element is clickable */
         public WebElement waitForClickable(WebElement element) {
             return wait.until(ExpectedConditions.elementToBeClickable(element));
         }

         /** Wait until element is present in DOM */
         public WebElement waitForPresence(By locator) {
             return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
         }

         /** Wait until element disappears */
         public boolean waitForInvisibility(By locator) {
             return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
         }

         /** Wait until text is present */
         public boolean waitForText(By locator, String text) {
             return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
         }

         // ================= SAFE ACTIONS =================

         /** Safe click with retry (handles PayPal DOM refresh) */
         public void safeClick(WebElement element) {
             wait.until(driver -> {
                 try {
                     waitForClickable(element).click();
                     return true;
                 } catch (StaleElementReferenceException e) {
                     return false;
                 }
             });
         }
         
         public void safeClickSubmitButton(WebElement element) {
        	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        	    By spinner = By.cssSelector("div[data-testid='exit-loader-spinner'], div[class*='SpinnerOverlay']");
                
        	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        	    wait.until(ExpectedConditions.elementToBeClickable(element));
                System.out.println("Dsssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssone");
                JavascriptExecutor js = (JavascriptExecutor)driver;
                //element.click();
                js.executeScript("arguments[0].click();",
                element);

        	    // AFTER click (THIS IS WHAT YOU WERE MISSING)
        	    wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
        	}


         /** Safe sendKeys */
         public void safeSendKeys(WebElement element, String text) {
             waitForVisible(element);
             element.clear();
             element.sendKeys(text);
         }

         // ================= PAGE / STATE =================

         /** Wait for URL to contain text */
         public boolean waitForUrlContains(String value) {
             return wait.until(ExpectedConditions.urlContains(value));
         }

         /** Hard sleep (use only when unavoidable) */
         public void sleep(long millis) {
             try {
                 Thread.sleep(millis);
             } catch (InterruptedException e) {
                 Thread.currentThread().interrupt();
             }
         }

         // ================= PAYPAL-SPECIFIC =================

         /** Wait for PayPal password page */
         public void waitForPasswordPage() {
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnLogin")));
         }

         /** Wait for PayPal email page */
         public void waitForEmailPage() {
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
             wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnNext")));
         }
     
         

}
