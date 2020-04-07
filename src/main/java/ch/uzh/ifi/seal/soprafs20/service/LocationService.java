package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnector;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import org.bson.Document;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Location Service
 * This class is the "worker" and responsible for all functionality related to the location
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    public LocationService(){}

    public List<Location> getLocations(){
        List<Location> listFountains = DatabaseConnector.getFountains();
        return listFountains;
    }

    public Location getLocation(Long id){
        Location location = new Location();
        return location;
    }

    public Location createLocation(Location newLocation){
        Location location = new Location();
        return location;
    }

    public void updateLocation(Location location){
    }

    public Chat getChat(Long id){
        Chat chat = new Chat();
        return chat;
    }

    public void postMessage(Long id, Message message){
    }

    public List<Location> getFavoriteLocations(Long userId){
        List<Location> allLocations = new ArrayList<>();
        return allLocations;
    }

    public void updateFavoriteLocations(Long userId, Long locationId){
    }

    public void getWebcam(Long locationId){}
}
