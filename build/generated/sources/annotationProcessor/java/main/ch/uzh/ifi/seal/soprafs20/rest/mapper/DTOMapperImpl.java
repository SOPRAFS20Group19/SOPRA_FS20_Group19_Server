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
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-05-07T14:25:50+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
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
        user.setAvatarNr( userPostDTO.getAvatarNr() );
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
        user.setAvatarNr( userPutDTO.getAvatarNr() );
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
        ArrayList<Integer> arrayList = user.getFriendsList();
        if ( arrayList != null ) {
            userGetDTO.setFriendsList( new ArrayList<Integer>( arrayList ) );
        }
        userGetDTO.setName( user.getName() );
        userGetDTO.setAvatarNr( user.getAvatarNr() );
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
        location.setAbfall( locationPostDTO.getAbfall() );
        location.setLatitude( locationPostDTO.getLatitude() );
        location.setSlabQuality( locationPostDTO.getSlabQuality() );
        location.setLocationType( locationPostDTO.getLocationType() );
        location.setKinderwagen( locationPostDTO.getKinderwagen() );
        location.setRomantics( locationPostDTO.getRomantics() );
        location.setView( locationPostDTO.getView() );
        location.setOel( locationPostDTO.getOel() );
        location.setTisch( locationPostDTO.getTisch() );
        location.setBaujahr( locationPostDTO.getBaujahr() );
        location.setNet( locationPostDTO.getNet() );
        location.setLongitude( locationPostDTO.getLongitude() );
        location.setBrunnenart_txt( locationPostDTO.getBrunnenart_txt() );
        location.setCost( locationPostDTO.getCost() );
        location.setHolz( locationPostDTO.getHolz() );
        location.setGlas( locationPostDTO.getGlas() );
        location.setRost( locationPostDTO.getRost() );
        location.setComfort( locationPostDTO.getComfort() );
        location.setOrt( locationPostDTO.getOrt() );
        location.setHunde( locationPostDTO.getHunde() );
        location.setPeace( locationPostDTO.getPeace() );
        location.setAdresse( locationPostDTO.getAdresse() );
        location.setOpeningHours( locationPostDTO.getOpeningHours() );
        location.setBaden( locationPostDTO.getBaden() );
        location.setCategory( locationPostDTO.getCategory() );
        location.setParkplatz( locationPostDTO.getParkplatz() );
        location.setPlz( locationPostDTO.getPlz() );
        location.setTrinkwasser( locationPostDTO.getTrinkwasser() );

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

        messageGetDTO.setMessageId( message.getMessageId() );
        messageGetDTO.setSenderId( message.getSenderId() );
        messageGetDTO.setSenderUsername( message.getSenderUsername() );
        messageGetDTO.setContent( message.getContent() );
        messageGetDTO.setTimestamp( message.getTimestamp() );

        return messageGetDTO;
    }
}
