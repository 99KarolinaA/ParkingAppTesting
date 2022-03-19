package mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages;

import lombok.Getter;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Getter
public class ParkingsPage extends BasePage {

    @FindBy(className = "deleteParking")
    private List<WebElement> deleteButton;


    @FindBy(className = "editParking")
    private List<WebElement> editButton;


    @FindBy(id = "addParking")
    private List<WebElement> addButton;

    @FindBy(className = "parkingCard")
    private List<WebElement> ParkingCards;


    public ParkingsPage(WebDriver driver) {
        super(driver);
    }

    public static ParkingsPage open(WebDriver driver) {
        get(driver, "/parking/all-parkings");
        return PageFactory.initElements(driver, ParkingsPage.class);
    }

    public void assertElements(int deleteButton, int editButton, int addButton, int parkingCards) {
        Assert.assertEquals("delete do not match", deleteButton, this.getDeleteButton().size());
        Assert.assertEquals("edit do not match", editButton, this.getEditButton().size());
        Assert.assertEquals("add is visible", addButton, this.getAddButton().size());
        Assert.assertEquals(parkingCards, this.getParkingCards().size());
    }

}