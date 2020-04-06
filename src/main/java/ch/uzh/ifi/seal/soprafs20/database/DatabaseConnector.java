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

    //connection to mongodb on the cloud with credentials of tim ehrensperger


    public static void createUser(User user) {
         MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://luloch:soprafs20@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");

         MongoDatabase testDatabase = mongoClient.getDatabase("test");
         MongoCollection<Document> testCollection = testDatabase.getCollection("hallo");
         Document doc = new Document("att", user.getUsername());
         testCollection.insertOne(doc);
    }


}
