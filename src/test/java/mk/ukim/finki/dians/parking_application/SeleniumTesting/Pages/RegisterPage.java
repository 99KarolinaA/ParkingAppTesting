package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage {

    private WebElement name;
    private WebElement surname;
    private WebElement username;
    private WebElement password;
    private WebElement repeatedPassword;
    private WebElement registerBtn;

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public static RegisterPage open(WebDriver driver) {
        get(driver, "/register");
        return PageFactory.initElements(driver, RegisterPage.class);
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).isDisplayed();
    }

    public void register(WebDriver driver, RegisterPage registerPage, String name, String surname, String username, String password,
                         String repeatedPassword) {
        registerPage.name.sendKeys(name);
        registerPage.surname.sendKeys(surname);
        registerPage.username.sendKeys(username);
        registerPage.password.sendKeys(password);
        registerPage.repeatedPassword.sendKeys(repeatedPassword);

        registerPage.registerBtn.click();
        System.out.println(driver.getCurrentUrl());
    }

    public String getErrorMessage() {
        WebElement errorPage = driver.findElement(By.cssSelector("div[class='text-danger']"));
        return errorPage.getText();
    }
}
