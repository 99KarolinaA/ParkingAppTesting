package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        WebElement page = driver.findElement(By.cssSelector("img[src='images/home.png']"));
        return page.isDisplayed();
    }
}
