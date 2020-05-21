package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

// representation of the user data given in a put request
public class UserPutDTO {

    private String name;
    private String username;
    private String password;
    private String birthDate;
    private int avatarNr;

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAvatarNr(){ return avatarNr; }

    public void setAvatarNr(int avatarNr){
        this.avatarNr = avatarNr;
    }


}
