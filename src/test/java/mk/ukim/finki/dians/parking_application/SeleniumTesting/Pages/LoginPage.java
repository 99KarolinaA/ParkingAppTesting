package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private WebElement username;

    private WebElement password;

    private WebElement login;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public static LoginPage open(WebDriver driver) {
        get(driver, "/login");
        return PageFactory.initElements(driver, LoginPage.class);
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).isDisplayed();
    }

    public void login(String user, String password) throws InterruptedException {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(user);
        Thread.sleep(5000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("input[value='Login']")).click();
        Thread.sleep(5000);

    }

    public static ParkingsPage doLogin(WebDriver driver, LoginPage loginPage, String username, String password) {
        loginPage.username.sendKeys(username);
        loginPage.password.sendKeys(password);
        loginPage.login.click();
        System.out.println(driver.getCurrentUrl());
        return PageFactory.initElements(driver, ParkingsPage.class);
    }


    public String getErrorMessage() {
        WebElement errorPage = driver.findElement(By.cssSelector("div[class='text-danger']"));
        return errorPage.getText();
    }

    public static LoginPage logout(WebDriver driver) {
        get(driver, "/logout");
        return PageFactory.initElements(driver, LoginPage.class);
    }
}
