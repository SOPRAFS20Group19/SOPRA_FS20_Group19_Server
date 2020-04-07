package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;

import java.lang.reflect.Array;
import java.util.List;

public class Location {

    private int id;
    //private String additional_information;
    private List coordinates;
    //private LocationType locationType;
    //private String picture;

    public Location(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
     */


    public List getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List coordinates) {
        this.coordinates = coordinates;
    }

    /*
    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

     */

    /*
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

     */
}
