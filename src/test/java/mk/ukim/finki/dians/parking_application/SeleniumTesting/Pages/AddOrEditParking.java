package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AddOrEditParking extends BasePage {

    private static WebElement name;
    private static WebElement city;
    private static WebElement address;
    private static WebElement latitude;
    private static WebElement longitude;
    private static WebElement rating;
    private static WebElement submit;

    public AddOrEditParking(WebDriver driver) {
        super(driver);
    }

    public static ParkingsPage addParking(WebDriver driver, String name, String city, String address, String latitude, String longitude, String rating) {
        get(driver, "/parking/add-form");

        PageFactory.initElements(driver, AddOrEditParking.class);
        AddOrEditParking.name.sendKeys(name);
        AddOrEditParking.city.sendKeys(city);
        AddOrEditParking.address.sendKeys(address);
        AddOrEditParking.latitude.sendKeys(latitude);
        AddOrEditParking.longitude.sendKeys(longitude);
        AddOrEditParking.rating.sendKeys(rating);

        AddOrEditParking.submit.click();
        return PageFactory.initElements(driver, ParkingsPage.class);
    }

    public static ParkingsPage editParking(WebDriver driver, WebElement editButton, String name, String city, String address, String latitude, String longitude, String rating) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                editButton);
        System.out.println(driver.getCurrentUrl());

        PageFactory.initElements(driver, AddOrEditParking.class);
        AddOrEditParking.name.sendKeys(name);
        AddOrEditParking.city.sendKeys(city);
        AddOrEditParking.address.sendKeys(address);
        AddOrEditParking.latitude.clear();
        AddOrEditParking.latitude.sendKeys(latitude);
        AddOrEditParking.longitude.clear();
        AddOrEditParking.longitude.sendKeys(longitude);
        AddOrEditParking.rating.clear();
        AddOrEditParking.rating.sendKeys(rating);

        AddOrEditParking.submit.click();
        return PageFactory.initElements(driver, ParkingsPage.class);
    }
}
