package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Location Controller
 * This class is responsible for handling all REST request that are related to the location.
 * The controller will receive the request and delegate the execution to the LocationService and finally return the result.
 */
@RestController
public class LocationController {

    private final LocationService locationService;

    LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LocationGetDTO> getAllLocations() {
        List<Location> locations =  locationService.getLocations();
        List<LocationGetDTO> locationGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Location location : locations) {
            locationGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLocationGetDTO(location));
        }
        return locationGetDTOs;

    }

    @PostMapping("/locations/filter")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LocationGetDTO> getFilteredLocation(@RequestBody FilterPostDTO filterPostDTO) {
        List<Location> locations =  locationService.getFilteredLocations(filterPostDTO);
        List<LocationGetDTO> locationGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (Location location : locations) {
            locationGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLocationGetDTO(location));
        }
        return locationGetDTOs;
    }

    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LocationGetDTO createLocation(@RequestBody LocationPostDTO locationPostDTO) {
        // convert API user to internal representation
        Location locationInput = DTOMapper.INSTANCE.convertLocationPostDTOtoEntity(locationPostDTO);

        // create user
        Location locationCreated = locationService.createLocation(locationInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLocationGetDTO(locationCreated);
    }

    @GetMapping("/locations/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LocationGetDTO getLocation(@PathVariable int locationId) {

        // get the location corresponding to the given ID
        Location location = locationService.getLocation(locationId);

        // convert the location to the API representation
        return DTOMapper.INSTANCE.convertEntityToLocationGetDTO(location);
    }

    @PutMapping("/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateLocation(@PathVariable Long locationId, @RequestBody Location location) {
        locationService.updateLocation(location);
    }

    @GetMapping("/locations/chats/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ArrayList<MessageGetDTO> getChat(@PathVariable Integer locationId) {
        ArrayList<Message> messages = locationService.getChat(locationId);
        ArrayList<MessageGetDTO> messagesGetDTO = new ArrayList<>();

        for (Message message : messages){
            messagesGetDTO.add(DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message));
        }

        return messagesGetDTO;
    }

    @PutMapping("/locations/chats/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void postMessage(@PathVariable Integer locationId, @RequestBody MessagePostDTO messagePostDTO) {
        locationService.postMessage(locationId, DTOMapper.INSTANCE.convertMessagePostDTOToEntity(messagePostDTO));
    }

    @GetMapping("/locations/favorites/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Location> getFavoriteLocations(@PathVariable int userId) {
        return locationService.getFavoriteLocations(userId);
    }

    @PutMapping("/locations/favorites/{userId}/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateFavoriteLocations(@PathVariable int userId, @PathVariable Integer locationId) {
        locationService.updateFavoriteLocations(userId, locationId);
    }

    @DeleteMapping("/locations/favorites/{userId}/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteFavoriteLocation(@PathVariable int userId, @PathVariable Integer locationId) {
        locationService.deleteFavoriteLocation(userId, locationId);
    }

    @GetMapping("/locations/favorites/{userId}/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LocationGetDTO checkIncludedInFavoriteLocations(@PathVariable int userId, @PathVariable Integer locationId) {
        Location location = locationService.checkFavoriteLocations(userId, locationId);
        return DTOMapper.INSTANCE.convertEntityToLocationGetDTO(location);
    }

    @GetMapping("/locations/rating/{userId}/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int checkIncludedInRatings(@PathVariable int userId, @PathVariable Integer locationId) {
        int rating = locationService.checkRating(userId, locationId);
        return rating;
    }

    @GetMapping("/locations/rating/{locationId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public double getRating(@PathVariable Integer locationId) {
        double rating = locationService.checkAverageRating(locationId);
        return rating;
    }


    @PutMapping("/locations/rating/{userId}/{locationId}/{ratedStars}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateRating(@PathVariable int userId, @PathVariable Integer locationId, @PathVariable int ratedStars) {
        LocationService.updateRating(userId, locationId, ratedStars);
    }
}
