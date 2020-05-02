package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorUser;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;

import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
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
public class UserServiceUnitTest {

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

    @BeforeEach
    public void setUp(){
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        testUser.setId(1000000);

        userService.createUser(testUser);
    }

    @AfterEach
    public void tearDown(){
        usersCollection.deleteOne(eq("username", "testUsername"));
        usersCollection.deleteOne(eq("username", "newTestUsername"));
    }

    /*@BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

     */

    /**
     * This method is used to test if method getUserById works properly
     */

    @Test
    public void testGetUserById() {
        //find user by username
        FindIterable<Document> request =  usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        //create a new user representation
        User userRepresentation = DatabaseConnectorUser.getUserInfo(user);
        int userId = userRepresentation.getId();

        //assertConditions
        assertTrue(userService.getUserById(userId).getName().equals("testName"), "Name of user found by Id not identical.");
        assertTrue(userService.getUserById(userId).getUsername().equals("testUsername"), "Username of user found by Id not identical.");
        assertTrue(userService.getUserById(userId).getPassword().equals("password"), "Password of user found by Id not identical.");
        assertFalse(userService.getUserById(userId).getName().equals("testName2"), "Name of user found by Id not identical.");
        assertFalse(userService.getUserById(userId).getUsername().equals("testUsername2"), "Name of user found by Id not identical.");
        assertFalse(userService.getUserById(userId).getPassword().equals("password2"), "Name of user found by Id not identical.");

    }

    /**
     * This method is used to test if method getUserById throws correct exception
     */

    @Test
    public void testNullGetUserById(){
        try{
            userService.getUserById(50000000);
        }catch (UserNotFoundException u){
            return;
        }
        fail("UserNotFoundException expected");

    }

    /**
     * This method is used to test if method updateUser works properly
     */

    @Test
    public void testUpdateUser(){
        //find user by username
        FindIterable<Document> request =  usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        //create a new user representation
        User userRepresentation = DatabaseConnectorUser.getUserInfo(user);
        int userId = userRepresentation.getId();

        //test that correct user was found
        assertTrue(userRepresentation.getName().equals("testName"), "Name of user before change was wrong.");
        assertTrue(userRepresentation.getUsername().equals("testUsername"), "Username of user before change was wrong.");
        assertTrue(userRepresentation.getPassword().equals("password"), "Password of user before change was wrong.");
        assertTrue(userRepresentation.getAvatarNr() ==(0), "Avatar of user before change was wrong.");

        //here the "real" testing starts
        //create userPutDTO, change Username, Name and Password
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("newTestUsername");
        userPutDTO.setName("newTestName");
        userPutDTO.setPassword("newPassword");
        userPutDTO.setAvatarId(6);

        //call the method which is to be tested. the updated properties of the user are stored in the userPutDTO
        userService.updateUser(userId, userPutDTO);

        //find updated user by Id
        FindIterable<Document> request2 =  usersCollection.find(eq("userId", userId));
        Document user2 = request2.first();

        //create a new user representation
        User updatedUserRepresentation = DatabaseConnectorUser.getUserInfo(user2);

        //test that user was updated properly
        assertTrue(updatedUserRepresentation.getName().equals("newTestName"), "Name of user after change was wrong.");
        assertTrue(updatedUserRepresentation.getUsername().equals("newTestUsername"), "Username of user after change was wrong.");
        assertTrue(updatedUserRepresentation.getPassword().equals("newPassword"), "Password of user after change was wrong.");
        assertTrue(updatedUserRepresentation.getAvatarNr() == 6, "Avatar of user after change was wrong.");
    }

    /**
     * This method is used to test if method updateUser throws correct exception
     */

    @Test
    public void testNullUpdateUser(){
        try{
            UserPutDTO userPutDTO = new UserPutDTO();
            userPutDTO.setUsername("newTestUsername");
            userPutDTO.setName("newTestName");
            userPutDTO.setPassword("newPassword");
            userService.updateUser(50000000, userPutDTO);
        }catch (UserNotFoundException u){
            return;
        }
        fail("UserNotFoundException expected");
    }

}

