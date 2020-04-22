package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Location;
import ch.uzh.ifi.seal.soprafs20.entity.Message;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "creationDate", target = "creationDate")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "coordinates", target = "coordinates")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "locationType", target = "locationType")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    LocationGetDTO convertEntityToLocationGetDTO(Location location);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "timestamp", target = "timestamp")
    Message convertMessagePostDTOToEntity(MessagePostDTO messagePostDTO);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "timestamp", target = "timestamp")
    MessageGetDTO convertEntityToMessageGetDTO(Message message);

}

