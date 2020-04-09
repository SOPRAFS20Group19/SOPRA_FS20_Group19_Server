package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnector;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import org.hibernate.dialect.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.crypto.Data;
import java.sql.Timestamp;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    // returns all users in the database
    public List<User> getUsers() {
        return DatabaseConnector.getAllUsers();
    }

    // returns a user in the repository identified by his ID
    public User getUser(Long id){
        List<User> users = this.userRepository.findAll();

        User userToReturn = null;

        for (User user: users){
            if (id.equals(user.getId())){
                userToReturn = user;
            }
        }

        if (userToReturn == null){
            throw new UserNotFoundException("This user could not be found.");
        }

        return userToReturn;
    }

    // updates a user with the updated username and birthdate
    public void updateUser(Long userId, UserPutDTO userWithNewData){
        List<User> users = this.userRepository.findAll();

        User userToUpdate = null;

        for (User user: users){
            if (userId.equals(user.getId())){
                userToUpdate = user;
            }
        }

        if (userToUpdate == null){
            throw new UserNotFoundException("This user could not be found.");
        }

        if (userWithNewData.getUsername() != null){
            userToUpdate.setUsername(userWithNewData.getUsername());
        }

        if (userWithNewData.getBirthDate() != null){
            userToUpdate.setBirthDate(userWithNewData.getBirthDate());
        }

        userRepository.save(userToUpdate);
        userRepository.flush();
    }

    // logs out all users
    public void logoutUsers(){
        List<User> users = this.userRepository.findAll();

        for (User user: users){
            user.setStatus(UserStatus.OFFLINE);
        }
    }

    // checks if the user that is attempting a login has the correct credentials
    public User checkForLogin(User userToBeLoggedIn){

        boolean validCredentials = DatabaseConnector.checkIfUserAndPasswordExist(userToBeLoggedIn.getUsername(), userToBeLoggedIn.getPassword());

        //Check if user is allowed to log in
        if (!validCredentials){ //case that the credentials are invalid
            boolean validUsername = DatabaseConnector.checkIfUsernameExists(userToBeLoggedIn.getUsername());
            //check if the username is valid, if yes, the password must be false
            String message = validUsername ? "Wrong password, please try again." : "This username does not exist, please register first.";
            throw new InvalidCredentialsException(message);
        }

        //If credentials are valid return new user representation
        return DatabaseConnector.getUserByUsername(userToBeLoggedIn.getUsername());
    }

    // creates a new user in the user repository
    public User createUser(User newUser) {
        newUser.setCreationDate(UserService.getCurrentDate());
        //Checks whether username is already taken
        if (DatabaseConnector.checkIfUsernameExists(newUser.getUsername())){
            throw new DuplicatedUserException("The provided username is already taken. Please try a new one.");
        }
        //If the username is not already taken create a new User in the database
        DatabaseConnector.createUser(newUser);

        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws SopraServiceException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByName = userRepository.findByName(userToBeCreated.getName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null && userByName != null) {
            throw new DuplicatedUserException("The username and the name provided are not unique. Therefore, the user could not be created!");
        }
        else if (userByUsername != null) {
            throw new DuplicatedUserException("The username provided is not unique. Therefore, the user could not be created!");

        }
        else if (userByName != null) {
            throw new DuplicatedUserException("The name provided is not unique. Therefore, the user could not be created!");

        }
    }

    // creates a timestamp of the current date for the creation date during the registration
    public static String getCurrentDate(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp.toString();
    }
}
