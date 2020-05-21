package ch.uzh.ifi.seal.soprafs20.database;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.database.key.Credentials;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DatabaseConnectorLocation {
    private static java.lang.Object Object;

    public DatabaseConnectorLocation(){}

    //connection to mongodb on the cloud with credentials of luca locher
    static MongoClient mongoClient = MongoClients.create(
            Credentials.getMongoCredentials());
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

    static MongoCollection<Document> toiletsCollection = locationStorage.getCollection("WC");

    static MongoCollection<Document> userToiletsCollection = locationStorage.getCollection("UserWC");

    static MongoCollection<Document> userTableTennisCollection = locationStorage.getCollection("UserTableTennis");

    static MongoCollection<Document> userBenchCollection = locationStorage.getCollection("UserBench");

    public static void addNewFountainToDatabase(Location location){
        JSONArray coordinatesAsJSON = new JSONArray();
        coordinatesAsJSON.put(location.getLongitude());
        coordinatesAsJSON.put(location.getLatitude());


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
                .append("properties", properties)
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId()));
        userFountainsCollection.insertOne(doc);

        // creates a new entry in the closestAddress DB
        DatabaseConnectorAddresses.createEntry(location.getId());

        Document updatedDoc = new Document().append("$set", new Document()
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId())));
        userFountainsCollection.updateOne(eq("properties.objectid", location.getId()), updatedDoc);

    }

    public static void addNewFireplaceToDatabase(Location location){
        JSONArray ausstattungAsJSON = new JSONArray();
        /*
        String ausstattung = location.getAusstattung();
        ArrayList<String> ausstattungList = new ArrayList<>(Arrays.asList(ausstattung.split(",")));
        for (String property : ausstattungList){
            ausstattungAsJSON.put(property);
        }

         */
        if (location.getHolz().equals("X")){
            ausstattungAsJSON.put("Holz");
        }
        if (location.getRost().equals("X")){
            ausstattungAsJSON.put("Rost");
        }
        if (location.getTisch().equals("X")){
            ausstattungAsJSON.put("Tisch");
        }
        if (location.getBaden().equals("X")){
            ausstattungAsJSON.put("Baden");
        }
        if (location.getTrinkwasser().equals("X")){
            ausstattungAsJSON.put("Trinkwasser");
        }
        if (location.getAbfall().equals("X")){
            ausstattungAsJSON.put("Abfall");
        }
        if (location.getParkplatz().equals("X")){
            ausstattungAsJSON.put("Parkplatz");
        }
        if (location.getHunde().equals("X")){
            ausstattungAsJSON.put("Hunde");
        }
        if (location.getKinderwagen().equals("X")){
            ausstattungAsJSON.put("Kinderwagen");
        }

        Document idAndCoordinates = new Document("locationtype", "fireplace")
                .append("Latitude", location.getLatitude())
                .append("Longitude", location.getLongitude())
                .append("Id", location.getId())
                ;

        Document doc = new Document("IconUrl", "/Content/images/fire-icon.png")
                .append("BarbecuePlace", idAndCoordinates)
                .append("Ausstattung", ausstattungAsJSON)
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId()));
        userFireplacesCollection.insertOne(doc);

        // creates a new entry in the closestAddress DB
        DatabaseConnectorAddresses.createEntry(location.getId());

        Document updatedDoc = new Document().append("$set", new Document()
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId())));
        userFireplacesCollection.updateOne(eq("BarbecuePlace.Id", location.getId()), updatedDoc);

    }

    public static void addNewRecyclingStationToDatabase(Location location){
        JSONArray coordinatesAsJSON = new JSONArray();
        coordinatesAsJSON.put(location.getLongitude());
        coordinatesAsJSON.put(location.getLatitude());

        Document coordinates = new Document("type", "Point")
                .append("coordinates", coordinatesAsJSON);

        Document properties = new Document("locationtype", "recycling")
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

    public static void addNewToiletToDatabase(Location location){
        Document doc = new Document()
                .append("latitude", location.getLatitude())
                .append("longitude", location.getLongitude())
                .append("objectid", location.getId())
                .append("adresse", location.getAdresse())
                .append("ort", location.getOrt())
                .append("plz", location.getPlz())
                .append("openinghours", location.getOpeningHours())
                .append("cost", location.getCost())
                .append("category", location.getCategory());
        userToiletsCollection.insertOne(doc);
    }

    public static void addNewTableTennisToDatabase(Location location){
        Document doc = new Document()
                .append("objectId", location.getId())
                .append("latitude", location.getLatitude())
                .append("longitude", location.getLongitude())
                .append("slabQuality", location.getSlabQuality())
                .append("net", location.getNet())
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId()));
        userTableTennisCollection.insertOne(doc);

        // creates a new entry in the closestAddress DB
        DatabaseConnectorAddresses.createEntry(location.getId());

        Document updatedDoc = new Document().append("$set", new Document()
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId())));
        userTableTennisCollection.updateOne(eq("objectId", location.getId()), updatedDoc);
    }

    public static void addNewBenchToDatabase(Location location){
        Document doc = new Document()
                .append("objectId", location.getId())
                .append("latitude", location.getLatitude())
                .append("longitude", location.getLongitude())
                .append("view", location.getView())
                .append("peace", location.getPeace())
                .append("romantics", location.getRomantics())
                .append("comfort", location.getComfort())
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId()));
        userBenchCollection.insertOne(doc);

        // creates a new entry in the closestAddress DB
        DatabaseConnectorAddresses.createEntry(location.getId());

        Document updatedDoc = new Document().append("$set", new Document()
                .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(location.getId())));
        userBenchCollection.updateOne(eq("objectId", location.getId()), updatedDoc);
    }

    //Helper function which generates a unique table tennis id
    public static int generateBenchId(){
        int random = (int) (Math.random() * 10000) + 60000000;
        FindIterable<Document> request = userBenchCollection.find(eq("objectid", random));

        if (request.first() != null){DatabaseConnectorLocation.generateBenchId();};
        return  random;
    }

    //Helper function which generates a unique table tennis id
    public static int generateTableTennisId(){
        int random = (int) (Math.random() * 10000) + 50000000;
        FindIterable<Document> request = userTableTennisCollection.find(eq("objectid", random));

        if (request.first() != null){DatabaseConnectorLocation.generateTableTennisId();};
        return  random;
    }

    //Helper function which generates a unique toilet id
    public static int generateToiletId(){
        int random = (int) (Math.random() * 10000) + 40000000;
        FindIterable<Document> request = toiletsCollection.find(eq("objectid", random));
        FindIterable<Document> request2 = userToiletsCollection.find(eq("objectid", random));

        if (request.first() != null || request2.first() != null){DatabaseConnectorLocation.generateToiletId();};
        return  random;
    }

    //Helper function which generates a unique fountain id
    public static int generateFountainId(){
        int random = (int) (Math.random() * 10000) + 10000000;
        FindIterable<Document> request = userFountainsCollection.find(eq("properties.objectid", random));
        FindIterable<Document> request2 = fountainsCollection.find(eq("properties.objectid", random));

        if (request.first() != null || request2.first() != null){DatabaseConnectorLocation.generateFountainId();};
        return  random;
    }

    //Helper function which generates a unique fireplace id
    public static int generateFireplaceId(){
        int random = (int) (Math.random() * 10000) + 20000000;
        FindIterable<Document> request = userFireplacesCollection.find(eq("BarbecuePlace.Id", random));
        FindIterable<Document> request2 = fireplacesCollection.find(eq("BarbecuePlace.Id", random));
        if (request.first() != null || request2.first() != null){DatabaseConnectorLocation.generateFireplaceId();};
        return  random;
    }

    //Helper function which generates a unique fireplace id
    public static int generateRecyclingId(){
        int random = (int) (Math.random() * 10000) + 30000000;
        FindIterable<Document> request = userRecyclingCollection.find(eq("properties.objectid", Integer.toString(random)));
        FindIterable<Document> request2 = recyclingCollection.find(eq("properties.objectid", Integer.toString(random)));
        if (request.first() != null || request2.first() != null){DatabaseConnectorLocation.generateRecyclingId();};
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

        else if (location.getLocationType() == LocationType.TOILET){
            location.setId(generateToiletId());
            addNewToiletToDatabase(location);
        }

        else if (location.getLocationType() == LocationType.TABLE_TENNIS){
            location.setId(generateTableTennisId());
            addNewTableTennisToDatabase(location);
        }

        else if (location.getLocationType() == LocationType.BENCH){
            location.setId(generateBenchId());
            addNewBenchToDatabase(location);
        }
        return location.getId();
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

    public static List<Location> getUserToilets(){
        List<Document> toiletsList = userToiletsCollection.find().into(new ArrayList<>());
        List<Location> toiletsListLocation = new ArrayList<>();
        for (Document toilet: toiletsList){
            String toiletAsString = toilet.toJson();
            JSONObject toiletAsJSON = new JSONObject(toiletAsString);
            //convert Document to Location
            Location toiletLocation = toiletToLocation(toiletAsJSON);
            //add Location to List of Locations
            toiletsListLocation.add(toiletLocation);
        }
        return toiletsListLocation;
    }

    public static List<Location> getUserTableTennis(){
        List<Document> tableTennisList = userTableTennisCollection.find().into(new ArrayList<>());
        List<Location> tableTennisListLocation = new ArrayList<>();
        for (Document tableTennis: tableTennisList){
            String tableTennisAsString = tableTennis.toJson();
            JSONObject tableTennisAsJSON = new JSONObject(tableTennisAsString);
            //convert Document to Location
            Location tableTennisLocation = tableTennisToLocation(tableTennisAsJSON);
            //add Location to List of Locations
            tableTennisListLocation.add(tableTennisLocation);
        }
        return tableTennisListLocation;
    }

    public static List<Location> getUserBench(){
        List<Document> benchList = userBenchCollection.find().into(new ArrayList<>());
        List<Location> benchListLocation = new ArrayList<>();
        for (Document bench: benchList){
            String benchAsString = bench.toJson();
            JSONObject benchAsJSON = new JSONObject(benchAsString);
            //convert Document to Location
            Location benchLocation = benchToLocation(benchAsJSON);
            //add Location to List of Locations
            benchListLocation.add(benchLocation);
        }
        return benchListLocation;
    }

    public static List<Location> getToilets(){
        List<Document> toiletsList = toiletsCollection.find().into(new ArrayList<>());
        List<Location> toiletsListLocation = new ArrayList<>();
        for (Document toilet: toiletsList){
            String toiletAsString = toilet.toJson();
            JSONObject toiletAsJSON = new JSONObject(toiletAsString);
            //convert Document to Location
            Location toiletLocation = toiletToLocation(toiletAsJSON);
            //add Location to List of Locations
            toiletsListLocation.add(toiletLocation);
        }
        return toiletsListLocation;
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

    public static Location benchToLocation(JSONObject benchAsJSON) throws JSONException {
        Location newLocation = new Location();

        // set coordinates
        double[] coordinates = {benchAsJSON.getDouble("longitude"), benchAsJSON.getDouble("latitude")};
        newLocation.setCoordinates(coordinates);

        newLocation.setId(benchAsJSON.getInt("objectId"));
        newLocation.setLatitude(benchAsJSON.getDouble("latitude"));
        newLocation.setLongitude(benchAsJSON.getDouble("longitude"));
        newLocation.setAddress(benchAsJSON.getString("closestStreet"));



        // set locationType
        newLocation.setLocationType(LocationType.BENCH);

        // retrieve and set additional information
        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();

        if (benchAsJSON.get("view").getClass().toString().equals("class java.lang.Integer")){
            additionalInformation.add("View: " + benchAsJSON.get("view") + "/5");
        }
        if (benchAsJSON.get("peace").getClass().toString().equals("class java.lang.Integer")){
            additionalInformation.add("Peace: " + benchAsJSON.get("peace") + "/5");
        }
        if (benchAsJSON.get("romantics").getClass().toString().equals("class java.lang.Integer")){
            additionalInformation.add("Romantics: " + benchAsJSON.get("romantics") + "/5");
        }
        if (benchAsJSON.get("comfort").getClass().toString().equals("class java.lang.Integer")){
            additionalInformation.add("Comfort: " + benchAsJSON.get("comfort") + "/5");
        }

        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));


        return newLocation;
    }

    public static Location tableTennisToLocation(JSONObject tableTennisAsJson) throws JSONException {
        Location newLocation = new Location();

        // set coordinates
        double[] coordinates = {tableTennisAsJson.getDouble("longitude"), tableTennisAsJson.getDouble("latitude")};
        newLocation.setCoordinates(coordinates);

        newLocation.setId(tableTennisAsJson.getInt("objectId"));
        newLocation.setLatitude(tableTennisAsJson.getDouble("latitude"));
        newLocation.setLongitude(tableTennisAsJson.getDouble("longitude"));
        newLocation.setAddress(tableTennisAsJson.getString("closestStreet"));


        // set locationType
        newLocation.setLocationType(LocationType.TABLE_TENNIS);

        // retrieve and set additional information
        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();

        if (tableTennisAsJson.get("slabQuality").getClass().toString().equals("class java.lang.Integer")){
            additionalInformation.add("Quality of table-top: " + tableTennisAsJson.get("slabQuality") + "/5");
        }

        if (tableTennisAsJson.get("net").getClass().toString().equals("class java.lang.String")){
            additionalInformation.add("Permanent net: " + tableTennisAsJson.getString("net"));
        }

        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
    }

    public static Location toiletToLocation(JSONObject toiletAsJson) throws JSONException {
        Location newLocation = new Location();

        newLocation.setLatitude(toiletAsJson.getDouble("latitude"));
        newLocation.setLongitude(toiletAsJson.getDouble("longitude"));
        newLocation.setId(toiletAsJson.getInt("objectid"));
        newLocation.setAddress(toiletAsJson.getString("adresse"));

        // set coordinates
        double[] coordinates = {toiletAsJson.getDouble("longitude"), toiletAsJson.getDouble("latitude")};
        newLocation.setCoordinates(coordinates);

        // set locationType
        newLocation.setLocationType(LocationType.TOILET);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        StringBuilder address = new StringBuilder();
        if (toiletAsJson.getString("adresse") != null && toiletAsJson.getString("plz") != null && toiletAsJson.getString("ort") != null){
            address.append("Full Address: ").append(toiletAsJson.getString("adresse")).append(", ")
                    .append(toiletAsJson.getString("plz")).append(" ").append(toiletAsJson.getString("ort"));
            additionalInformation.add(address.toString());
        }
        if (toiletAsJson.get("openinghours").getClass().toString().equals("class java.lang.String")){
            String openingHours = "Opening hours: " + toiletAsJson.getString("openinghours");
            additionalInformation.add(openingHours);
        }
        if (toiletAsJson.get("cost").getClass().toString().equals("class java.lang.String")){
            String cost = "Cost: " + toiletAsJson.getString("cost");
            additionalInformation.add(cost);
        }
        if (toiletAsJson.get("category").getClass().toString().equals("class java.lang.String")){
            if (toiletAsJson.getString("category").equals("WC (nicht rollstuhlgängig)")) {
                String cost = "Suitable for disabled people: No";
                additionalInformation.add(cost);

            }
            if (toiletAsJson.getString("category").equals("WC (rollstuhlgängig)")) {
                String cost = "Suitable for disabled people: Yes";
                additionalInformation.add(cost);

            }
        }

        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
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

        // set address
        newLocation.setAddress(fountainAsJson.getString("closestStreet"));

        // set locationType
        newLocation.setLocationType(LocationType.FOUNTAIN);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        if (properties.get("art_txt").getClass().toString().equals("class java.lang.String")){
            String fountainType = "Fountain type: " + properties.getString("art_txt").replace("Brunnen_", "");
            additionalInformation.add(fountainType);
        }
        //additionalInformation.append("Fountain type: ").append(properties.getString("art_txt")).append("\\n");
        if (properties.get("brunnenart_txt").getClass().toString().equals("class java.lang.String")){
            String access = "Access: " + properties.getString("brunnenart_txt");
            additionalInformation.add(access);
        }
        //additionalInformation.append("Access: ").append(properties.getString("brunnenart_txt")).append("\\n");
        if (properties.get("baujahr").getClass().toString().equals("class java.lang.Integer")) {
            if (properties.getInt("baujahr") != 0) {
                String yOC = "Year of construction: " + properties.get("baujahr");
                additionalInformation.add(yOC);
            }
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

        // set address if fireplace is in Zurich
        if (newLocation.getLongitude() > 8.4680486289 && newLocation.getLongitude() < 8.6191027275
                && newLocation.getLatitude() > 47.3232261256 && newLocation.getLatitude() < 47.4308197123){
            newLocation.setAddress(DatabaseConnectorAddresses.getClosestAddress(newLocation.getId()));
        }


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

        // set address
        newLocation.setAddress(properties.getString("adresse"));

        // set locationType
        newLocation.setLocationType(LocationType.RECYCLING_STATION);

        // retrieve and set additional information
        ArrayList<String> additionalInformation = new ArrayList<>();
        StringBuilder address = new StringBuilder();
        if (properties.getString("adresse") != null && properties.getString("plz") != null && properties.getString("ort") != null){
            address.append("Full Address: ").append(properties.getString("adresse")).append(", ")
                    .append(properties.getString("plz")).append(" ").append(properties.getString("ort"));
            additionalInformation.add(address.toString());
        }

        additionalInformation.add("Disposable at this location:");
        if (properties.get("metall").getClass().toString().equals("class java.lang.String")){
            additionalInformation.add("- Metal");
        }
        if (properties.get("glas").getClass().toString().equals("class java.lang.String")){
            additionalInformation.add("- Glass");
        }
        if (properties.get("oel").getClass().toString().equals("class java.lang.String")){
            additionalInformation.add("- Oil");
        }

        newLocation.setAdditionalInformation(additionalInformation.toArray(new String[0]));

        return newLocation;
    }

    /* one time move of address from address DB to location DB for performance reasons, do not use again. Keep in code if ever needed again
    public static void moveAddressToLocation(){
        List<Location> listFountains = getFountains();
        List<Location> listFireplaces = getFireplaces();
        List<Location> listUserFountains = getUserFountains();
        List<Location> listUserFireplaces = getUserFireplaces();

        for (Location fountain : listFountains){
            Document updatedDoc = new Document().append("$set", new Document()
                    .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(fountain.getId())));
            fountainsCollection.updateOne(eq("properties.objectid", fountain.getId()), updatedDoc);
        }

        for (Location fireplace : listFireplaces){
            Document updatedDoc = new Document().append("$set", new Document()
                    .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(fireplace.getId())));
            fireplacesCollection.updateOne(eq("BarbecuePlace.Id", fireplace.getId()), updatedDoc);
        }

        for (Location userFountain : listUserFountains){
            Document updatedDoc = new Document().append("$set", new Document()
                    .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(userFountain.getId())));
            userFountainsCollection.updateOne(eq("properties.objectid", userFountain.getId()), updatedDoc);
        }

        for (Location userFireplace : listUserFireplaces){
            Document updatedDoc = new Document().append("$set", new Document()
                    .append("closestStreet", DatabaseConnectorAddresses.getClosestAddress(userFireplace.getId())));
            userFireplacesCollection.updateOne(eq("BarbecuePlace.Id", userFireplace.getId()), updatedDoc);
        }
    }*/

}
