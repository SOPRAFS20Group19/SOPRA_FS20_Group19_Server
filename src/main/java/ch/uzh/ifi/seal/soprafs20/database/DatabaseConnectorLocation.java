package ch.uzh.ifi.seal.soprafs20.database;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectorLocation {
    public DatabaseConnectorLocation(){}

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
