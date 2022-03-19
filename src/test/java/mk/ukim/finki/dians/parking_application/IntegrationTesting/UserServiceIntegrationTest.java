package mk.ukim.finki.dians.parking_application.IntegrationTesting;

import mk.ukim.finki.dians.parking_application.model.enumeration.Role;
import mk.ukim.finki.dians.parking_application.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.dians.parking_application.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.dians.parking_application.model.exceptions.UsernameExistsException;
import mk.ukim.finki.dians.parking_application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/*
JUST RUN THE TESTS
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    public void testSuccessfulRegister() {
        Assert.assertNotNull(userService.register("test", "test", "test", "test", "test", Role.ROLE_USER));
    }

    @Test
    public void testRegisterPasswordMismatch() {

        Exception exception = assertThrows(PasswordsDoNotMatchException.class, () -> {
            this.userService.register("test", "test", "test123", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Passwords do not match exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterDuplicateUsername() {

        Exception exception = assertThrows(UsernameExistsException.class, () -> {
            this.userService.register("user", "test", "test", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "User with username: user already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterEmptyUsername() {

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            this.userService.register("", "test", "test", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Invalid arguments exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterNullUsername() {

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            this.userService.register(null, "test", "test", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Invalid arguments exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterEmptyPassword() {

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            this.userService.register("username", "", "test", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Invalid arguments exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testRegisterNullPasword() {

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            this.userService.register("username", null, "test", "test", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Invalid arguments exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testRegisterEmptyName() {

        Exception exception = assertThrows(InvalidArgumentsException.class, () -> {
            this.userService.register("username", "test", "test", "", "test", Role.ROLE_USER);
        });

        String expectedMessage = "Invalid arguments exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testLoginUsernameNotFound() {

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            this.userService.loadUserByUsername("notfoundUsername");
        });

        String expectedMessage = "notfoundUsername";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testLoginUsernameFound() {

        String username = this.userService.loadUserByUsername("user").getUsername();
        assertNotNull(username);
    }
}

