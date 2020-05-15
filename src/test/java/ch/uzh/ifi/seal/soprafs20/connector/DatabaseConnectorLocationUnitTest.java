package ch.uzh.ifi.seal.soprafs20.connector;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorUser;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.*;
import org.bson.Document;

import ch.uzh.ifi.seal.soprafs20.exceptions.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


@WebAppConfiguration
public class DatabaseConnectorLocationUnitTest {
    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase locationStorage = mongoClient.getDatabase("LocationStorage");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> fountainsCollection = locationStorage.getCollection("Fountains");
    //Establish connection to the Fireplaces Collection (development purposes only)
    static MongoCollection<Document> fireplacesCollection = locationStorage.getCollection("Fireplaces");
    //Establish connection to the Recycling Collection (development purposes only)
    static MongoCollection<Document> recyclingCollection = locationStorage.getCollection("Recycling");

    static MongoCollection<Document> userFountainsCollection = locationStorage.getCollection("UserFountains");

    static MongoCollection<Document> userFireplacesCollection = locationStorage.getCollection("UserFireplaces");

    static MongoCollection<Document> userRecyclingCollection = locationStorage.getCollection("UserRecycling");

    static MongoCollection<Document> toiletsCollection = locationStorage.getCollection("WC");

    static MongoCollection<Document> userToiletsCollection = locationStorage.getCollection("UserWC");

    static MongoCollection<Document> userTableTennisCollection = locationStorage.getCollection("UserTableTennis");

    static MongoCollection<Document> userBenchCollection = locationStorage.getCollection("UserBench");

    @Test
    public void generateFountainIdTest(){
        int id = DatabaseConnectorLocation.generateFountainId();
        List<Location> request =  DatabaseConnectorLocation.getFountains();
        List<Location> request2 = DatabaseConnectorLocation.getUserFountains();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
        for(Location used:request2){
            assertFalse(used.getId()==id);
        }
    }

    @Test
    public void generateRecyclingStationIdTest(){
        int id = DatabaseConnectorLocation.generateRecyclingId();
        List<Location> request =  DatabaseConnectorLocation.getRecyclingStations();
        List<Location> request2 = DatabaseConnectorLocation.getUserRecycling();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
        for(Location used:request2){
            assertFalse(used.getId()==id);
        }
    }

    @Test
    public void generateFireplaceIdTest(){
        int id = DatabaseConnectorLocation.generateFireplaceId();
        List<Location> request =  DatabaseConnectorLocation.getFireplaces();
        List<Location> request2 = DatabaseConnectorLocation.getUserFireplaces();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
        for(Location used:request2){
            assertFalse(used.getId()==id);
        }
    }

    @Test
    public void generateToiletsIdTest(){
        int id = DatabaseConnectorLocation.generateToiletId();
        List<Location> request =  DatabaseConnectorLocation.getToilets();
        List<Location> request2 = DatabaseConnectorLocation.getUserToilets();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
        for(Location used:request2){
            assertFalse(used.getId()==id);
        }
    }

    @Test
    public void generateBenchIdTest(){
        int id = DatabaseConnectorLocation.generateBenchId();
        List<Location> request =  DatabaseConnectorLocation.getUserBench();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
    }

    @Test
    public void generateTableTennisIdTest(){
        int id = DatabaseConnectorLocation.generateTableTennisId();
        List<Location> request =  DatabaseConnectorLocation.getUserTableTennis();
        for(Location used:request){
            assertFalse(used.getId()==id);
        }
    }
}

