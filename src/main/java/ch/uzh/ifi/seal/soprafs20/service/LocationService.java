package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorFavoriteLocations;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocation;
import ch.uzh.ifi.seal.soprafs20.database.DatabaseConnectorLocationChats;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.exceptions.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.FilterPostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

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

        allLocations.addAll(listFountains);
        allLocations.addAll(listFireplaces);
        allLocations.addAll(listRecyclingStations);

        return allLocations;
    }

    public List<Location> getFilteredLocations(FilterPostDTO filterPostDTO) {
        List<Location> filteredLocations = new ArrayList<>();
        if (filterPostDTO.showFountains()){
            List<Location> listFountains = DatabaseConnectorLocation.getFountains();
            filteredLocations.addAll(listFountains);
        }
        if (filterPostDTO.showFireplaces()){
            List<Location> listFireplaces = DatabaseConnectorLocation.getFireplaces();
            filteredLocations.addAll(listFireplaces);
        }
        if (filterPostDTO.showRecyclingStations()){
            List<Location> listRecyclingStations = DatabaseConnectorLocation.getRecyclingStations();
            filteredLocations.addAll(listRecyclingStations);
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
        Location location = new Location();
        return location;
    }

    public void updateLocation(Location location){
    }

    public ArrayList<Message> getChat(Integer locationId){
        return DatabaseConnectorLocationChats.getChat(locationId);
    }

    public void postMessage(Integer locationId, Message message){
        message.setTimestamp(getCurrentTimestamp());
        DatabaseConnectorLocationChats.postMessage(locationId, message);
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

    public String getCurrentTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM, HH:mm");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf.format(timestamp);
    }
}
