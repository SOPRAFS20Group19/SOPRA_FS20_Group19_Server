package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Chat;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
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
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.getLocation(1L)).willReturn(location);

        MockHttpServletRequestBuilder getRequest = get("/locations/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId().intValue(), (Integer) JsonPath.parse(responseAsString).read("$.id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$.address"));
        Assertions.assertEquals(location.getCoordinates(), JsonPath.parse(responseAsString).read("$.coordinates"));
        Assertions.assertEquals(location.getInformation(), JsonPath.parse(responseAsString).read("$.information"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$.locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 404 get /locations/{locationId}
    @Test
    public void getLocation_invalidInput() throws Exception{
        given(locationService.getLocation(Mockito.anyLong())).willThrow(new LocationNotFoundException("This location could not be found."));

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
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        List<Location> allLocations = new ArrayList<>();
        allLocations.add(location);

        given(locationService.getLocations()).willReturn(allLocations);

        MockHttpServletRequestBuilder getRequest = get("/locations/").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId().intValue(), (Integer) JsonPath.parse(responseAsString).read("$[0].id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$[0].address"));
        Assertions.assertEquals(location.getCoordinates(), JsonPath.parse(responseAsString).read("$[0].coordinates"));
        Assertions.assertEquals(location.getInformation(), JsonPath.parse(responseAsString).read("$[0].information"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$[0].locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 201 post /locations
    @Test
    public void createLocation_validInput() throws Exception{
        Location location = new Location();
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.createLocation(Mockito.any())).willReturn(location);

        MockHttpServletRequestBuilder postRequest = post("/locations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(postRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId().intValue(), (Integer) JsonPath.parse(responseAsString).read("$.id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$.address"));
        Assertions.assertEquals(location.getCoordinates(), JsonPath.parse(responseAsString).read("$.coordinates"));
        Assertions.assertEquals(location.getInformation(), JsonPath.parse(responseAsString).read("$.information"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$.locationType"));
        Assertions.assertEquals(201, response.getResponse().getStatus());
    }

    // Code 409 post /locations
    @Test
    public void createLocation_locationAlreadyExists() throws Exception{
        Location location = new Location();
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        given(locationService.createLocation(Mockito.any())).willThrow(new DuplicatedLocationException("This location already exists."));

        MockHttpServletRequestBuilder postRequest = post("/locations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(postRequest).andReturn();

        Assertions.assertEquals(409, response.getResponse().getStatus());
        Assertions.assertEquals("This location already exists.", response.getResolvedException().getMessage());
    }

    // Code 204 put /locations/{locationId}
    @Test
    public void updateLocation_validInput() throws Exception{
        Location location = new Location();
        location.setPicture("picture.url");

        doNothing().when(locationService).updateLocation(location);

        MockHttpServletRequestBuilder putRequest = put("/locations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(204, response.getResponse().getStatus());
        Assertions.assertEquals("", response.getResponse().getContentAsString());
    }

    // Code 404 put /locations/{locationId}
    @Test
    public void updateLocation_invalidInput() throws Exception{
        Location location = new Location();
        location.setPicture("picture.url");

        doThrow(new LocationNotFoundException("This location could not be found.")).when(locationService).updateLocation(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/locations/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(location));

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This location could not be found.", response.getResolvedException().getMessage());
    }

    // Code 200 get /locations/chats/{locationId}
    @Test
    public void getChatOfLocation_validInput() throws Exception{
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setCreationDate("2020-03-04 13:54:14.474");
        user.setBirthDate("05.07.1999");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        Message message = new Message();
        message.setContent("Hello");
        message.setSender(user);
        message.setTimestamp("19:55");
        List<Message> messages = new ArrayList<>();
        messages.add(message);

        Chat chat = new Chat();
        chat.setMessages(messages);

        given(locationService.getChat(1L)).willReturn(chat);

        MockHttpServletRequestBuilder getRequest = get("/locations/chats/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(chat.getMessages().get(0).getContent(), JsonPath.parse(responseAsString).read("$.messages.[0].content"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getName(), JsonPath.parse(responseAsString).read("$.messages.[0].sender.name"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getUsername(), JsonPath.parse(responseAsString).read("$.messages.[0].sender.username"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getPassword(), JsonPath.parse(responseAsString).read("$.messages.[0].sender.password"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getCreationDate(), JsonPath.parse(responseAsString).read("$.messages.[0].sender.creationDate"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getBirthDate(), JsonPath.parse(responseAsString).read("$.messages.[0].sender.birthDate"));
        Assertions.assertEquals(chat.getMessages().get(0).getSender().getId().intValue(), (Integer) JsonPath.parse(responseAsString).read("$.messages.[0].sender.id"));
        Assertions.assertEquals(chat.getMessages().get(0).getTimestamp(), JsonPath.parse(responseAsString).read("$.messages.[0].timestamp"));
        Assertions.assertEquals(200, response.getResponse().getStatus());

    }

    // Code 404 get /locations/chats/{locationId}
    @Test
    public void getChatOfLocation_invalidInput() throws Exception{
        given(locationService.getChat(Mockito.anyLong())).willThrow(new LocationNotFoundException("This location could not be found."));

        MockHttpServletRequestBuilder getRequest = get("/locations/chats/99")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This location could not be found.", response.getResolvedException().getMessage());
    }

    // Code 204 put /locations/chats/{locationId}
    @Test
    public void postMessageToLocation_validInput() throws Exception{
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setCreationDate("2020-03-04 13:54:14.474");
        user.setBirthDate("05.07.1999");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        Message message = new Message();
        message.setContent("Hello");
        message.setSender(user);
        message.setTimestamp("19:55");

        doNothing().when(locationService).postMessage(1L, message);

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
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setCreationDate("2020-03-04 13:54:14.474");
        user.setBirthDate("05.07.1999");
        user.setStatus(UserStatus.OFFLINE);
        user.setId(1L);

        Message message = new Message();
        message.setContent("Hello");
        message.setSender(user);
        message.setTimestamp("19:55");

        doThrow(new LocationNotFoundException("This location could not be found.")).when(locationService).postMessage(Mockito.anyLong(), Mockito.any());

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
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        List<Location> favoriteLocations = new ArrayList<>();
        favoriteLocations.add(location);

        given(locationService.getFavoriteLocations(1L)).willReturn(favoriteLocations);

        MockHttpServletRequestBuilder getRequest = get("/locations/favorites/1").contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();
        String responseAsString = response.getResponse().getContentAsString();

        Assertions.assertEquals(location.getId().intValue(), (Integer) JsonPath.parse(responseAsString).read("$[0].id"));
        Assertions.assertEquals(location.getAddress(), JsonPath.parse(responseAsString).read("$[0].address"));
        Assertions.assertEquals(location.getCoordinates(), JsonPath.parse(responseAsString).read("$[0].coordinates"));
        Assertions.assertEquals(location.getInformation(), JsonPath.parse(responseAsString).read("$[0].information"));
        Assertions.assertEquals(location.getLocationType().toString(), JsonPath.parse(responseAsString).read("$[0].locationType"));
        Assertions.assertEquals(200, response.getResponse().getStatus());
    }

    // Code 404 get /locations/favorites/{userId}
    @Test
    public void getFavoritesOfUser_invalidInput() throws Exception{
        given(locationService.getFavoriteLocations(Mockito.anyLong())).willThrow(new UserNotFoundException("This user could not be found."));

        MockHttpServletRequestBuilder getRequest = get("/locations/favorites/99")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult response = mockMvc.perform(getRequest).andReturn();

        Assertions.assertEquals(404, response.getResponse().getStatus());
        Assertions.assertEquals("This user could not be found.", response.getResolvedException().getMessage());
    }

    // Code 204 put /locations/favorites/{userId}
    @Test
    public void updateFavoritesOfUser_validInput() throws Exception{
        Location location = new Location();
        location.setId(1L);
        location.setAddress("Street Nr");
        location.setCoordinates("123");
        location.setInformation("Infos");
        location.setLocationType(LocationType.FIREPLACE);

        doNothing().when(locationService).updateFavoriteLocations(1L, 1L);

        MockHttpServletRequestBuilder putRequest = put("/locations/favorites/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1L));

        MvcResult response = mockMvc.perform(putRequest).andReturn();

        Assertions.assertEquals(204, response.getResponse().getStatus());
        Assertions.assertEquals("", response.getResponse().getContentAsString());
    }

    // Code 404 put /locations/favorites/{userId}
    @Test
    public void updateFavoritesOfUser_invalidInput() throws Exception{
        doThrow(new UserNotFoundException("This user could not be found.")).when(locationService).updateFavoriteLocations(Mockito.anyLong(), Mockito.anyLong());

        MockHttpServletRequestBuilder getRequest = put("/locations/favorites/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(1L));

        MvcResult response = mockMvc.perform(getRequest).andReturn();

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
