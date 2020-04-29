package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Updates.*;

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
    }


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
    }

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

    }

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

    }

}
