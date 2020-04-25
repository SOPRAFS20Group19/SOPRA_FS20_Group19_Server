package ch.uzh.ifi.seal.soprafs20.database;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.*;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseConnectorLocation {
    private static java.lang.Object Object;

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

    static MongoCollection<Document> userFountainsCollection = locationStorage.getCollection("UserFountains");

    static MongoCollection<Document> userFireplacesCollection = locationStorage.getCollection("UserFireplaces");

    static MongoCollection<Document> userRecyclingCollection = locationStorage.getCollection("UserRecycling");



    public static void getFountainById(int id){
        Document fountainOne = fountainsCollection.find().first();

        //Document fountain = (Document) fountainsCollection.find(eq("properties.objectid", id));
        //return fountainToLocation(fountainOne);
    }

    public static void addNewFountainToDatabase(Location location){
        JSONArray coordinatesAsJSON = new JSONArray();
        coordinatesAsJSON.put(location.getLatitude());
        coordinatesAsJSON.put(location.getLongitude());

        Document coordinates = new Document("type", "Point")
                .append("coordinates", coordinatesAsJSON);

        Document properties = new Document("locationtype", "fountain")
                .append("objectid", location.getId())
                .append("art_txt", location.getArt_txt())
                .append("brunnenart_txt", location.getBrunnenart_txt())
                .append("historisches_baujahr", location.getBaujahr())
                .append("baujahr", location.getBaujahr());


        Document doc = new Document("type", "Feature")
                .append("geometry", coordinates)
                .append("properties", properties);
        userFountainsCollection.insertOne(doc);
    }

    public static void addNewFireplaceToDatabase(Location location){
        JSONArray ausstattungAsJSON = new JSONArray();
        ausstattungAsJSON.put("Holz");
        ausstattungAsJSON.put("Zeitung");

        Document idAndCoordinates = new Document("locationtype", "fireplace")
                .append("Latitude", location.getLatitude())
                .append("Longitude", location.getLongitude())
                .append("Id", location.getId());

        Document doc = new Document("IconUrl", "/Content/images/fire-icon.png")
                .append("BarbecuePlace", idAndCoordinates)
                .append("Ausstattung", ausstattungAsJSON);
        userFireplacesCollection.insertOne(doc);
    }

    public static void addNewRecyclingStationToDatabase(Location location){
        JSONArray coordinatesAsJSON = new JSONArray();
        coordinatesAsJSON.put(location.getLatitude());
        coordinatesAsJSON.put(location.getLongitude());

        Document coordinates = new Document("type", "Point")
                .append("coordinates", coordinatesAsJSON);

        Document properties = new Document()
                .append("objectid", Integer.toString(location.getId()))
                .append("adresse", location.getAdresse())
                .append("plz", location.getPlz())
                .append("ort", location.getOrt())
                .append("metall", location.getMetall())
                .append("glas", location.getGlas())
                .append("oel", location.getOel());

        Document doc = new Document("type", "Feature")
                .append("geometry", coordinates)
                .append("properties", properties);
        userRecyclingCollection.insertOne(doc);
    }


    //Helper function which generates a unique fountain id
    public static int generateFountainId(){
        int random = (int) (Math.random() * 10000) + 10000000;
        FindIterable<Document> request = userFountainsCollection.find(eq("userId", random));
        FindIterable<Document> request2 = fountainsCollection.find(eq("userId", random));

        if (request.first() != null || request2.first() != null){random = DatabaseConnectorLocation.generateFountainId();};
        return  random;
    }

    //Helper function which generates a unique fireplace id
    public static int generateFireplaceId(){
        int random = (int) (Math.random() * 10000) + 20000000;
        FindIterable<Document> request = userFireplacesCollection.find(eq("userId", random));
        FindIterable<Document> request2 = fireplacesCollection.find(eq("userId", random));
        if (request.first() != null || request2.first() != null){random = DatabaseConnectorLocation.generateFireplaceId();};
        return  random;
    }

    //Helper function which generates a unique fireplace id
    public static int generateRecyclingId(){
        int random = (int) (Math.random() * 10000) + 30000000;
        FindIterable<Document> request = userRecyclingCollection.find(eq("userId", random));
        FindIterable<Document> request2 = recyclingCollection.find(eq("userId", random));
        if (request.first() != null || request2.first() != null){random = DatabaseConnectorLocation.generateRecyclingId();};
        return  random;
    }

    public static int createLocation(Location location){

        if (location.getLocationType() == LocationType.FOUNTAIN){
            location.setId(generateFountainId());
            addNewFountainToDatabase(location);
        }

        else if (location.getLocationType() == LocationType.FIREPLACE){
            location.setId(generateFireplaceId());
            addNewFireplaceToDatabase(location);

        }

        else if (location.getLocationType() == LocationType.RECYCLING_STATION){
            location.setId(generateRecyclingId());
            addNewRecyclingStationToDatabase(location);

        }

        return location.getId();

        /*
        else if (location.getLocationType() == LocationType.FIREPLACE){
            fireplacesCollection.insertOne(doc);
        }
        else {
            recyclingCollection.insertOne(doc);
        }

         */

    }

    public static List<Location> getUserFountains() throws JSONException {
        List<Document> fountainsList = userFountainsCollection.find().into(new ArrayList<>());
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

    public static List<Location> getUserFireplaces(){
        List<Document> fireplacesList = userFireplacesCollection.find().into(new ArrayList<>());
        List<Location> fireplacesListLocation = new ArrayList<>();
        for (Document fireplace: fireplacesList){
            String fireplaceAsString = fireplace.toJson();
            JSONObject fireplaceAsJSON = new JSONObject(fireplaceAsString);
            //convert Document to Location
            Location fireplaceLocation = fireplaceToLocation(fireplaceAsJSON);

            fireplacesListLocation.add(fireplaceLocation);


            // check if fireplace is in Zurich
            /*
            if (fireplaceLocation.getLongitude() > 8.4680486289 && fireplaceLocation.getLongitude() < 8.6191027275
                    && fireplaceLocation.getLatitude() > 47.3232261256 && fireplaceLocation.getLatitude() < 47.4308197123){
                //add Location to List of Locations
                fireplacesListLocation.add(fireplaceLocation);
            }

             */
        }
        return fireplacesListLocation;
    }

    public static List<Location> getUserRecycling(){
        List<Document> recyclingList = userRecyclingCollection.find().into(new ArrayList<>());
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

            // check if fireplace is in Zurich
            if (fireplaceLocation.getLongitude() > 8.4680486289 && fireplaceLocation.getLongitude() < 8.6191027275
            && fireplaceLocation.getLatitude() > 47.3232261256 && fireplaceLocation.getLatitude() < 47.4308197123){
                //add Location to List of Locations
                fireplacesListLocation.add(fireplaceLocation);
            }
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

        // set ID
        newLocation.setId(properties.getInt("objectid"));

        // set coordinates
        JSONArray coordinatesAsJSON = (JSONArray) geometry.get("coordinates");
        double lat = coordinatesAsJSON.getDouble(1);
        newLocation.setLatitude(lat);
        double lon = coordinatesAsJSON.getDouble(0);
        newLocation.setLongitude(lon);
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);

        // set locationType
        newLocation.setLocationType(LocationType.FOUNTAIN);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        if (properties.get("art_txt") != null){
            String fountainType = "Fountain type: " + properties.getString("art_txt").replace("Brunnen_", "");
            additionalInformation.add(fountainType);
        }
        //additionalInformation.append("Fountain type: ").append(properties.getString("art_txt")).append("\\n");
        if (properties.get("brunnenart_txt") != null){
            String access = "Access: " + properties.getString("brunnenart_txt");
            additionalInformation.add(access);
        }
        //additionalInformation.append("Access: ").append(properties.getString("brunnenart_txt")).append("\\n");
        if (properties.get("baujahr") != null) {
            String yOC = "Year of construction: " + properties.get("baujahr");
            additionalInformation.add(yOC);
        }
        //additionalInformation.append("Year of construction: ").append(properties.get("baujahr")).append("\\n");
        /*
        if (properties.getString("wasserart_txt") != null){
            String waterSource = "Water source type: " + properties.getString("wasserart_txt");
            additionalInformation.add(waterSource);
            //additionalInformation.append("Water source type: ").append(properties.getString("wasserart_txt")).append("\\n");
        }

         */
        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
    }

    public static Location fireplaceToLocation(JSONObject fireplaceAsJSON){
        Location newLocation = new Location();

        JSONObject barbecuePlace = fireplaceAsJSON.getJSONObject("BarbecuePlace");
        JSONArray features = fireplaceAsJSON.getJSONArray("Ausstattung");

        // set ID
        newLocation.setId(barbecuePlace.getInt("Id"));

        // set coordinates
        double lat = barbecuePlace.getDouble("Latitude");
        newLocation.setLatitude(lat);
        double lon = barbecuePlace.getDouble("Longitude");
        newLocation.setLongitude(lon);
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);

        // set locationType
        newLocation.setLocationType(LocationType.FIREPLACE);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        additionalInformation.add("Features and environment:");
        for (int i = 0; i < features.length(); i++){
            String feature = "- " + features.getString(i);
            additionalInformation.add(feature);
        }
        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
    }

    public static Location recyclingToLocation(JSONObject recyclingAsJSON){
        Location newLocation = new Location();

        JSONObject properties = recyclingAsJSON.getJSONObject("properties");
        JSONObject geometry = recyclingAsJSON.getJSONObject("geometry");
        JSONArray coordinatesAsJSON = (JSONArray) geometry.get("coordinates");

        // set ID
        newLocation.setId(Integer.parseInt(properties.getString("objectid")));

        // set coordinates
        double lat = coordinatesAsJSON.getDouble(1);
        newLocation.setLatitude(lat);
        double lon = coordinatesAsJSON.getDouble(0);
        newLocation.setLongitude(lon);
        double[] coordinates = {lon, lat};
        newLocation.setCoordinates(coordinates);

        // set locationType
        newLocation.setLocationType(LocationType.RECYCLING_STATION);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        StringBuilder address = new StringBuilder();
        if (properties.getString("adresse") != null && properties.getString("plz") != null && properties.getString("ort") != null){
            address.append("Address: ").append(properties.getString("adresse")).append(", ")
                    .append(properties.getString("plz")).append(" ").append(properties.getString("ort"));
            additionalInformation.add(address.toString());
        }

        additionalInformation.add("Disposable at this location:");
        if (properties.get("metall") != null){
            additionalInformation.add("- Metal");
        }
        if (properties.get("glas") != null){
            additionalInformation.add("- Glass");
        }
        if (properties.get("oel") != null){
            additionalInformation.add("- Oil");
        }

        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
    }

}
