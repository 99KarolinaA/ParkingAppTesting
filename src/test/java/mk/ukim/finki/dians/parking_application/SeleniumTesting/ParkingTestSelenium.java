package mk.ukim.finki.dians.parking_application.SeleniumTesting;

import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.AddOrEditParking;
import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.LocatePage;
import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.LoginPage;
import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.ParkingsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.junit.jupiter.api.Test;

/*
FIRST RUN THE APP, THEN THE TEST
*/
public class ParkingTestSelenium {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = getDriver();
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/Karolina/Documents/GitHub/ParkingAppTesting/parking_application/src/main/resources/drivers/chromedriver.exe");
        return new ChromeDriver();
    }

    @AfterEach
    public void destroy() {
        if (this.driver != null) {
            this.driver.close();
        }
    }

    /*
    Test add, edit and delete parking
    Test security authorization
    */
    @Test
    public void test1() {
        //user login
        LoginPage loginPage = LoginPage.open(this.driver);
        LoginPage.doLogin(this.driver, loginPage, "user", "user");
        ParkingsPage parkingsPage = ParkingsPage.open(this.driver);
        parkingsPage.assertElements(0, 0, 0, 4);
        loginPage = LoginPage.logout(this.driver);

        //admin login
        LoginPage.doLogin(this.driver, loginPage, "admin", "admin");
        parkingsPage = ParkingsPage.open(this.driver);
        parkingsPage.assertElements(4, 4, 1, 4);

        //add parkings
        parkingsPage = AddOrEditParking.addParking(this.driver, "test", "Skopje", "test address", "41.9945142", "21.4061413", "5");
        parkingsPage.assertElements(5, 5, 1, 5);

        parkingsPage = AddOrEditParking.addParking(this.driver, "test2", "Skopje", "test address2", "41.9948949", "21.4117381", "4");
        parkingsPage.assertElements(6, 6, 1, 6);

        //delete parking
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                parkingsPage.getDeleteButton().get(parkingsPage.getDeleteButton().size() - 1));
        parkingsPage.assertElements(5, 5, 1, 5);

        //edit parking
        parkingsPage = AddOrEditParking.
                editParking(this.driver, parkingsPage.getEditButton().get(parkingsPage.getDeleteButton().size() - 1),
                        "testedit", "Skopje", "test addressedit", "41.9948949", "21.4117381", "4");
        parkingsPage.assertElements(5, 5, 1, 5);

        //delete parking
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                parkingsPage.getDeleteButton().get(parkingsPage.getDeleteButton().size() - 1));
        parkingsPage.assertElements(4, 4, 1, 4);

    }

    /*
    Test search by city/address sorted by name/rating
    */
    @Test
    public void test2() {
        //user login
        LoginPage loginPage = LoginPage.open(this.driver);
        LoginPage.doLogin(this.driver, loginPage, "user", "user");

        //search by city sorted by default (name)
        ParkingsPage parkingsPage = LocatePage.locateParkingByCity(this.driver);
        //another html page without add/edit - LOCATE.HTML not ALL-PARKINGS.HTML
        parkingsPage.assertElements(0, 0, 0, 3);

        //search by address sorted by name
        parkingsPage = LocatePage.locateParkingByAddressSortedByName(this.driver);
        parkingsPage.assertElements(0, 0, 0, 4);
        String p1 = driver.findElements(By.tagName("iframe")).get(0).getAttribute("src");
        Assert.assertEquals("https://www.google.com/maps/d/embed?mid=1txUPlEdtj6plPAOGkBhTrixwl7UYwXyv&z=17", p1);

        //search by address sorted by rating
        parkingsPage = LocatePage.locateParkingByAddressSortedByRating(this.driver);
        parkingsPage.assertElements(0, 0, 0, 4);
        String p2 = driver.findElements(By.tagName("iframe")).get(1).getAttribute("src");
        Assert.assertEquals("https://www.google.com/maps/d/u/0/embed?mid=1lSMzXTm9-kU-Nak6qE6jBuxNhK0093dO&z=17", p2);

        //search by city but with no result
        parkingsPage = LocatePage.ParkingNotFound(this.driver);
        parkingsPage.assertElements(0, 0, 0, 0);
        WebElement page = driver.findElement(By.cssSelector("h3[class='errormsg']"));
        Assert.assertTrue(page.isDisplayed());

    }

}
