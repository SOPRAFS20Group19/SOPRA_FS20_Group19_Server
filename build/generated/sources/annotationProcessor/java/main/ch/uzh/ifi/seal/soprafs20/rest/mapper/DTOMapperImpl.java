package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.LocationGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.UserPostDTO;
import java.util.Arrays;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-16T18:47:39+0200",
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
        user.setUsername( userPostDTO.getUsername() );

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
        userGetDTO.setBirthDate( user.getBirthDate() );
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

        double[] coordinates = location.getCoordinates();
        if ( coordinates != null ) {
            locationGetDTO.setCoordinates( Arrays.copyOf( coordinates, coordinates.length ) );
        }
        locationGetDTO.setLocationType( location.getLocationType() );
        locationGetDTO.setId( location.getId() );

        return locationGetDTO;
    }
}
