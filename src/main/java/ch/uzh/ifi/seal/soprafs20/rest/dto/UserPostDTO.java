package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class UserPostDTO {

    private String name;

    private String username;

    private String password;

    private int avatarNr;

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

    public int getAvatarNr(){ return avatarNr; }

    public void setAvatarNr(int avatarNr){
        this.avatarNr = avatarNr;
    }
}
