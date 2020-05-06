package ch.uzh.ifi.seal.soprafs20.database;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.model.UpdateOptions;
import org.json.JSONArray;
import org.json.JSONObject;

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
    public static User createUser(User user) {
        ArrayList<Integer> emptyFriendsList = new ArrayList<>();
        Document doc = new Document("username", user.getUsername())
                .append("name", user.getName())
                .append("password", user.getPassword())
                .append("creation-date", user.getCreationDate())
                .append("online", false)
                .append("userId", DatabaseConnectorUser.generateId())
                .append("avatarNr", 0)
                .append("friendsList", emptyFriendsList);
        usersCollection.insertOne(doc);

        // creates an entry for the new user in the FavoriteLocations DB
        DatabaseConnectorFavoriteLocations.createEntry(doc.getInteger("userId"));

        User userToReturn = DatabaseConnectorUser.getUserInfo(doc);
        return userToReturn;
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

    //sets the online status of a user to either true or false
    public static void setOnlineStatus(User user, boolean b){
        usersCollection.updateOne(eq("username", user.getUsername()),
                set("online", b));
    }

    //sets the online status of a user to either true or false
    public static void setOnlineStatusById(int id, boolean b){
        usersCollection.updateOne(eq("userId", id),
                set("online", b));
    }


    //returns a user with a certain username
    public static User getUserByUsername(String username){
        FindIterable<Document> request =  usersCollection.find(eq("username", username));
        Document user = request.first();
        //create a new user representation
        User userRepresentation = DatabaseConnectorUser.getUserInfo(user);
        return userRepresentation;
    }

    //not finished
    public static User getUserById(int id){
        FindIterable<Document> request =  usersCollection.find(eq("userId", id)); //Temporary id-field does not exist by now
        Document user = request.first();

        if (user == null){
            throw new UserNotFoundException("This user could not be found");
        }

        //create a new user representation
        User userRepresentation = DatabaseConnectorUser.getUserInfo(user);
        return userRepresentation;
    }

    public static String getUsernameById(int id){
        FindIterable<Document> request =  usersCollection.find(eq("userId", id)); //Temporary id-field does not exist by now
        Document user = request.first();

        if (user == null){
            throw new UserNotFoundException("This user could not be found");
        }

        return user.getString("username");
    }

    //transforms BSON User into Backend User Representation
    public static User getUserInfo(Document user){
        User userRepresentation = new User();
        userRepresentation.setUsername(user.getString("username"));
        userRepresentation.setName(user.getString("name"));
        userRepresentation.setPassword(user.getString("password"));
        userRepresentation.setCreationDate(user.getString("creation-date"));
        userRepresentation.setId(user.getInteger("userId", 0)); //if user has no id he gets assigned id: 0, flag that something is not working properly
        userRepresentation.setStatus(user.getBoolean("online")? UserStatus.ONLINE : UserStatus.OFFLINE);
        userRepresentation.setAvatarNr(user.getInteger( "avatarNr", 0)); //if user has no avatar chosen he gets the default value 0
        userRepresentation.setFriendsList(getFriends(userRepresentation.getId()));

        return userRepresentation;
    }

    //Helper function which generates a unique user id
    public static int generateId(){
        int random = (int) (Math.random() * 100000);
        FindIterable<Document> request = usersCollection.find(eq("userId", random));
        if (request.first() != null){random = DatabaseConnectorUser.generateId();};
        return random;
    }

    //returns all users represented as User.class
    public static ArrayList<User> getAllUsers(){
        List<Document> request = usersCollection.find().into(new ArrayList<>());
        ArrayList<User> allUsers = new ArrayList<>();
        for (Document doc : request){
            User tempUser = DatabaseConnectorUser.getUserInfo(doc);
            allUsers.add(tempUser);
        }
        return allUsers;
    }

    //updates the name of a user
    public static void updateName(User userToUpdate){
        usersCollection.updateOne(eq("userId", userToUpdate.getId()),
                set("name", userToUpdate.getName()));
    }

    //updates the username of a user
    public static void updateUsername(User userToUpdate){
        usersCollection.updateOne(eq("userId", userToUpdate.getId()),
                set("username", userToUpdate.getUsername()));
    }

    //updates the username of a user
    public static void updatePassword(User userToUpdate){
        usersCollection.updateOne(eq("userId", userToUpdate.getId()),
                set("password", userToUpdate.getPassword()));
    }

    //updates the new avatar number
    public static void updateAvatarNr(User userToUpdate){
        usersCollection.updateOne(eq("userId", userToUpdate.getId()),
                set("avatarNr", userToUpdate.getAvatarNr()));
    }

    public static void addFriend(int addingUserId, int toBeAddedUserId){
        ArrayList<Integer> currentFriends = getFriends(addingUserId);
        boolean shouldAdd = true;
        // check if friend to be added is already included in list
        for (Integer friendId : currentFriends){
            if (friendId == toBeAddedUserId) {
                shouldAdd = false;
                break;
            }
        }
        if (shouldAdd){
            usersCollection.updateOne(eq("userId", addingUserId), Updates.addToSet("friendsList", toBeAddedUserId));
        }
    }

    public static void deleteFriend(int deletingUserId, int toBeDeletedUserId){
        usersCollection.updateOne(eq("userId", deletingUserId),
                Updates.pull("friendsList", toBeDeletedUserId));
    }

    public static ArrayList<Integer> getFriends(int userId){
        FindIterable<Document> request =  usersCollection.find(eq("userId", userId));
        Document user = request.first();

        String userAsString = user.toJson();
        JSONObject userAsJson = new JSONObject(userAsString);

        if (userAsJson.getJSONArray("friendsList") == null){

        }
        JSONArray friendsListJson = userAsJson.getJSONArray("friendsList");
        ArrayList<Integer> friendsList = new ArrayList<>();

        for (int i = 0; i < friendsListJson.length(); i++){
            friendsList.add(friendsListJson.getInt(i));
        }

        return friendsList;
    }

    // initial setup to give all existing users an empty friends List
    public static void friendsListSetup(){
        ArrayList<User> allUsers = getAllUsers();

        for (User user : allUsers){
            ArrayList<Integer> emptyFriendsList = new ArrayList<>();
            Document updatedDoc = new Document().append("$set", new Document()
                    .append("friendsList", emptyFriendsList));

            usersCollection.updateOne(eq("userId", user.getId()), updatedDoc);
        }
    }

}
