package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorFavoriteLocations;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocationChats;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorRating;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.exceptions.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.FilterPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

/**
 * Location Service
 * This class is the "worker" and responsible for all functionality related to the location
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    public LocationService(){}

    public List<Location> getLocations(){
        List<Location> allLocations = new ArrayList<>();
        List<Location> listFountains = DatabaseConnectorLocation.getFountains();
        List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
        List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();
        List<Location> listUserFountains = DatabaseConnectorLocation.getUserFountains();
        List<Location> listUserFireplaces = DatabaseConnectorLocation.getUserFireplaces();
        List<Location> listUserRecycling = DatabaseConnectorLocation.getUserRecycling();
        List<Location> listToilets = DatabaseConnectorLocation.getToilets();
        List<Location> listUserToilets = DatabaseConnectorLocation.getUserToilets();
        List<Location> listUserBench = DatabaseConnectorLocation.getUserBench();
        List<Location> listUserTableTennis = DatabaseConnectorLocation.getUserTableTennis();


        allLocations.addAll(listFountains);
        allLocations.addAll(listFireplaces);
        allLocations.addAll(listRecyclingStations);
        allLocations.addAll(listUserFountains);
        allLocations.addAll(listUserFireplaces);
        allLocations.addAll(listUserRecycling);
        allLocations.addAll(listToilets);
        allLocations.addAll(listUserToilets);
        allLocations.addAll(listUserBench);
        allLocations.addAll(listUserTableTennis);


        return allLocations;
    }

    public List<Location> getFilteredLocations(FilterPostDTO filterPostDTO) {
        List<Location> filteredLocations = new ArrayList<>();
        if (filterPostDTO.showFountains()){
            List<Location> listFountains = DatabaseConnectorLocation.getFountains();
            List<Location> listUserFountains = DatabaseConnectorLocation.getUserFountains();
            filteredLocations.addAll(listFountains);
            filteredLocations.addAll(listUserFountains);
        }
        if (filterPostDTO.showFireplaces()){
            List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
            List<Location> listUserFireplaces = DatabaseConnectorLocation.getUserFireplaces();
            filteredLocations.addAll(listFireplaces);
            filteredLocations.addAll(listUserFireplaces);

        }
        if (filterPostDTO.showRecyclingStations()){
            List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();
            List<Location> listUserRecyclingStations = DatabaseConnectorLocation.getUserRecycling();
            filteredLocations.addAll(listRecyclingStations);
            filteredLocations.addAll(listUserRecyclingStations);

        }
        if (filterPostDTO.showToilets()){
            List<Location> listToilets = DatabaseConnectorLocation.getToilets();
            List<Location> listUserToilets = DatabaseConnectorLocation.getUserToilets();
            filteredLocations.addAll(listToilets);
            filteredLocations.addAll(listUserToilets);
        }
        if (filterPostDTO.showTableTennis()){
            List<Location> listUserTableTennis = DatabaseConnectorLocation.getUserTableTennis();
            filteredLocations.addAll(listUserTableTennis);
        }
        if (filterPostDTO.showBenches()){
            List<Location> listUserBenches = DatabaseConnectorLocation.getUserBench();
            filteredLocations.addAll(listUserBenches);
        }

        return filteredLocations;
    }

    public Location getLocation(int id){
        List<Location> allLocations = this.getLocations();

        Location locationToReturn = null;

        for (Location location: allLocations){
            if (id == (location.getId())){
                locationToReturn = location;
            }
        }

        if (locationToReturn == null){
            throw new LocationNotFoundException("This location could not be found.");
        }

        return locationToReturn;
    }

    public Location createLocation(Location newLocation){
        int newLocationId = DatabaseConnectorLocation.createLocation(newLocation);
        //ADD TO CHAT -> IMPORTANT! später die Kommentarfunktion entfernen, LK: auskommentiert am 27.04
        DatabaseConnectorLocationChats.addChatForNewLocation(newLocationId);
        Location locationToReturn = this.getLocation(newLocationId);
        return locationToReturn;
    }

    public ArrayList<Message> getChat(Integer locationId){
        return DatabaseConnectorLocationChats.getChat(locationId);
    }

    public void postMessage(Integer locationId, Message message){
        message.setTimestamp(getCurrentTimestamp());
        DatabaseConnectorLocationChats.postMessage(locationId, message);
    }

    public void deleteMessage(Integer locationId, int messageId){
        DatabaseConnectorLocationChats.deleteMessage(locationId, messageId);
    }

    // gets a users favorite locations
    public List<Location> getFavoriteLocations(int userId){
        ArrayList<Integer> favoriteLocationIds = DatabaseConnectorFavoriteLocations.getFavoriteLocations(userId);
        List<Location> locationsToReturn = new ArrayList<>();
        for (Integer locationId : favoriteLocationIds){
            locationsToReturn.add(this.getLocation(locationId));
        }
        return locationsToReturn;
    }

    public void updateFavoriteLocations(int userId, Integer locationId){
        ArrayList<Integer> favorites = DatabaseConnectorFavoriteLocations.getFavoriteLocations(userId);
        ArrayList<Integer> newFavorites = new ArrayList<>(favorites);
        if (!favorites.contains(locationId)){
            newFavorites.add(locationId);
        }
        DatabaseConnectorFavoriteLocations.updateFavoriteLocations(userId, newFavorites);
    }

    public void deleteFavoriteLocation(int userId, Integer locationId){
        ArrayList<Integer> favorites = DatabaseConnectorFavoriteLocations.getFavoriteLocations(userId);
        ArrayList<Integer> newFavorites = new ArrayList<>(favorites);
        if (favorites.contains(locationId)){
            newFavorites.remove(locationId);
        }
        DatabaseConnectorFavoriteLocations.updateFavoriteLocations(userId, newFavorites);
    }

    public Location checkFavoriteLocations(int userId, Integer locationId){
        ArrayList<Integer> favoriteLocationIds = DatabaseConnectorFavoriteLocations.getFavoriteLocations(userId);
        Location locationToReturn = null;
        for (Integer locationIdToCheck : favoriteLocationIds){
            if (locationId.equals(locationIdToCheck)){
                locationToReturn = this.getLocation(locationId);
            }
        }
        return locationToReturn;
    }

    public int checkRating(int userId, Integer locationId){
        //implement the function
        return DatabaseConnectorRating.getRating(userId,locationId);

    }

    public double checkAverageRating(Integer locationId){
        return DatabaseConnectorRating.getAverageRating(locationId);
    }

    public void updateRating(int userId, Integer locationId, int rating){
        DatabaseConnectorRating.updateRating(userId, locationId, rating);
    }

    public String getCurrentTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM, HH:mm");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }
}
