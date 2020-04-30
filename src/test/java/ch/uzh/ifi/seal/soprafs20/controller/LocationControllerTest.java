package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.FilterPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import ch.uzh.ifi.seal.soprafs20.service.LocationService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the LocationController i.e. GET/POST request without actually sending them over the network.
 * This tests if the LocationController works.
 */


@WebMvcTest(LocationController.class)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    // Code 200 get /locations/{locationId}
    @Test
    public void getLocation_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.getLocation(1)).willReturn(location);

        MockHttpServletRequestBuilder getRequest = get("/locations/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId(), (Integer) JsonPath.parse(responseAsString).read("$.id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$.address"));
        Assertions.assertEquals(location.getLongitude(), JsonPath.parse(responseAsString).read("$.longitude"));
        Assertions.assertEquals(location.getLatitude(), JsonPath.parse(responseAsString).read("$.latitude"));
        Assertions.assertEquals(location.getAdditionalInformation()[0], JsonPath.parse(responseAsString).read("$.additionalInformation.[0]"));
        Assertions.assertEquals(location.getAdditionalInformation()[1], JsonPath.parse(responseAsString).read("$.additionalInformation.[1]"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$.locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 404 get /locations/{locationId}
    @Test
    public void getLocation_invalidInput() throws Exception{
        given(locationService.getLocation(Mockito.anyInt())).willThrow(new LocationNotFoundException("This location could not be found."));

        MockHttpServletRequestBuilder getRequest = get("/locations/99")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This location could not be found.", response.getResolvedException().getMessage());
    }

    // Code 200 get /locations
    @Test
    public void getLocations_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        List<Location> allLocations = new ArrayList<>();
        allLocations.add(location);

        given(locationService.getLocations()).willReturn(allLocations);

        MockHttpServletRequestBuilder getRequest = get("/locations/").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId(), (Integer) JsonPath.parse(responseAsString).read("$[0].id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$[0].address"));
        Assertions.assertEquals(location.getLongitude(), JsonPath.parse(responseAsString).read("$[0].longitude"));
        Assertions.assertEquals(location.getLatitude(), JsonPath.parse(responseAsString).read("$[0].latitude"));
        Assertions.assertEquals(location.getAdditionalInformation()[0], JsonPath.parse(responseAsString).read("$[0].additionalInformation.[0]"));
        Assertions.assertEquals(location.getAdditionalInformation()[1], JsonPath.parse(responseAsString).read("$[0].additionalInformation.[1]"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$[0].locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 201 post /locations
    @Test
    public void createLocation_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.createLocation(Mockito.any())).willReturn(location);

        MockHttpServletRequestBuilder postRequest = post("/locations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(postRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId(), (Integer) JsonPath.parse(responseAsString).read("$.id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$.address"));
        Assertions.assertEquals(location.getLongitude(), JsonPath.parse(responseAsString).read("$.longitude"));
        Assertions.assertEquals(location.getLatitude(), JsonPath.parse(responseAsString).read("$.latitude"));
        Assertions.assertEquals(location.getAdditionalInformation()[0], JsonPath.parse(responseAsString).read("$.additionalInformation.[0]"));
        Assertions.assertEquals(location.getAdditionalInformation()[1], JsonPath.parse(responseAsString).read("$.additionalInformation.[1]"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$.locationType"));
        Assertions.assertEquals(201, response.getResponse().getStatus());
    }

    // Code 409 post /locations
    @Test
    public void createLocation_locationAlreadyExists() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.createLocation(Mockito.any())).willThrow(new DuplicatedLocationException("This location already exists."));

        MockHttpServletRequestBuilder postRequest = post("/locations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(postRequest).andReturn();

        Assertions.assertEquals(409, response.getResponse().getStatus());
        Assertions.assertEquals("This location already exists.", response.getResolvedException().getMessage());
    }

    // Code 200 get /locations/chats/{locationId}
    @Test
    public void getChatOfLocation_validInput() throws Exception{

        Message message = new Message();
        message.setContent("Hello");
        message.setSenderId(1);
        message.setTimestamp("29.04, 19:55");
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        given(locationService.getChat(1)).willReturn((ArrayList<Message>) messages);

        MockHttpServletRequestBuilder getRequest = get("/locations/chats/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(messages.get(0).getContent(), JsonPath.parse(responseAsString).read("$.[0].content"));
        Assertions.assertEquals(messages.get(0).getSenderUsername(), JsonPath.parse(responseAsString).read("$.[0].senderId"));
        Assertions.assertEquals(messages.get(0).getTimestamp(), JsonPath.parse(responseAsString).read("$.[0].timestamp"));
        Assertions.assertEquals(200, response.getResponse().getStatus());

    }

    // Code 404 get /locations/chats/{locationId}
    @Test
    public void getChatOfLocation_invalidInput() throws Exception{
        given(locationService.getChat(Mockito.anyInt())).willThrow(new LocationNotFoundException("This location could not be found."));

        MockHttpServletRequestBuilder getRequest = get("/locations/chats/99")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This location could not be found.", response.getResolvedException().getMessage());
    }

    // Code 204 put /locations/chats/{locationId}
    @Test
    public void postMessageToLocation_validInput() throws Exception{
        Message message = new Message();
        message.setContent("Hello");
        message.setSenderId(1);
        message.setTimestamp("29.04, 19:55");

        doNothing().when(locationService).postMessage(1, message);

        MockHttpServletRequestBuilder putRequest = put("/locations/chats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(message));

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(204, response.getResponse().getStatus());
        Assertions.assertEquals("", response.getResponse().getContentAsString());

    }

    // Code 404 put /locations/chats/{locationId}
    @Test
    public void postMessageToLocation_invalidInput() throws Exception{
        Message message = new Message();
        message.setContent("Hello");
        message.setSenderId(1);
        message.setTimestamp("19:55");

        doThrow(new LocationNotFoundException("This location could not be found.")).when(locationService).postMessage(Mockito.anyInt(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/locations/chats/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(message));

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This location could not be found.", response.getResolvedException().getMessage());
    }

    // Code 200 get /locations/favorites/{userId}
    @Test
    public void getFavoritesOfUser_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        List<Location> favoriteLocations = new ArrayList<>();
        favoriteLocations.add(location);

        given(locationService.getFavoriteLocations(1)).willReturn(favoriteLocations);

        MockHttpServletRequestBuilder getRequest = get("/locations/favorites/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId(), (Integer) JsonPath.parse(responseAsString).read("$[0].id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$[0].address"));
        Assertions.assertEquals(location.getLongitude(), JsonPath.parse(responseAsString).read("$[0].longitude"));
        Assertions.assertEquals(location.getLatitude(), JsonPath.parse(responseAsString).read("$[0].latitude"));
        Assertions.assertEquals(location.getAdditionalInformation()[0], JsonPath.parse(responseAsString).read("$[0].additionalInformation.[0]"));
        Assertions.assertEquals(location.getAdditionalInformation()[1], JsonPath.parse(responseAsString).read("$[0].additionalInformation.[1]"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$[0].locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 404 get /locations/favorites/{userId}
    @Test
    public void getFavoritesOfUser_invalidInput() throws Exception{
        given(locationService.getFavoriteLocations(Mockito.anyInt())).willThrow(new UserNotFoundException("This user could not be found."));

        MockHttpServletRequestBuilder getRequest = get("/locations/favorites/99")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This user could not be found.", response.getResolvedException().getMessage());
    }

    // Code 204 put /locations/favorites/{userId}/{locationId}
    @Test
    public void updateFavoritesOfUser_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        doNothing().when(locationService).updateFavoriteLocations(1, 1);

        MockHttpServletRequestBuilder putRequest = put("/locations/favorites/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(204, response.getResponse().getStatus());
        Assertions.assertEquals("", response.getResponse().getContentAsString());
    }

    // Code 200 delete /locations/favorites/{userId}
    @Test
    public void deleteFavoritesOfUser_validInput() throws Exception{
        Location location = new Location();
        location.setId(1);
        location.setAddress("Street Nr");
        location.setCoordinates(new double[]{47.35, 8.5});
        location.setAdditionalInformation(new String[]{"Infos", "Info2"});
        location.setLocationType(LocationType.FIREPLACE);

        doNothing().when(locationService).deleteFavoriteLocation(1, 1);

        MockHttpServletRequestBuilder deleteRequest = delete("/locations/favorites/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(deleteRequest).andReturn();

        Assertions.assertEquals(204, response.getResponse().getStatus());
        Assertions.assertEquals("", response.getResponse().getContentAsString());
    }

    // Code 404 put /locations/favorites/{userId}/{locationId}
    @Test
    public void updateFavoritesOfUser_invalidInput() throws Exception{
        doThrow(new UserNotFoundException("This user could not be found.")).when(locationService).updateFavoriteLocations(Mockito.anyInt(), Mockito.anyInt());

        MockHttpServletRequestBuilder putRequest = put("/locations/favorites/99/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This user could not be found.", response.getResolvedException().getMessage());
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SopraServiceException(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}

