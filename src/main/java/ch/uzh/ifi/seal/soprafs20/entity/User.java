package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import java.util.ArrayList;

/**
 * Internal User Representation
 */

public class User {



	private int id;

	private String name;

	private String username;

	private String token;

	private String password;

	private String creationDate;

	private String birthDate;

	private UserStatus status;

	private int avatarNr;

	private ArrayList favoriteLocations;

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getCreationDate(){ return creationDate; }

	public void setCreationDate(String creationDate){ this.creationDate = creationDate;}

	public ArrayList getFavoriteLocations() {
		return favoriteLocations;
	}

	public void setFavoriteLocations(ArrayList favoriteLocations) {
		this.favoriteLocations = favoriteLocations;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public int getAvatarNr(){ return avatarNr; }

	public void setAvatarNr(int avatarNr) {
		this.avatarNr = avatarNr;
	}

	public ArrayList<Integer> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(ArrayList<Integer> friendsList) {
		this.friendsList = friendsList;
	}
}
