package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorUser;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;

import static org.junit.jupiter.api.Assertions.*;

import com.mongodb.client.*;
import org.bson.Document;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Autowired
    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    @Autowired
    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");
    @Autowired
    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");

    @Autowired
    private UserService userService;

    /*@BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

     */

    // Checks if a new user is created
    @Test
    public void createUser_validInputs_success() {
        // given
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        // not tested, Id, Token, creation Date,status, favorite locations
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());

        usersCollection.deleteOne(eq("username", "testUsername"));
    }


    // Checks if the user can not be created if the username already exists
    @Test
    public void createUser_duplicateUsername_throwsException() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        testUser2.setName("testName2");
        testUser2.setUsername("testUsername");
        testUser2.setPassword("password");

        // check that an error is thrown
        String exceptionMessage = "The provided username is already taken. Please try a new one.";
        DuplicatedUserException exception = assertThrows(DuplicatedUserException.class, () -> userService.createUser(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());

        usersCollection.deleteOne(eq("username", "testUsername"));

    }

    // Checks if a user is logged in
    @Test
    public void checkForLogin_validInputs_success() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User loggedInUser = new User();

        loggedInUser = userService.checkForLogin(createdUser);

        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());

        usersCollection.deleteOne(eq("username", "testUsername"));

    }

    // Checks if the user can not log in if the username does not exist
    @Test
    public void checkForLogin_duplicateUsername_throwsException() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        FindIterable<Document> request2 = usersCollection.find(eq("username", "testUsernamefalse"));
        Document user2 = request.first();
        assertNull(user2);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();

        testUser2.setUsername("testUsernamefalse");
        testUser2.setPassword("password");

        // check that an error is thrown
        String exceptionMessage = "This username does not exist, please register first.";
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> userService.checkForLogin(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());

        usersCollection.deleteOne(eq("username", "testUsername"));

    }

    // Checks if the user can not log in if the password is false for the given username
    @Test
    public void checkForLogin_wrongPassword_throwsException() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        FindIterable<Document> request2 = usersCollection.find(eq("username", "testUsername"));
        Document user2 = request.first();
        assertNull(user2);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();

        testUser2.setUsername("testUsername");
        testUser2.setPassword("passwordfalse");

        // check that an error is thrown
        String exceptionMessage = "Wrong password, please try again.";
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> userService.checkForLogin(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getMessage());

        usersCollection.deleteOne(eq("username", "testUsername"));

    }

    // Checks if the status of user is OFFLINE if the user logs out
    @Test
    public void checkForLogout_success() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User createdUser = userService.createUser(testUser);

        User loggedInUser;

        loggedInUser = userService.checkForLogin(createdUser);

        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());

        userService.logoutUser(loggedInUser.getId());

        User loggedOutUser = DatabaseConnectorUser.getUserById(loggedInUser.getId());

        assertEquals(UserStatus.OFFLINE, loggedOutUser.getStatus());

        usersCollection.deleteOne(eq("username", "testUsername"));

    }

    // Checks if the users credentials are updated if the user changes the credentials
    @Test
    public void changeCredentials_validInputs_success() {
        // given
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        // not tested, Id, Token, creation Date,status, favorite locations
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getPassword(), createdUser.getPassword());

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setName("testNamenew");
        userPutDTO.setUsername("testUsernamenew");
        userPutDTO.setPassword("passwordnew");

        userService.updateUser(createdUser.getId(), userPutDTO);

        User updatedUser = DatabaseConnectorUser.getUserById(createdUser.getId());

        // then
        // not tested, Id, Token, creation Date,status, favorite locations
        assertEquals(updatedUser.getName(), userPutDTO.getName());
        assertEquals(updatedUser.getUsername(), userPutDTO.getUsername());
        assertEquals(updatedUser.getPassword(), userPutDTO.getPassword());

        usersCollection.deleteOne(eq("username", "testUsernamenew"));
    }




}
