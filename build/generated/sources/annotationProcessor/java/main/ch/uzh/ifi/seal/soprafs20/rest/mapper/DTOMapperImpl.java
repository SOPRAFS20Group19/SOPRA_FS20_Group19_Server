package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LocationGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LocationPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MessageGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.MessagePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPutDTO;
import java.util.Arrays;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-29T14:25:57+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 13 (Oracle Corporation)"
)
public class DTOMapperImpl implements DTOMapper {

    @Override
    public User convertUserPostDTOtoEntity(UserPostDTO userPostDTO) {
        if ( userPostDTO == null ) {
            return null;
        }

        User user = new User();

        user.setName( userPostDTO.getName() );
        user.setPassword( userPostDTO.getPassword() );
        user.setUsername( userPostDTO.getUsername() );

        return user;
    }

    @Override
    public User convertUserPutDTOtoEntity(UserPutDTO userPutDTO) {
        if ( userPutDTO == null ) {
            return null;
        }

        User user = new User();

        user.setPassword( userPutDTO.getPassword() );
        user.setUsername( userPutDTO.getUsername() );
        user.setName( userPutDTO.getName() );
        user.setBirthDate( userPutDTO.getBirthDate() );

        return user;
    }

    @Override
    public UserGetDTO convertEntityToUserGetDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserGetDTO userGetDTO = new UserGetDTO();

        userGetDTO.setPassword( user.getPassword() );
        userGetDTO.setName( user.getName() );
        userGetDTO.setId( user.getId() );
        userGetDTO.setCreationDate( user.getCreationDate() );
        userGetDTO.setUsername( user.getUsername() );
        userGetDTO.setStatus( user.getStatus() );

        return userGetDTO;
    }

    @Override
    public LocationGetDTO convertEntityToLocationGetDTO(Location location) {
        if ( location == null ) {
            return null;
        }

        LocationGetDTO locationGetDTO = new LocationGetDTO();

        String[] additionalInformation = location.getAdditionalInformation();
        if ( additionalInformation != null ) {
            locationGetDTO.setAdditionalInformation( Arrays.copyOf( additionalInformation, additionalInformation.length ) );
        }
        locationGetDTO.setAddress( location.getAddress() );
        locationGetDTO.setLatitude( location.getLatitude() );
        double[] coordinates = location.getCoordinates();
        if ( coordinates != null ) {
            locationGetDTO.setCoordinates( Arrays.copyOf( coordinates, coordinates.length ) );
        }
        locationGetDTO.setLocationType( location.getLocationType() );
        locationGetDTO.setId( location.getId() );
        locationGetDTO.setLongitude( location.getLongitude() );

        return locationGetDTO;
    }

    @Override
    public Location convertLocationPostDTOtoEntity(LocationPostDTO locationPostDTO) {
        if ( locationPostDTO == null ) {
            return null;
        }

        Location location = new Location();

        location.setArt_txt( locationPostDTO.getArt_txt() );
        location.setMetall( locationPostDTO.getMetall() );
        location.setAusstattung( locationPostDTO.getAusstattung() );
        location.setLatitude( locationPostDTO.getLatitude() );
        location.setGlas( locationPostDTO.getGlas() );
        location.setLocationType( locationPostDTO.getLocationType() );
        location.setOrt( locationPostDTO.getOrt() );
        location.setOel( locationPostDTO.getOel() );
        location.setAdresse( locationPostDTO.getAdresse() );
        location.setBaujahr( locationPostDTO.getBaujahr() );
        location.setLongitude( locationPostDTO.getLongitude() );
        location.setBrunnenart_txt( locationPostDTO.getBrunnenart_txt() );
        location.setPlz( locationPostDTO.getPlz() );

        return location;
    }

    @Override
    public Message convertMessagePostDTOToEntity(MessagePostDTO messagePostDTO) {
        if ( messagePostDTO == null ) {
            return null;
        }

        Message message = new Message();

        message.setSenderId( messagePostDTO.getSenderId() );
        message.setContent( messagePostDTO.getContent() );

        return message;
    }

    @Override
    public MessageGetDTO convertEntityToMessageGetDTO(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageGetDTO messageGetDTO = new MessageGetDTO();

        messageGetDTO.setSenderId( message.getSenderUsername() );
        messageGetDTO.setContent( message.getContent() );
        messageGetDTO.setTimestamp( message.getTimestamp() );

        return messageGetDTO;
    }
}
