package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LocationService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // get the users
        ArrayList<User> users = userService.getAllUsers();
        ArrayList<UserGetDTO> usersGetDTO = new ArrayList<>();
        // convert the users to the API representation
        for (User user : users){
            usersGetDTO.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return usersGetDTO;
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserById(@PathVariable int userId) {
        // get the user corresponding to the given ID
        User user = userService.getUserById(userId);
        // convert the user to the API representation
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping ("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable int userId, @RequestBody UserPutDTO userPutDTO) {
        // updates the user identified by the given ID with the given data by the client
        userService.updateUser(userId, userPutDTO);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody UserPutDTO userPutDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // login user
        User loggedInUser = userService.checkForLogin(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
    }

    @PutMapping("/logout/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logoutUser(@PathVariable int userId){
        //log out user with given User id
        userService.logoutUser(userId);
    }

    @GetMapping("/users/friends/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getFriendsOfUser(@PathVariable int userId) {
        // get the friends corresponding to the given ID
        ArrayList<User> allFriends = userService.getFriends(userId);
        List<UserGetDTO> allFriendsGetDTO = new ArrayList<>();

        // convert the friends to the API representation
        for (User friend : allFriends){
            allFriendsGetDTO.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(friend));
        }

        return allFriendsGetDTO;
    }

    @PutMapping("/users/friends/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void addFriend(@PathVariable int userId, @PathVariable int friendId){
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/friends/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteFriend(@PathVariable int userId, @PathVariable int friendId){
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/users/friends/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkIfFriend(@PathVariable int userId, @PathVariable int friendId){
        return userService.checkFriend(userId, friendId);
    }

    @GetMapping("/users/chats/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<MessageGetDTO> getChat(@PathVariable int userId, @PathVariable int friendId){
        ArrayList<Message> chat = userService.getChat(userId, friendId);
        ArrayList<MessageGetDTO> chatDTO = new ArrayList<>();

        for (Message message : chat){
            chatDTO.add(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message));
        }

        return chatDTO;
    }

    @GetMapping("/users/chats/news/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkUnreadMessages(@PathVariable int userId, @PathVariable int friendId){
        return userService.checkUnreadMessages(userId, friendId);
    }

    @PutMapping("/users/chats/{userId}/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void postMessageToFriend(@PathVariable int userId, @PathVariable int friendId, @RequestBody MessagePostDTO messagePostDTO){
        userService.postMessage(userId, friendId, DTOMapper.INSTANCE.convertMessagePostDTOToEntity(messagePostDTO));
    }

    @DeleteMapping("/users/chats/{userId}/{friendId}/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteMessage(@PathVariable Integer userId, @PathVariable Integer friendId, @PathVariable int messageId) {
        userService.deleteMessage(userId, friendId, messageId);
    }

}
