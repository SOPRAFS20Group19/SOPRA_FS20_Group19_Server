package ch.uzh.ifi.seal.soprafs20.database;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DatabaseConnectorUser {
    public DatabaseConnectorUser(){}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Users Database (development purposes only)
    static MongoDatabase usersDevelopment = mongoClient.getDatabase("UsersDevelopment");
    //Establish connection to the Users Collection (development purposes only)
    static MongoCollection<Document> usersCollection = usersDevelopment.getCollection("Users");

    //creates User in the database; called bi createUser() in UserService
    public static void createUser(User user) {

        Document doc = new Document("username", user.getUsername())
                .append("name", user.getName())
                .append("password", user.getPassword())
                .append("creation-date", user.getCreationDate())
                .append("online", false);
        usersCollection.insertOne(doc);
    }

    //Checks if user tried to login with valid credentials
    public static boolean checkIfUserAndPasswordExist(String username, String password){
        FindIterable<Document> request = usersCollection.find(and(eq("username", username), eq("password", password)));
        Document user = request.first();
        if (user == null){
            return false;
        }
        return true;
    }

    //checks if user provided a valid username
    public static boolean checkIfUsernameExists(String username){
        FindIterable<Document> request = usersCollection.find(eq("username", username));
        Document user = request.first();
        if (user == null){
            return false;
        }
        return true;
    }

    //returns a user with a certain username
    public static User getUserByUsername(String username){
        FindIterable<Document> request =  usersCollection.find(eq("username", username));
        Document user = request.first();
        //create a new user representation
        User userRepresentation = DatabaseConnector.getUserInfo(user);
        return userRepresentation;
    }

    //transforms BSON User into Backend User Representation
    public static User getUserInfo(Document user){
        User userRepresentation = new User();
        userRepresentation.setUsername(user.getString("username"));
        userRepresentation.setName(user.getString("name"));
        userRepresentation.setPassword(user.getString("password"));
        userRepresentation.setCreationDate(user.getString("creation-date"));
        userRepresentation.setStatus(user.getBoolean("online")? UserStatus.ONLINE : UserStatus.OFFLINE);
        return userRepresentation;
    }

    //returns all users represented as User.class
    public static List<User> getAllUsers(){
        List<Document> request = usersCollection.find().into(new ArrayList<>());
        List<User> allUsers = new ArrayList<User>();
        for (Document doc : request){
            User tempUser = DatabaseConnector.getUserInfo(doc);
            allUsers.add(tempUser);
        }
        return allUsers;


    }
}
