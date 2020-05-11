package ch.uzh.ifi.seal.soprafs20.database;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.exceptions.LocationNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;


public class DatabaseConnectorLocationChats {
    public DatabaseConnectorLocationChats() {}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase LocationChats = mongoClient.getDatabase("LocationChats");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> chatsCollection = LocationChats.getCollection("Chats");
    static MongoCollection<Document> friendsChatsCollection = LocationChats.getCollection("FriendsChats");

    // only used for initializing the DB collection. Do not run again
    public static void initialSetup(){
        // set up array for locationIds
        ArrayList<Integer> locationIds = new ArrayList<>();

        // get all locations
        List<Location> allLocations = new ArrayList<>();
        List<Location> listFountains = DatabaseConnectorLocation.getFountains();
        List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
        List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();

        allLocations.addAll(listFountains);
        allLocations.addAll(listFireplaces);
        allLocations.addAll(listRecyclingStations);

        // add all locationsIds to the array
        for (Location location : allLocations){
            locationIds.add(location.getId());
        }

        // insert a doc with an empty chat for each location
        for (Integer id : locationIds){
            ArrayList<Document> emptyChat = new ArrayList<>();
            Document doc = new Document("locationId", id)
                    .append("messages", emptyChat);
            chatsCollection.insertOne(doc);
        }
    }

    public static void createNewFriendsChat(Integer userId1, Integer userId2){
        if (userId1 > userId2){
            Integer temp = userId1;
            userId1 = userId2;
            userId2 = temp;
        }

        ArrayList<Document> emptyChat = new ArrayList<>();
        Document doc = new Document("userId1", userId1.toString())
                .append("userId2", userId2.toString())
                .append("readUser1", true)
                .append("readUser2", true)
                .append("messages", emptyChat);
        friendsChatsCollection.insertOne(doc);
    }

    public static ArrayList<Message> getChatFriends(Integer userId1, Integer userId2){
        FindIterable<Document> request =  friendsChatsCollection.find(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())));
        Document chatDoc = request.first();

        if (chatDoc == null){
            throw new UserNotFoundException("This user-friend pair could not be found");
        }

        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");
        ArrayList<Message> chat = new ArrayList<>();

        // iterate through all message objects in the chat object
        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            // convert the messageJSON to the Message entity type
            chat.add(convertMessageJSONToEntityFriends(messageJSON));
        }

        return chat;
    }

    public static boolean checkForExistingChat(Integer userId1, Integer userId2){
        FindIterable<Document> request =  friendsChatsCollection.find(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())));
        Document chatDoc = request.first();

        return chatDoc != null;
    }

    public static void setOnRead(Integer userId1, Integer userId2, Integer readerId){
        if (userId1.equals(readerId)){
            friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())), Updates.set("readUser1", true));
        }
        else {
            friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())), Updates.set("readUser2", true));
        }
    }

    public static void setOnUnread(Integer userId1, Integer userId2, Integer unreadId){
        if (userId1.equals(unreadId)){
            friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())), Updates.set("readUser1", false));
        }
        else {
            friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())), Updates.set("readUser2", false));
        }
    }

    public static boolean checkUnreadMessages(Integer userId1, Integer userId2, int checkId){
        FindIterable<Document> request =  friendsChatsCollection.find(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())));
        Document chatDoc = request.first();

        if (chatDoc == null){
            throw new UserNotFoundException("This user-friend pair could not be found");
        }

        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        boolean isReadByCheckingUser;
        if (userId1.equals(checkId)){
            isReadByCheckingUser = chatAsJSON.getBoolean("readUser1");
        }
        else {
            isReadByCheckingUser = chatAsJSON.getBoolean("readUser2");
        }

        return !isReadByCheckingUser;
    }

    public static void postMessageFriends(Integer senderId, Integer recipientId, Message message){
        Integer userId1;
        Integer userId2;
        if (senderId <= recipientId){
            userId1 = senderId;
            userId2 = recipientId;
        }
        else {
            userId1 = recipientId;
            userId2 = senderId;
        }


        Document doc = new Document("senderId", message.getSenderId().toString())
                .append("content", message.getContent())
                .append("timestamp", message.getTimestamp())
                .append("messageId", generateMessageIdFriends(userId1, userId2).toString());

        friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())), Updates.addToSet("messages", doc));
        setOnUnread(userId1, userId2, recipientId);
    }

    public static void deleteMessageFriends(Integer deletingId, Integer friendId, int messageId){
        Integer userId1;
        Integer userId2;
        if (deletingId <= friendId){
            userId1 = deletingId;
            userId2 = friendId;
        }
        else {
            userId1 = friendId;
            userId2 = deletingId;
        }
        FindIterable<Document> request =  friendsChatsCollection.find(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())));
        Document chatDoc = request.first();

        if (chatDoc == null){
            throw new UserNotFoundException("This user-friend pair could not be found");
        }

        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");

        Message messageToRemove = new Message();

        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            if (Integer.parseInt(messageJSON.getString("messageId")) == messageId){
                messageToRemove = convertMessageJSONToEntityFriends(messageJSON);
            }
        }

        friendsChatsCollection.updateOne(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())),
                Updates.pull("messages", convertEntityToDocumentFriends(messageToRemove)));
    }

    public static Integer generateMessageIdFriends(Integer userId1, Integer userId2){
        FindIterable<Document> request =  friendsChatsCollection.find(and(eq("userId1", userId1.toString()), eq("userId2", userId2.toString())));
        Document chatDoc = request.first();

        if (chatDoc == null){
            throw new UserNotFoundException("This user-friend pair could not be found");
        }

        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");

        Integer random = (int) (Math.random() * 100000);

        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            if (messageJSON.getInt("messageId") == random){
                random = generateMessageIdFriends(userId1, userId2);
            }
        }

        return random;
    }

    public static Message convertMessageJSONToEntityFriends(JSONObject messageJSON){
        Message message = new Message();

        Integer userId = Integer.parseInt(messageJSON.getString("senderId"));

        message.setSenderUsername(DatabaseConnectorUser.getUsernameById(userId));
        message.setSenderId(userId);
        message.setContent(messageJSON.getString("content"));
        message.setTimestamp(messageJSON.getString("timestamp"));
        message.setMessageId(messageJSON.getInt("messageId"));

        return message;
    }

    public static Document convertEntityToDocumentFriends(Message message){
        return new Document("senderId", message.getSenderId().toString())
                .append("content", message.getContent())
                .append("timestamp", message.getTimestamp())
                .append("messageId", message.getMessageId().toString());
    }

    //creates Entry in the database when a new location is created
    public static void addChatForNewLocation(int id){
        ArrayList<Document> emptyChat = new ArrayList<>();
        Document doc = new Document("locationId", id)
                .append("messages", emptyChat);
        chatsCollection.insertOne(doc);
    }

    public static ArrayList<Message> getChatLocations(Integer locationId){
        FindIterable<Document> request =  chatsCollection.find(eq("locationId", locationId));
        Document chatDoc = request.first();

        // throw exception if there is no chat for the given location
        if (chatDoc == null){
            throw new LocationNotFoundException("This location could not be found");
        }

        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");
        ArrayList<Message> chat = new ArrayList<>();

        // iterate through all message objects in the chat object
        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            // convert the messageJSON to the Message entity type
            chat.add(convertMessageJSONToEntityLocations(messageJSON));
        }

        return chat;
    }

    public static void postMessageLocations(Integer locationId, Message message){
        Document doc = new Document("senderId", message.getSenderId())
                .append("content", message.getContent())
                .append("timestamp", message.getTimestamp())
                .append("messageId", generateIdLocations(locationId));

        chatsCollection.updateOne(eq("locationId", locationId), Updates.addToSet("messages", doc));
    }

    //Helper function which generates a unique message id that is not taken yet by a message for this location chat
    public static int generateIdLocations(int locationId){
        FindIterable<Document> request = chatsCollection.find(eq("locationId", locationId));
        Document chatDoc = request.first();
        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");

        int random = (int) (Math.random() * 100000);

        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            if (messageJSON.getInt("messageId") == random){
                random = generateIdLocations(locationId);
            }
        }

        return random;
    }

    public static void deleteMessageLocations(Integer locationId, int messageId){
        FindIterable<Document> request =  chatsCollection.find(eq("locationId", locationId));
        Document chatDoc = request.first();
        JSONObject chatAsJSON = new JSONObject(chatDoc.toJson());
        JSONArray messagesJSON = chatAsJSON.getJSONArray("messages");

        Message messageToRemove = new Message();

        for (int i = 0; i < messagesJSON.length(); i++) {
            JSONObject messageJSON = messagesJSON.getJSONObject(i);
            if (messageJSON.getInt("messageId") == messageId){
                messageToRemove = convertMessageJSONToEntityLocations(messageJSON);
            }
        }

        chatsCollection.updateOne(eq("locationId", locationId),
                Updates.pull("messages", convertEntityToDocumentLocations(messageToRemove)));
    }

    public static Message convertMessageJSONToEntityLocations(JSONObject messageJSON){
        Message message = new Message();

        int userId = messageJSON.getInt("senderId");

        message.setSenderUsername(DatabaseConnectorUser.getUsernameById(userId));
        message.setSenderId(userId);
        message.setContent(messageJSON.getString("content"));
        message.setTimestamp(messageJSON.getString("timestamp"));
        message.setMessageId(messageJSON.getInt("messageId"));

        return message;
    }

    public static Document convertEntityToDocumentLocations(Message message){
        return new Document("senderId", message.getSenderId())
                .append("content", message.getContent())
                .append("timestamp", message.getTimestamp())
                .append("messageId", message.getMessageId());
    }
}
