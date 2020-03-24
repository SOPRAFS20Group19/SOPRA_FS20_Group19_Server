package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Location> getAllLocations() {
        return locationService.getLocations();
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
    public Location getLocation(@PathVariable Long locationId) {
        return locationService.getLocation(locationId);
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
    public List<Location> getFavoriteLocations(@PathVariable Long userId) {
        return locationService.getFavoriteLocations(userId);
    }

    @PutMapping("/locations/favorites/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateFavoriteLocations(@PathVariable Long userId, @RequestBody Long locationId) {
        locationService.updateFavoriteLocations(userId, locationId);
    }
}
