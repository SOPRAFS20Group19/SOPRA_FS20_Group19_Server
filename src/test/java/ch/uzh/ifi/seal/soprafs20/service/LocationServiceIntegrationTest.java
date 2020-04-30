package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnector;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
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

import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorFavoriteLocations;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocationChats;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorRating;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.exceptions.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.FilterPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Updates.*;

/**
 * Test class for the LocationResource REST resource.
 *
 * @see LocationService
 */
@WebAppConfiguration
@SpringBootTest
public class LocationServiceIntegrationTest {

    @Autowired
    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    @Autowired
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase locationStorage = mongoClient.getDatabase("LocationStorage");
    @Autowired
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> fountainsCollection = locationStorage.getCollection("Fountains");
    @Autowired
    //Establish connection to the Fireplaces Collection (development purposes only)
    static MongoCollection<Document> fireplacesCollection = locationStorage.getCollection("Fireplaces");
    @Autowired
    //Establish connection to the Recycling Collection (development purposes only)
    static MongoCollection<Document> recyclingCollection = locationStorage.getCollection("Recycling");

    @Autowired
    static MongoCollection<Document> userFountainsCollection = locationStorage.getCollection("UserFountains");

    @Autowired
    static MongoCollection<Document> userFireplacesCollection = locationStorage.getCollection("UserFireplaces");

    @Autowired
    static MongoCollection<Document> userRecyclingCollection = locationStorage.getCollection("UserRecycling");

    @Autowired
    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");


    @Autowired
    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;



    /*@BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

     */

    @Test
    public void getLocation_validInputs_success(){
        //create location test??

        Location testFountain = new Location();
        testFountain.setLocationType(LocationType.FOUNTAIN);
        testFountain.setLongitude(0);
        testFountain.setLatitude(0);

        /*

        Location testRecycling = new Location();
        testRecycling.setLocationType(LocationType.RECYCLING_STATION);
        testRecycling.setLongitude(1);
        testRecycling.setLatitude(1);

        Location testFireplace = new Location();
        testFireplace.setLocationType(LocationType.FIREPLACE);
        testFireplace.setLongitude(2);
        testFireplace.setLatitude(2);*/

        Location newFountain = locationService.createLocation(testFountain);
        //Location newRecycling = locationService.createLocation(testRecycling);
        //Location newFireplace = locationService.createLocation(testFireplace);

        assertEquals(testFountain.getLocationType(), newFountain.getLocationType());
        assertEquals(testFountain.getLongitude(), newFountain.getLongitude());
        assertEquals(testFountain.getLatitude(), newFountain.getLatitude());

        /*
        assertEquals(testRecycling.getLocationType(), newRecycling.getLocationType());
        assertEquals(testRecycling.getLongitude(), newRecycling.getLongitude());
        assertEquals(testRecycling.getLatitude(), newRecycling.getLatitude());

        assertEquals(testFireplace.getLocationType(), newFireplace.getLocationType());
        assertEquals(testFireplace.getLongitude(), newFireplace.getLongitude());
        assertEquals(testFireplace.getLatitude(), newFireplace.getLatitude());*/

        // attention: deletion doesn't work!
        //fountainsCollection.deleteOne(eq("properties", Document.parse("objectid:" + newFountain.getId())));
        //fountainsCollection.deleteOne(eq("properties.objectid", newRecycling.getId()));
        //fountainsCollection.deleteOne(eq("properties.objectid", newFireplace.getId()));

    }

    @Test
    public void getFilteredLocations_success(){
        //Test Filter Fountains
        FilterPostDTO filterPostDTO = new FilterPostDTO();
        filterPostDTO.setFountains(true);
        filterPostDTO.setRecyclingStations(false);
        filterPostDTO.setFireplaces(false);
        List<Location> filteredLocations = locationService.getFilteredLocations(filterPostDTO);

        for(Location location:filteredLocations){
            assertEquals(location.getLocationType(), LocationType.FOUNTAIN);
        }

        //Test filter RecyclingStation
        FilterPostDTO filterPostDTO2 = new FilterPostDTO();
        filterPostDTO2.setFountains(false);
        filterPostDTO2.setRecyclingStations(true);
        filterPostDTO2.setFireplaces(false);
        List<Location> filteredLocations2 = locationService.getFilteredLocations(filterPostDTO2);

        for(Location location:filteredLocations2){
            assertEquals(location.getLocationType(), LocationType.RECYCLING_STATION);
        }

        //Test filter Fireplaces
        FilterPostDTO filterPostDTO3 = new FilterPostDTO();
        filterPostDTO3.setFountains(false);
        filterPostDTO3.setRecyclingStations(false);
        filterPostDTO3.setFireplaces(true);
        List<Location> filteredLocations3 = locationService.getFilteredLocations(filterPostDTO3);

        for(Location location:filteredLocations3){
            assertEquals(location.getLocationType(), LocationType.FIREPLACE);
        }
        //Noch alle falsch testen
    }

    //Chat??

    @Test
    public void locationChat(){
        // tests the chat
        Location testFountain = new Location();
        testFountain.setLocationType(LocationType.FOUNTAIN);
        testFountain.setLongitude(0);
        testFountain.setLatitude(0);
        Location newFountain = locationService.createLocation(testFountain);

        User testUser = new User();
        testUser.setName("testName");
        testUser.setUsername("testUsername");
        testUser.setPassword("password");

        // when
        User createdUser = userService.createUser(testUser);

        Message message = new Message();
        message.setSenderId(createdUser.getId());
        message.setContent("TestMessage");
        message.setSenderUsername(createdUser.getUsername());

        locationService.postMessage(newFountain.getId(), message);

        ArrayList<Message> messages = locationService.getChat(newFountain.getId());

        for (Message message1: messages){
            assertEquals(message1.getContent(), message.getContent());
            assertEquals(message1.getSenderUsername(), message.getSenderUsername());
        }

        usersCollection.deleteOne(eq("username", "testUsername"));
    }

}