package ch.uzh.ifi.seal.soprafs20.database.key;

public class Credentials {
    public Credentials() {}

    private static String mongoCredentials = "mongodb+srv://lknufi:abc@knowyourcity-ijzn3.gcp.mongodb.net/test?retryWrites=true&w=majority";

    public static String getMongoCredentials() {
        return mongoCredentials;
    }
}
