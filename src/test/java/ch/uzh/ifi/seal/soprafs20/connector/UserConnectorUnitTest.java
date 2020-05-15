package ch.uzh.ifi.seal.soprafs20.connector;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorUser;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import java.lang.reflect.Field;


import static org.junit.jupiter.api.Assertions.*;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.*;
import org.bson.Document;

import ch.uzh.ifi.seal.soprafs20.exceptions.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@WebAppConfiguration
public class UserConnectorUnitTest {

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");

    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");

    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");


    @Test
    public void setOnlineStatusTest(){
        FindIterable<Document> request = usersCollection.find();
        Document user = request.first();

        //create a new user representation
        User userRepresentation = DatabaseConnectorUser.getUserInfo(user);

        DatabaseConnectorUser.setOnlineStatus(userRepresentation, true);
        User userRepresentation1= DatabaseConnectorUser.getUserInfo(user);

        assertEquals(UserStatus.ONLINE, userRepresentation1.getStatus());

        DatabaseConnectorUser.setOnlineStatus(userRepresentation, false);
        User userRepresentation2= DatabaseConnectorUser.getUserInfo(user);

        assertEquals(UserStatus.OFFLINE, userRepresentation2.getStatus());

    }

    @Test
    public void generateIdTest(){
        int id = DatabaseConnectorUser.generateId();
        FindIterable<Document> request =  usersCollection.find();
        for(Document used:request){
            User user2 = DatabaseConnectorUser.getUserInfo(used);
            assertFalse(user2.getId()==id);
        }
    }
}
