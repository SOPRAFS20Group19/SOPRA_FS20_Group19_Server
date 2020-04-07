package ch.uzh.ifi.seal.soprafs20.rest.dto;

import java.util.List;

public class LocationGetDTO {
    private int id;
    private List coordinates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List coordinates) {
        this.coordinates = coordinates;
    }
}
