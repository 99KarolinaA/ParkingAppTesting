package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LocatePage extends BasePage {

    @FindBy(id = "cityName")
    private static WebElement searchByCity;

    @FindBy(id = "address")
    private static WebElement searchByAddress;

    @FindBy(id = "name")
    private static WebElement sortName;

    @FindBy(id = "rating")
    private static WebElement sortRating;

    @FindBy(id = "searchParking")
    private static WebElement searchButton;

    public LocatePage(WebDriver driver) {
        super(driver);
    }

    public static ParkingsPage locateParkingByCity(WebDriver driver) {
        get(driver, "/parking/locate");
        PageFactory.initElements(driver, LocatePage.class);
        searchByCity.sendKeys("Skopje");
        searchButton.click();
        return PageFactory.initElements(driver, ParkingsPage.class);
    }

    public static ParkingsPage ParkingNotFound(WebDriver driver) {
        get(driver, "/parking/locate");
        PageFactory.initElements(driver, LocatePage.class);
        searchByCity.sendKeys("NOTFOUNDCITY");
        searchByAddress.sendKeys("");
        searchButton.click();
        return PageFactory.initElements(driver, ParkingsPage.class);

    }

    public static ParkingsPage locateParkingByAddressSortedByName(WebDriver driver) {
        get(driver, "/parking/locate");
        PageFactory.initElements(driver, LocatePage.class);
        searchByAddress.sendKeys("Noemvri");
//        sortName.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                sortName);
        return PageFactory.initElements(driver, ParkingsPage.class);
    }

    public static ParkingsPage locateParkingByAddressSortedByRating(WebDriver driver) {
        get(driver, "/parking/locate");
        PageFactory.initElements(driver, LocatePage.class);
        searchByAddress.sendKeys("Noemvri");
//        sortRating.click();
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                sortRating);
        return PageFactory.initElements(driver, ParkingsPage.class);
    }

}
