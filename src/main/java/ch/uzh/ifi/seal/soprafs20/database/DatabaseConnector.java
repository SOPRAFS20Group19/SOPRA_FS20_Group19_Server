package ch.uzh.ifi.seal.soprafs20.database;


import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import com.mongodb.*;
import com.mongodb.client.*;

import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

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
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");
    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");

    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase locationStorage = mongoClient.getDatabase("LocationStorage");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> fountainsCollection = locationStorage.getCollection("Fountains");
    //Establish connection to the Fireplaces Collection (development purposes only)
    static MongoCollection<Document> fireplacesCollection = locationStorage.getCollection("Fireplaces");
    //Establish connection to the Recycling Collection (development purposes only)
    static MongoCollection<Document> recyclingCollection = locationStorage.getCollection("Recycling");


    //creates User in the database; called bi createUser() in UserService
    public static void createUser(User user) {

         Document doc = new Document("username", user.getUsername())
                                    .append("name", user.getName())
                                    .append("password", user.getPassword())
                                    .append("creation-date", user.getCreationDate());
         usersCollection.insertOne(doc);
    }

    //Checks if user tried to login with valid credentials
    public static boolean checkIfUserAndPasswordExist(String username, String password){
        Document user = (Document) usersCollection.find(and(eq("username", username), eq("password", password)));
        if (user == null){
            return false;
        }
        return true;
    }

    //checks if user provided a valid username
    public static boolean checkIfUsernameExists(String username){
        Document user = (Document) usersCollection.find(eq("username", username));
        if (user == null){
            return false;
        }
        return true;
    }


    //returns a user with a certain username
    public static User getUserByUsername(String username){
        Document user = (Document) usersCollection.find(eq("username", username));
        //create a new user representation
        User userRepresentation = new User();
        userRepresentation.setUsername(user.getString("username"));
        userRepresentation.setName(user.getString("name"));
        userRepresentation.setPassword(user.getString("password"));
        userRepresentation.setCreationDate(user.getString("creation-date"));
        return userRepresentation;
    }

    public static void getFountainById(int id){
        Document fountainOne = fountainsCollection.find().first();

        //Document fountain = (Document) fountainsCollection.find(eq("properties.objectid", id));
        //return fountainToLocation(fountainOne);
    }

    public static List<Location> getFountains() throws JSONException {
        List<Document> fountainsList = fountainsCollection.find().into(new ArrayList<>());
        List<Location> fountainsListLocation = new ArrayList<>();
        for (Document fountain : fountainsList) {
            String fountainAsString = fountain.toJson();
            JSONObject fountainAsJson = new JSONObject(fountainAsString);
            //convert Document to Location
            Location fountainLocation = fountainToLocation(fountainAsJson);
            //add Location to List of Locations
            fountainsListLocation.add(fountainLocation);
        }
        return fountainsListLocation;
    }

    public static List<Location> getFireplaces(){
        List<Document> fireplacesList = fireplacesCollection.find().into(new ArrayList<>());
        List<Location> fireplacesListLocation = new ArrayList<>();
        for (Document fireplace: fireplacesList){
            String fireplaceAsString = fireplace.toJson();
            JSONObject fireplaceAsJSON = new JSONObject(fireplaceAsString);
            //convert Document to Location
            Location fireplaceLocation = fireplaceToLocation(fireplaceAsJSON);
            //add Location to List of Locations
            fireplacesListLocation.add(fireplaceLocation);
        }
        return fireplacesListLocation;
    }

    public static List<Location> getRecyclingStations(){
        List<Document> recyclingList = recyclingCollection.find().into(new ArrayList<>());
        List<Location> recyclingListLocation = new ArrayList<>();
        for (Document recyclingStation: recyclingList){
            String recyclingAsString = recyclingStation.toJson();
            JSONObject recyclingAsJSON = new JSONObject(recyclingAsString);
            //convert Document to Location
            Location recyclingLocation = recyclingToLocation(recyclingAsJSON);
            //add Location to List of Locations
            recyclingListLocation.add(recyclingLocation);
        }
        return recyclingListLocation;
    }

    public static Location fountainToLocation(JSONObject fountainAsJson) throws JSONException {
        Location newLocation = new Location();

        JSONObject properties = fountainAsJson.getJSONObject("properties");
        JSONObject geometry = fountainAsJson.getJSONObject("geometry");

        newLocation.setId(properties.getInt("objectid"));

        JSONArray coordinatesAsJSON = (JSONArray) geometry.get("coordinates");
        double lat = coordinatesAsJSON.getDouble(1);
        double lon = coordinatesAsJSON.getDouble(0);
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);
        newLocation.setLocationType(LocationType.FOUNTAIN);
        return newLocation;
    }

    public static Location fireplaceToLocation(JSONObject fireplaceAsJSON){
        Location newLocation = new Location();

        JSONObject barbecuePlace = fireplaceAsJSON.getJSONObject("BarbecuePlace");

        newLocation.setId(barbecuePlace.getInt("Id"));
        double lat = barbecuePlace.getDouble("Latitude");
        double lon = barbecuePlace.getDouble("Longitude");
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);
        newLocation.setLocationType(LocationType.FIREPLACE);
        return newLocation;
    }

    public static Location recyclingToLocation(JSONObject recyclingAsJSON){
        Location newLocation = new Location();

        JSONObject properties = recyclingAsJSON.getJSONObject("properties");
        JSONObject geometry = recyclingAsJSON.getJSONObject("geometry");
        JSONArray coordinatesAsJSON = (JSONArray) geometry.get("coordinates");

        newLocation.setId(Integer.parseInt(properties.getString("objectid")));
        double lat = coordinatesAsJSON.getDouble(1);
        double lon = coordinatesAsJSON.getDouble(0);
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);
        newLocation.setLocationType(LocationType.RECYCLING_STATION);
        return newLocation;
    }


}
