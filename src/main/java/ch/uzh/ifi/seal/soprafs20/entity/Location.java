package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Location {

    private int id;
    private String address;
    private String additionalInformation;
    private double longitude;
    private double latitude;
    private double[] coordinates;
    private LocationType locationType;
    private String picture;

    public Location(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getInformation() {
        return additionalInformation;
    }

    public void setInformation(String information) {
        this.additionalInformation = information;
    }



    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.longitude = coordinates[0];
        this.latitude = coordinates[1];
        this.coordinates = coordinates;
    }


    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }




    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
