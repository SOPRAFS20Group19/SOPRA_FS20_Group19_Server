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
    @Mapping(source = "avatarNr", target = "avatarNr")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "avatarNr", target = "avatarNr")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "avatarNr", target = "avatarNr")
    @Mapping(source = "friendsList", target = "friendsList")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "coordinates", target = "coordinates")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "locationType", target = "locationType")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    LocationGetDTO convertEntityToLocationGetDTO(Location location);

    @Mapping(source = "locationType", target = "locationType")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "baujahr", target = "baujahr")
    @Mapping(source = "art_txt", target = "art_txt")
    @Mapping(source = "brunnenart_txt", target = "brunnenart_txt")
    @Mapping(source = "adresse", target = "adresse")
    @Mapping(source = "plz", target = "plz")
    @Mapping(source = "ort", target = "ort")
    @Mapping(source = "metall", target = "metall")
    @Mapping(source = "glas", target = "glas")
    @Mapping(source = "oel", target = "oel")
    @Mapping(source = "ausstattung", target = "ausstattung")
    @Mapping(source = "holz", target = "holz")
    @Mapping(source = "rost", target = "rost")
    @Mapping(source = "tisch", target = "tisch")
    @Mapping(source = "trinkwasser", target = "trinkwasser")
    @Mapping(source = "abfall", target = "abfall")
    @Mapping(source = "parkplatz", target = "parkplatz")
    @Mapping(source = "baden", target = "baden")
    @Mapping(source = "hunde", target = "hunde")
    @Mapping(source = "kinderwagen", target = "kinderwagen")
    @Mapping(source = "openingHours", target = "openingHours")
    @Mapping(source = "cost", target = "cost")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "slabQuality", target = "slabQuality")
    @Mapping(source = "net", target = "net")
    @Mapping(source = "view", target = "view")
    @Mapping(source = "peace", target = "peace")
    @Mapping(source = "romantics", target = "romantics")
    @Mapping(source = "comfort", target = "comfort")
    Location convertLocationPostDTOtoEntity(LocationPostDTO locationPostDTO);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "content", target = "content")
    Message convertMessagePostDTOToEntity(MessagePostDTO messagePostDTO);

    @Mapping(source = "senderId", target = "senderId")
    @Mapping(source = "senderUsername", target = "senderUsername")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(source = "messageId", target = "messageId")
    MessageGetDTO convertEntityToMessageGetDTO(Message message);

}

