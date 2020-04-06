package ch.uzh.ifi.seal.soprafs20.database;


import ch.uzh.ifi.seal.soprafs20.entity.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

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

    //connection to the user database (development purposes only)
    static MongoDatabase userDatabase = mongoClient.getDatabase("UsersDevelopment");

    //connection to the users collection (development purposes only)
    static MongoCollection<Document> usersCollection = userDatabase.getCollection("Users");


    //method that inserts a new user into the database; called by createUser() in UserService
    public static void createUser(User user) {
         Document doc = new Document("username", user.getUsername())
                                    .append("name", user.getName())
                                    .append("password", user.getPassword())
                                    .append("creation-date", user.getCreationDate());
         usersCollection.insertOne(doc);
    }




}
