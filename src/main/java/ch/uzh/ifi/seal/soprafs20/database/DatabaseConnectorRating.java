package ch.uzh.ifi.seal.soprafs20.database;


import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;


public class DatabaseConnectorRating {
    public DatabaseConnectorRating(){}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase ratingLocation = mongoClient.getDatabase("RatingLocation");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> ratingsCollection = ratingLocation.getCollection("Ratings");

    public static int getRating(int userId, int locationId){
        FindIterable<Document> request =  ratingsCollection.find(and((eq("userId", userId)), (eq("locationId", locationId))));
        Document test = request.first();
        int total = 0;
        int finalRating=0;
        int timesRated=0;
        if (test != null) {
            for (Document singleRating : request) {
                JSONObject ratingAsJSON = new JSONObject(singleRating.toJson());
                int oneRating = ratingAsJSON.getInt("rating");
                total = total + oneRating;
                timesRated = timesRated + 1;
            }
            finalRating = total / timesRated;
        }
        return finalRating;
    }

    public static double getAverageRating(Integer locationId){
        FindIterable<Document> request = ratingsCollection.find((eq("locationId", locationId)));
        Document noDoc = request.first();
        double total = 0;
        double finalRating = 0;
        int timesRated = 0;
        if (noDoc == null){
            return finalRating;
        }

        for (Document doc : request){
            JSONObject ratingAsJSON = new JSONObject(doc.toJson());
            int oneRating = ratingAsJSON.getInt("rating");
            total += oneRating;
            timesRated++;
        }
        finalRating = (double) Math.round(100*(total/timesRated))/100;
        return finalRating;
    }

    //updates the rating
    public static void updateRating(int userId, int locationId, int rating){
        FindIterable<Document> request =  ratingsCollection.find(and((eq("userId", userId)), (eq("locationId", locationId))));
        Document test = request.first();
        if (test == null){
            Document doc = new Document("userId", userId)
                    .append("locationId", locationId)
                    .append("rating", rating);
            ratingsCollection.insertOne(doc);
        }
        else{ratingsCollection.updateOne(and(eq("userId", userId), (eq("locationId", locationId))),
                set("rating", rating));}
    }
}
