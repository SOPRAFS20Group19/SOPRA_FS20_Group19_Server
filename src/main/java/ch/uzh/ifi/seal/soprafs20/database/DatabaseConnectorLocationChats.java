package ch.uzh.ifi.seal.soprafs20.database;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.LocationNotFoundException;
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


public class DatabaseConnectorLocationChats {
    public DatabaseConnectorLocationChats() {}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase LocationChats = mongoClient.getDatabase("LocationChats");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> chatsCollection = LocationChats.getCollection("Chats");

    //creates Entry in the database when a new location is created
    public static void createEntry(Integer locationId) {
        ArrayList<Message> emptyChat = new ArrayList<>();
        Document doc = new Document("locationId", locationId)
                .append("messages", emptyChat);
        chatsCollection.insertOne(doc);
    }

    public static ArrayList<Message> getChat(Integer locationId){
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
            chat.add(convertMessageJSONToEntity(messageJSON));
        }

        return chat;
    }

    public static void postMessage(Integer locationId, Message message){
        ArrayList<Message> chat = getChat(locationId);
        chat.add(message);
        chatsCollection.updateOne(eq("locationId", locationId),
                set("messages", chat));
    }

    public static Message convertMessageJSONToEntity(JSONObject messageJSON){
        Message message = new Message();

        message.setSenderId(messageJSON.getInt("senderId"));
        message.setContent(messageJSON.getString("content"));
        message.setTimestamp(messageJSON.getString("timestamp"));

        return message;
    }
}
