package ch.uzh.ifi.seal.soprafs20.database;


import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;



public class DatabaseConnector {

    public DatabaseConnector(){}


    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://luloch:soprafs20@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");
    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");

    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase locationStorage = mongoClient.getDatabase("LocationStorage");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> fountainsCollection = usersDevelopment.getCollection("Fountains");


    //creates User in the database; called bi createUser() in UserService
    public static void createUser(User user) {

         Document doc = new Document("username", user.getUsername())
                                    .append("name", user.getName())
                                    .append("password", user.getPassword())
                                    .append("creation-date", user.getCreationDate());
         usersCollection.insertOne(doc);
    }

    public static List<Location> getFountains(){
        List<Document> fountainsList = fountainsCollection.find().into(new ArrayList<>());
        List<Location> fountainsListLocation = new ArrayList<>();
        for (Document fountain : fountainsList) {
            fountain.toJson();
            //convert Document to Location
            Location fountainLocation = fountainToLocation(fountain);
            //add Location to List of Locations
            fountainsListLocation.add(fountainLocation);
        }
        return fountainsListLocation;
    }

    public static Location fountainToLocation(Document document){
        Location newLocation = new Location();
        newLocation.setId(document.getInteger("objectid"));
        newLocation.setCoordinates((List) document.get("coordinates"));
        return newLocation;
    }


}
