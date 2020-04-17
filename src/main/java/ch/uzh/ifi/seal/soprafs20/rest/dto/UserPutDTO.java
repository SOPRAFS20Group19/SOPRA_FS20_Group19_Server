package ch.uzh.ifi.seal.soprafs20.rest.dto;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

// representation of the user data given in a put request
public class UserPutDTO {

    private String username;
    private UserStatus status;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getOnlineStatus() {
        return status;
    }

    public void setOnlineStatus(UserStatus status) {
        this.status = status;
    }


}
