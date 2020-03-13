package ch.uzh.ifi.seal.soprafs20.rest.dto;

// representation of the user data given in a put request
public class UserPutDTO {

    private String username;

    private String birthDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
