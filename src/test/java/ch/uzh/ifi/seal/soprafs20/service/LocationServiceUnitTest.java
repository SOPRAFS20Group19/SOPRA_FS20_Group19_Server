package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnector;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.DuplicatedUserException;
import ch.uzh.ifi.seal.soprafs20.exceptions.InvalidCredentialsException;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
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


@WebAppConfiguration
@SpringBootTest
public class LocationServiceUnitTest {

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

    /*
    @BeforeEach
    public void setUp(){
        Location testFountain = new Location();
        testFountain.setLocationType(LocationType.FOUNTAIN);
        testFountain.setLongitude(0);
        testFountain.setLatitude(0);

        locationService.createLocation(testFountain);

    }

    @AfterEach
    public void tearDown(){
        fountainsCollection.deleteOne(eq("closestStreet", "Schwarzbächlistrasse"));
    }

     */

    @Test
    public void testGetLocation(){
        Location testFountain = new Location();
        testFountain.setLocationType(LocationType.FOUNTAIN);
        testFountain.setLongitude(0);
        testFountain.setLatitude(0);
        testFountain.setBaujahr(1901);

        Location newLocation = locationService.createLocation(testFountain);
        int locationId = newLocation.getId();

        assertTrue(newLocation.getBaujahr()==locationService.getLocation(locationId).getBaujahr(), "Baujahr property doesn't fit! -> GetLocation doesn't work properly.");
        assertTrue(newLocation.getLongitude()==locationService.getLocation(locationId).getLongitude(), "Longitude property doesn't fit! -> GetLocation doesn't work properly.");
        assertTrue(newLocation.getLatitude()==locationService.getLocation(locationId).getLatitude(), "Latitude property doesn't fit! -> GetLocation doesn't work properly.");

        fountainsCollection.deleteOne(eq("closestStreet", "Schwarzbächlistrasse"));

        //TODO: WE NEED BETTER DELETION OVER ID!!!!!! Doesnt't Work
        //FindIterable<Document> request = fountainsCollection.find(eq("properties", Document.parse("{objectid: " + newLocation.getId() + "}")));
        //fountainsCollection.deleteOne(request.first());

    }



    /**
     * This method is used to test if method getLocation throws correct exception
     */

    @Test
    public void testNullGetLocation(){
        try{
            locationService.getLocation(1000000000);
        }catch (LocationNotFoundException l){
            return;
        }
        fail("LocationNotFoundException expected");
    }

}