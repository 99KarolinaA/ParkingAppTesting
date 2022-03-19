package mk.ukim.finki.dians.parking_application.BusinessLogicTesting;

import mk.ukim.finki.dians.parking_application.model.User;
import mk.ukim.finki.dians.parking_application.model.enumeration.Role;
import mk.ukim.finki.dians.parking_application.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.dians.parking_application.model.exceptions.PasswordsDoNotMatchException;
import mk.ukim.finki.dians.parking_application.model.exceptions.UsernameExistsException;
import mk.ukim.finki.dians.parking_application.repository.UserRepository;
import mk.ukim.finki.dians.parking_application.service.UserService;
import mk.ukim.finki.dians.parking_application.service.implementations.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/*
JUST RUN THE TESTS
*/
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.service = Mockito.spy(new UserServiceImpl(this.userRepository, this.passwordEncoder));
    }

    @Test
    public void testSuccessfulRegister() {
        //given
        User user = generateUser();

        //when
        when(this.userRepository.save(Mockito.any(User.class))).thenReturn(user);
        when(this.passwordEncoder.encode(Mockito.anyString())).thenReturn("password");

        User actual = this.service.register("username", "password", "password", "name", "surname", Role.ROLE_USER);

        //then
        assertNotNull(user);
        assertEquals(user.getName(), actual.getName());
        assertEquals(user.getRole(), actual.getRole());
        assertEquals(user.getSurname(), actual.getSurname());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getUsername(), actual.getUsername());
    }


    @Test
    public void testNullUsername() {
        //then
        Exception exception = assertThrows(InvalidArgumentsException.class,
                () -> service.register(null, "password", "password", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = "Invalid arguments exception";
        assertEquals(exceptedMessage, exception.getMessage());
    }

    @Test
    public void testEmptyUsername() {
        //then
        Exception exception = assertThrows(InvalidArgumentsException.class,
                () -> service.register("", "password", "password", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = "Invalid arguments exception";
        assertEquals(exceptedMessage, exception.getMessage());
    }


    @Test
    public void testEmptyPassword() {
        //then
        Exception exception = assertThrows(InvalidArgumentsException.class,
                () -> service.register("username", "", "password", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = "Invalid arguments exception";
        assertEquals(exceptedMessage, exception.getMessage());
    }

    @Test
    public void testNullPassword() {
        //then
        Exception exception = assertThrows(InvalidArgumentsException.class,
                () -> service.register("username", null, "password", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = "Invalid arguments exception";
        assertEquals(exceptedMessage, exception.getMessage());
    }


    @Test
    public void testPasswordMismatch() {
        //then
        Exception exception = assertThrows(PasswordsDoNotMatchException.class,
                () -> service.register("username", "password", "otherPassword", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = "Passwords do not match exception";
        assertEquals(exceptedMessage, exception.getMessage());
    }


    @Test
    public void testDuplicateUsername() {
        //given
        User user = generateUser();

        //when
        when(this.userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //then
        Exception exception = assertThrows(UsernameExistsException.class,
                () -> service.register("username", "password", "password", "name", "surname", Role.ROLE_USER));
        String exceptedMessage = String.format("User with username: %s already exists", user.getUsername());
        assertEquals(exceptedMessage, exception.getMessage());
    }

    @Test
    public void testSuccessfulLoadUserByUsername() {
        //given
        User user = generateUser();

        //when
        when(this.userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User actual = (User) this.service.loadUserByUsername(user.getUsername());

        //then
        assertEquals(user, actual);
    }

    @Test
    public void testLoadUserByUsernameMismatch() {
        //given
        User user = generateUser();
        String wrongUsername = "otherUsername";

        //when
        when(this.userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //then
        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(wrongUsername));
        String exceptedMessage = wrongUsername;
        assertEquals(exceptedMessage, exception.getMessage());
    }

    private User generateUser() {
        return new User("username", "password", "name", "surname");
    }

}
