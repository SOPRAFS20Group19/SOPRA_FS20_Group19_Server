package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
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

    /* Currently not in use, drop???
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

     */

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
    public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

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
}
