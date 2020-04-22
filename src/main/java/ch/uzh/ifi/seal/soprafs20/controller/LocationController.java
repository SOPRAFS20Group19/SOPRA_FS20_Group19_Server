package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.FilterPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LocationGetDTO;
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
    public Location createLocation(@RequestBody Location location) {
        return locationService.createLocation(location);
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
    public Chat getChat(@PathVariable Long locationId) {
        return locationService.getChat(locationId);
    }

    @PutMapping("/locations/chats/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void postMessage(@PathVariable Long locationId, @RequestBody Message message) {
        locationService.postMessage(locationId, message);
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
}
