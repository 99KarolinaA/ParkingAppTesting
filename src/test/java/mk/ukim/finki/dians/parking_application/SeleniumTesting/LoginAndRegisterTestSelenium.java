package mk.ukim.finki.dians.parking_application.SeleniumTesting;

import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.HomePage;
import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.LoginPage;
import mk.ukim.finki.dians.parking_application.SeleniumTesting.Pages.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/*
FIRST RUN THE APP, THEN THE TEST
DON'T RUN THE TEST TWICE, THE LAST TEST WILL FAIL, THE USER WILL BE ALREADY REGISTERED
 */
public class LoginAndRegisterTestSelenium {
    private WebDriver driver;

    @BeforeTest
    public void setup() {
        driver = getDriver();
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:/Users/Karolina/Documents/GitHub/ParkingAppTesting/parking_application/src/main/resources/drivers/chromedriver.exe");
        return new ChromeDriver();
    }

    /*
    LOGIN
    */
    @Test
    public void LoginOpen() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);

        assertTrue(loginPage.isLoaded());
    }

    @Test
    public void LoginWithInvalidCredentials() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        assertTrue(loginPage.isLoaded());

        loginPage.login("test123", "error");
        String errorMessage = loginPage.getErrorMessage();
        System.out.println(errorMessage);
        assertEquals(errorMessage, "Invalid user credentials exception");

    }

    @Test
    public void LoginWithEmptyUsername() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        assertTrue(loginPage.isLoaded());

        loginPage.login("", "test");
        String errorMessage = loginPage.getErrorMessage();
        System.out.println(errorMessage);
        assertEquals(errorMessage, "Invalid user credentials exception");

    }

    @Test
    public void SuccessfulLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        assertTrue(loginPage.isLoaded());

        loginPage.login("user", "user");
        assertTrue(new HomePage(driver).isLoaded());

    }

    /*
    REGISTER
    */
    @Test
    public void RegisterOpen() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);
        RegisterPage.open(driver);

        assertTrue(registerPage.isLoaded());
    }

    @Test
    public void RegisterPasswordMismatch() throws InterruptedException {
        RegisterPage registerPage = RegisterPage.open(driver);
        assertTrue(registerPage.isLoaded());

        registerPage.register(driver, registerPage, "name", "surname", "username", "password", "error");
        String errorMessage = registerPage.getErrorMessage();
        System.out.println(errorMessage);
        assertEquals(errorMessage, "Passwords do not match exception");

    }

    @Test
    public void RegisterArgumentEmpty() throws InterruptedException {
        RegisterPage registerPage = RegisterPage.open(driver);
        assertTrue(registerPage.isLoaded());

        registerPage.register(driver, registerPage, "", "surname", "username", "password", "password");
        String errorMessage = registerPage.getErrorMessage();
        System.out.println(errorMessage);
        assertEquals(errorMessage, "Invalid arguments exception");

    }

    @Test
    public void RegisterUsernameAlreadyExists() throws InterruptedException {
        RegisterPage registerPage = RegisterPage.open(driver);
        assertTrue(registerPage.isLoaded());

        registerPage.register(driver, registerPage, "user", "surname", "user", "password", "password");
        String errorMessage = registerPage.getErrorMessage();
        System.out.println(errorMessage);
        assertEquals(errorMessage, "User with username: user already exists");

    }

    @Test
    public void SuccessfulRegister() throws InterruptedException {
        RegisterPage registerPage = RegisterPage.open(driver);
        assertTrue(registerPage.isLoaded());

        registerPage.register(driver, registerPage, "test", "test", "test", "test", "test");
        LoginPage loginPage = new LoginPage(driver);
        assertTrue(loginPage.isLoaded());

        loginPage.login("test", "test");
        assertTrue(new HomePage(driver).isLoaded());

    }

}
