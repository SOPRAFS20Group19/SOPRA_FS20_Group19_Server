package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorUser;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;

import static com.mongodb.client.model.Filters.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mongodb.client.*;
import org.bson.Document;


import java.util.ArrayList;
import java.util.function.BooleanSupplier;

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
    static MongoDatabase locationChats = mongoClient.getDatabase("LocationChats");

    @Autowired
    static MongoCollection<Document> friendsChatsCollection = locationChats.getCollection("FriendsChats");

    @Autowired
    private UserService userService;

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

    // Checks if the user can add and delete a friend
    @Test
    public void addFriendAndDeleteFriend_success() {
        FindIterable<Document> request = usersCollection.find(eq("username", "testUsername"));
        Document user = request.first();
        assertNull(user);

        FindIterable<Document> request2 = usersCollection.find(eq("username", "testUsername1"));
        Document user2 = request2.first();
        assertNull(user2);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User userWantsToAddFriend = userService.createUser(testUser);


        User testUser2 = new User();
        testUser2.setName("testName1");
        testUser2.setUsername("testUsername1");
        testUser2.setPassword("password1");
        User addedUser = userService.createUser(testUser2);


        // Adds the User 'addedUser' to the Friends List of the User'userWantsToAddFriend'
        userService.addFriend(userWantsToAddFriend.getId(), addedUser.getId());

        boolean friends;

        friends = userService.checkFriend(userWantsToAddFriend.getId(), addedUser.getId());

        assertTrue(friends);

        // Deletes the User 'addedUser' of the Friends List of the User'userWantsToAddFriend'
        userService.deleteFriend(userWantsToAddFriend.getId(), addedUser.getId());

        boolean friends1;

        friends1 = userService.checkFriend(userWantsToAddFriend.getId(), addedUser.getId());

        assertFalse(friends1);

        friendsChatsCollection.deleteOne(or(eq("userId1", userWantsToAddFriend.getId()), eq("userId2", userWantsToAddFriend.getId())));
        usersCollection.deleteOne(eq("username", "testUsername"));
        usersCollection.deleteOne(eq("username", "testUsername1"));
    }

    @Test
    public void friendsPostAndDeleteMessage_success(){
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User friend1 = userService.createUser(testUser);


        User testUser2 = new User();
        testUser2.setName("testName1");
        testUser2.setUsername("testUsername1");
        testUser2.setPassword("password1");
        User friend2 = userService.createUser(testUser2);


        // add the users as friends in order to create a chat
        userService.addFriend(friend1.getId(), friend2.getId());
        userService.addFriend(friend2.getId(), friend1.getId());

        Message message = new Message();
        message.setSenderId(friend1.getId());
        message.setContent("TestMessage");
        message.setSenderUsername(friend1.getUsername());

        userService.postMessage(friend1.getId(), friend2.getId(), message);

        ArrayList<Message> messages = userService.getChat(friend1.getId(), friend2.getId());

        int messageIdToBeDeleted = 0;

        for (Message message1: messages){
            assertEquals(message1.getContent(), message.getContent());
            assertEquals(message1.getSenderUsername(), message.getSenderUsername());
            messageIdToBeDeleted = message1.getMessageId();
        }

        // check that the posting user has no unread messages and the receiving user does
        assertFalse(userService.checkUnreadMessages(friend1.getId(), friend2.getId()));
        assertTrue(userService.checkUnreadMessages(friend2.getId(), friend1.getId()));

        // delete message and assert that the chat is empty afterwards
        userService.deleteMessage(friend1.getId(), friend2.getId(), messageIdToBeDeleted);
        ArrayList<Message> messagesAfterDeletion = userService.getChat(friend1.getId(), friend2.getId());
        assertEquals(0, messagesAfterDeletion.size());

        friendsChatsCollection.deleteOne(or(eq("userId1", friend1.getId()), eq("userId2", friend1.getId())));
        usersCollection.deleteOne(eq("username", "testUsername"));
        usersCollection.deleteOne(eq("username", "testUsername1"));
    }

    @Test
    public void friendsPostMessageAndReadMessage_success(){
        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");
        User friend1 = userService.createUser(testUser);


        User testUser2 = new User();
        testUser2.setName("testName1");
        testUser2.setUsername("testUsername1");
        testUser2.setPassword("password1");
        User friend2 = userService.createUser(testUser2);


        // add the users as friends in order to create a chat
        userService.addFriend(friend1.getId(), friend2.getId());
        userService.addFriend(friend2.getId(), friend1.getId());

        Message message = new Message();
        message.setSenderId(friend1.getId());
        message.setContent("TestMessage");
        message.setSenderUsername(friend1.getUsername());

        userService.postMessage(friend1.getId(), friend2.getId(), message);

        // check that the posting user has no unread messages and the receiving user does
        assertFalse(userService.checkUnreadMessages(friend1.getId(), friend2.getId()));
        assertTrue(userService.checkUnreadMessages(friend2.getId(), friend1.getId()));

        // retrieve the chat from the perspective of friend2 and assert that both users have no unread messages afterwards
        userService.getChat(friend2.getId(), friend1.getId());
        assertFalse(userService.checkUnreadMessages(friend1.getId(), friend2.getId()));
        assertFalse(userService.checkUnreadMessages(friend2.getId(), friend1.getId()));

        friendsChatsCollection.deleteOne(or(eq("userId1", friend1.getId()), eq("userId2", friend1.getId())));
        usersCollection.deleteOne(eq("username", "testUsername"));
        usersCollection.deleteOne(eq("username", "testUsername1"));
    }

    @Test
    public void getAllUsers_success(){
        // Tests if all users are in the array list
        User testUser1 = new User();
        testUser1.setName("testName1");
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("password1");
        User user1 = userService.createUser(testUser1);

        User testUser2 = new User();
        testUser2.setName("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setPassword("password2");
        User user2 = userService.createUser(testUser2);

        User testUser3 = new User();
        testUser3.setName("testName3");
        testUser3.setUsername("testUsername3");
        testUser3.setPassword("password3");
        User user3 = userService.createUser(testUser3);

        User testUser4 = new User();
        testUser4.setName("testName4");
        testUser4.setUsername("testUsername4");
        testUser4.setPassword("password4");
        User user4 = userService.createUser(testUser4);

        ArrayList<User> allUsers = userService.getAllUsers();


        for(User testFirstUser: allUsers){
            if(testFirstUser.getId()==(user1.getId())){
                user1=testFirstUser;
            }
            assertTrue(allUsers.contains(testFirstUser));
        }

        assertTrue(allUsers.size()>= 4);


        usersCollection.deleteOne(eq("username", "testUsername1"));
        usersCollection.deleteOne(eq("username", "testUsername2"));
        usersCollection.deleteOne(eq("username", "testUsername3"));
        usersCollection.deleteOne(eq("username", "testUsername4"));
    }


}
