package ch.uzh.ifi.seal.soprafs20.database;


import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.database.key.Credentials;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;


public class DatabaseConnectorFavoriteLocations {
    public DatabaseConnectorFavoriteLocations(){}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            Credentials.getMongoCredentials());
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase favoriteLocations = mongoClient.getDatabase("FavoriteLocations");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> locationsCollection = favoriteLocations.getCollection("Locations");

    //creates Entry in the database
    public static void createEntry(int userId) {
        ArrayList<Integer> emptyFavorites = new ArrayList<Integer>();
        Document doc = new Document("userId", userId)
                .append("favoriteLocations", emptyFavorites);
        locationsCollection.insertOne(doc);
    }

    public static ArrayList<Integer> getFavoriteLocations(int userId){
        FindIterable<Document> request =  locationsCollection.find(eq("userId", userId));
        Document user = request.first();

        if (user == null){
            throw new UserNotFoundException("This user could not be found");
        }

        JSONObject userAsJSON = new JSONObject(user.toJson());
        JSONArray favoriteLocationsJSON = userAsJSON.getJSONArray("favoriteLocations");
        ArrayList<Integer> favoriteLocations = new ArrayList<>();
        for (int i = 0; i < favoriteLocationsJSON.length(); i++) {
            favoriteLocations.add(favoriteLocationsJSON.getInt(i));
        }

        return favoriteLocations;
    }

    //updates the list of favorite locations of a user
    public static void updateFavoriteLocations(int id, ArrayList<Integer> favorites){
        locationsCollection.updateOne(eq("userId", id),
                set("favoriteLocations", favorites));
    }
}
