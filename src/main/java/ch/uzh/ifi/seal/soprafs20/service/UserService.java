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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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

    // returns all users in the repository
    public List<User> getUsers() {
        return this.userRepository.findAll();
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
        this.logoutUsers();
        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

        if (userByUsername == null) {
            throw new InvalidCredentialsException("This username does not exist, please register first.");
        }

        userByUsername.setStatus(UserStatus.ONLINE);

        String passwordToBeChecked = userToBeLoggedIn.getPassword();
        String correctPassword = userByUsername.getPassword();

        String nameToBeChecked = userToBeLoggedIn.getName();
        String correctName = userByUsername.getName();

        if (!passwordToBeChecked.equals(correctPassword)){
            throw new InvalidCredentialsException("Wrong password, please try again.");
        }

        if (!nameToBeChecked.equals(correctName)){
            throw new InvalidCredentialsException("Wrong name, please try again.");
        }

        return userByUsername;
    }

    // creates a new user in the user repository
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(getCurrentDate());

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        //newUser = userRepository.save(newUser);
        //userRepository.flush();
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
    public String getCurrentDate(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp.toString();
    }
}
