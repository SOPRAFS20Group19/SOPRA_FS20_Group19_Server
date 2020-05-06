package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import java.util.ArrayList;

public class UserGetDTO {

    private int id;
    private String name;
    private String username;
    private String password;
    private UserStatus status;
    private String creationDate;
    private int avatarNr;
    private ArrayList<Integer> friendsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getCreationDate(){ return creationDate;}

    public void setCreationDate(String creationDate){ this.creationDate = creationDate;}

    public int getAvatarNr(){return avatarNr;}

    public void setAvatarNr(int avatarNr){
        this.avatarNr = avatarNr;
    }

    public void setFriendsList(ArrayList<Integer> friendsList) {
        this.friendsList = friendsList;
    }

    public ArrayList<Integer> getFriendsList() {
        return friendsList;
    }
}
