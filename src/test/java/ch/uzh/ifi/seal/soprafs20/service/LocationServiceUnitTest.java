package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;


import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.*;
import org.bson.Document;

import ch.uzh.ifi.seal.soprafs20.exceptions.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


@WebAppConfiguration
@SpringBootTest
public class LocationServiceUnitTest {

    @Autowired
    LocationService locationService;

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