package ch.uzh.ifi.seal.soprafs20.database;



import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Address;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.LocationNotFoundException;
import ch.uzh.ifi.seal.soprafs20.exceptions.UserNotFoundException;
import com.mongodb.client.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.bson.Document;
import org.json.JSONException;

import org.json.*;
import org.json.simple.parser.ParseException;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.io.FileReader;
import java.util.Iterator;

public class DatabaseConnectorAddresses {
    public DatabaseConnectorAddresses(){}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority");
    //Establish connection to the Location Database (development purposes only)
    static MongoDatabase AddressZurich = mongoClient.getDatabase("AddressZurich");
    //Establish connection to the Fountains Collection (development purposes only)
    static MongoCollection<Document> addressCollection = AddressZurich.getCollection("Address");
    static MongoCollection<Document> closestAddressCollection = AddressZurich.getCollection("ClosestAddress");

    // only used for initializing the address collection. Do not run again
    public static void initialSetup() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("/Users/luisknufinke/Documents/UZH/04 - FS20/Softwarepraktikum/Know your City/SOPRA_FS20_Group19_Server/src/main/java/ch/uzh/ifi/seal/soprafs20/geoz.adrstzh_strassennamen_stzh_p.json"));
        JSONObject file = (JSONObject) obj;
        JSONArray streetnames = (JSONArray) file.get("features");

        for (JSONObject streetnameObject : (Iterable<JSONObject>) streetnames) {
            JSONObject properties = (JSONObject) streetnameObject.get("properties");
            JSONObject geometry = (JSONObject) streetnameObject.get("geometry");
            JSONArray coordinatesAsJSON = (JSONArray) geometry.get("coordinates");
            double lat = (double) coordinatesAsJSON.get(1);
            double lon = (double) coordinatesAsJSON.get(0);
            String streetname = (String) properties.get("lokalisationsname");

            Document doc = new Document("latitude", lat)
                    .append("longitude", lon)
                    .append("streetname", streetname);
            addressCollection.insertOne(doc);
        }
    }

    public static ArrayList<Address> getAddresses(){
        List<Document> addressListDoc = addressCollection.find().into(new ArrayList<>());
        ArrayList<Address> addressList = new ArrayList<>();
        for (Document addressDoc : addressListDoc) {
            String addressAsString = addressDoc.toJson();
            org.json.JSONObject addressAsJson = new org.json.JSONObject(addressAsString);
            //convert Document to Location
            Address address = convertAddressToEntity(addressAsJson);
            //add Location to List of Locations
            addressList.add(address);
        }
        return addressList;
    }

    public static Address convertAddressToEntity(org.json.JSONObject addressAsJSON){
        Address address = new Address();
        address.setLatitude(addressAsJSON.getDouble("latitude"));
        address.setLongitude(addressAsJSON.getDouble("longitude"));
        address.setStreet(addressAsJSON.getString("streetname"));

        return address;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2){
        var R = 6371e3; // metres
        var φ1 = Math.toRadians(x1);
        var φ2 = Math.toRadians(x2);
        var Δφ = Math.toRadians(x2-x1);
        var Δλ = Math.toRadians(y2-y1);

        var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ/2) * Math.sin(Δλ/2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
        //double x = Math.pow(1000000 * x2 - 1000000 * x1, 2);
        //double y = Math.pow(1000000 * y2 - 1000000 * y1, 2);
        //return Math.sqrt(x+y);
    }

    public static Location getLocationById(Integer locationId){
        List<Location> allLocations = new ArrayList<>();
        List<Location> listFountains = DatabaseConnectorLocation.getFountains();
        List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
        List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();
        List<Location> listUserFountains = DatabaseConnectorLocation.getUserFountains();
        List<Location> listUserFireplaces = DatabaseConnectorLocation.getUserFireplaces();
        List<Location> listUserRecycling = DatabaseConnectorLocation.getUserRecycling();

        allLocations.addAll(listFountains);
        allLocations.addAll(listFireplaces);
        allLocations.addAll(listRecyclingStations);
        allLocations.addAll(listUserFountains);
        allLocations.addAll(listUserFireplaces);
        allLocations.addAll(listUserRecycling);

        Location locationToReturn = null;

        for (Location location: allLocations){
            if (locationId == (location.getId())){
                locationToReturn = location;
            }
        }

        if (locationToReturn == null){
            throw new LocationNotFoundException("This location could not be found.");
        }

        return locationToReturn;
    }

    public static String calculateClosestAddressStreet(Integer locationId){
        ArrayList<Address> addresses = getAddresses();
        Location location = getLocationById(locationId);

        Address closestAddress = addresses.get(0);
        double closestDistance = calculateDistance(location.getLatitude(), location.getLongitude(), closestAddress.getLatitude(), closestAddress.getLongitude());

        for (Address address : addresses){
            double distance = calculateDistance(location.getLatitude(), location.getLongitude(), address.getLatitude(), address.getLongitude());

            if (distance < closestDistance){
                closestAddress = address;
                closestDistance = distance;
            }
        }

        return closestAddress.getStreet();
    }

    // initial setup for closest address collection, do not run again
    public static void setupClosestAddressCollection(){
        List<Location> allLocations = new ArrayList<>();
        List<Location> listFountains = DatabaseConnectorLocation.getFountains();
        List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
        List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();
        List<Location> listUserFountains = DatabaseConnectorLocation.getUserFountains();
        List<Location> listUserFireplaces = DatabaseConnectorLocation.getUserFireplaces();
        List<Location> listUserRecycling = DatabaseConnectorLocation.getUserRecycling();

        allLocations.addAll(listFountains);
        allLocations.addAll(listFireplaces);
        allLocations.addAll(listRecyclingStations);
        allLocations.addAll(listUserFountains);
        allLocations.addAll(listUserFireplaces);
        allLocations.addAll(listUserRecycling);

        for (Location location : allLocations){
            FindIterable<Document> request =  closestAddressCollection.find(eq("locationId", location.getId()));
            Document existingDoc = request.first();
            if (existingDoc == null){
                Document doc = new Document("locationId", location.getId())
                        .append("closestStreet", calculateClosestAddressStreet(location.getId()));
                closestAddressCollection.insertOne(doc);
            }
        }
    }

    public static String getClosestAddress(Integer locationId){
        FindIterable<Document> request =  closestAddressCollection.find(eq("locationId", locationId));
        Document doc = request.first();
        if (doc == null){
            return " ";
        }
        return doc.getString("closestStreet");
    }

    // adds an entry in the collection for added locations
    public static void createEntry(Integer locationId){
        Document doc = new Document("locationId", locationId)
                .append("closestStreet", calculateClosestAddressStreet(locationId));
        closestAddressCollection.insertOne(doc);
    }

    public static void testFieldAdding(Integer locationId){
        Document updatedDoc = new Document().append("$set", new Document().append("secondAddress", getClosestAddress(locationId)));
        closestAddressCollection.updateOne(eq("locationId", locationId), updatedDoc);
    }
}
