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

