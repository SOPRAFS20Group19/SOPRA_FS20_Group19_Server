package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapping;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */


public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("password");
        userPostDTO.setAvatarNr(1);

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getAvatarNr(), user.getAvatarNr());
    }

    @Test
    public void testUpdateUser_fromUserPutDTO_toUser_success() {
        // create UserPutDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setName("name");
        userPutDTO.setUsername("username");
        userPutDTO.setPassword("password");
        userPutDTO.setAvatarNr(1);

        // MAP -> Update user
        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        // check content
        assertEquals(userPutDTO.getName(), user.getName());
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
        assertEquals(userPutDTO.getAvatarNr(), user.getAvatarNr());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.OFFLINE);
        user.setFriendsList(new ArrayList<>());
        user.setCreationDate("01.01.2000");
        user.setAvatarNr(1);
        user.setId(1000);

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getName(), userGetDTO.getName());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getFriendsList(), userGetDTO.getFriendsList());
        assertEquals(user.getCreationDate(), userGetDTO.getCreationDate());
        assertEquals(user.getAvatarNr(), userGetDTO.getAvatarNr());
    }

    @Test
    public void testGetLocation_fromLocation_toLocationGetDTO_success() {
        // create Location
        Location location = new Location();
        location.setLongitude(0);
        location.setLatitude(0);
        location.setId(100);
        location.setAddress("Runwaystreet 123");
        location.setAdditionalInformation(new String[]{"Info", "moreInfo"});
        location.setLocationType(LocationType.FOUNTAIN);

        // MAP -> Create LocationGetDTO
        LocationGetDTO locationGetDTO = DTOMapper.INSTANCE.convertEntityToLocationGetDTO(location);

        // check content
        assertEquals(location.getId(), locationGetDTO.getId());
        assertEquals(location.getLatitude(), locationGetDTO.getLatitude());
        assertEquals(location.getLongitude(), locationGetDTO.getLongitude());
        assertEquals(location.getLocationType(), locationGetDTO.getLocationType());
        assertEquals(location.getAddress(), locationGetDTO.getAddress());
        assertEquals(location.getAdditionalInformation()[0], locationGetDTO.getAdditionalInformation()[0]);
        assertEquals(location.getAdditionalInformation()[1], locationGetDTO.getAdditionalInformation()[1]);
    }

    @Test
    public void testPostLocation_fromLocationPostDTO_toEntity_success() {
        // create LocationPostDTO
        LocationPostDTO locationPostDTO = new LocationPostDTO();
        locationPostDTO.setLongitude(0);
        locationPostDTO.setLatitude(0);
        locationPostDTO.setLocationType(LocationType.FOUNTAIN);
        locationPostDTO.setBaden("X");
        locationPostDTO.setBaujahr(1900);
        locationPostDTO.setBrunnenart_txt("öffentlicher Brunnen");
        locationPostDTO.setTrinkwasser("X");

        // MAP -> Create Location
        Location location = DTOMapper.INSTANCE.convertLocationPostDTOtoEntity(locationPostDTO);

        // check content
        assertEquals(location.getLatitude(), locationPostDTO.getLatitude());
        assertEquals(location.getLongitude(), locationPostDTO.getLongitude());
        assertEquals(location.getLocationType(), locationPostDTO.getLocationType());
        assertEquals(location.getBaden(), locationPostDTO.getBaden());
        assertEquals(location.getBaujahr(), locationPostDTO.getBaujahr());
        assertEquals(location.getBrunnenart_txt(), locationPostDTO.getBrunnenart_txt());
        assertEquals(location.getTrinkwasser(), locationPostDTO.getTrinkwasser());
    }

    @Test
    public void testSendMessage_fromMessagePostDTO_toEntity_success() {
        // create MessagePostDTO
        MessagePostDTO messagePostDTO = new MessagePostDTO();
        messagePostDTO.setContent("Hi");
        messagePostDTO.setSenderId(1000);

        // MAP -> Create Message
        Message message = DTOMapper.INSTANCE.convertMessagePostDTOToEntity(messagePostDTO);

        // check content
        assertEquals(messagePostDTO.getSenderId(), message.getSenderId());
        assertEquals(messagePostDTO.getContent(), message.getContent());
    }

    @Test
    public void testGetMessage_fromEntity_toMessageGetDTO_success() {
        // create Message
        Message message = new Message();
        message.setSenderUsername("username");
        message.setTimestamp("10.01, 13:00");
        message.setContent("Hi");
        message.setMessageId(1000);
        message.setSenderId(1000);

        // MAP -> Create MssageGetDTO
        MessageGetDTO messageGetDTO = DTOMapper.INSTANCE.convertEntityToMessageGetDTO(message);

        // check content
        assertEquals(message.getSenderUsername(), messageGetDTO.getSenderUsername());
        assertEquals(message.getContent(), messageGetDTO.getContent());
        assertEquals(message.getTimestamp(), messageGetDTO.getTimestamp());
        assertEquals(message.getSenderId(), messageGetDTO.getSenderId());
        assertEquals(message.getMessageId(), messageGetDTO.getMessageId());
    }

}


