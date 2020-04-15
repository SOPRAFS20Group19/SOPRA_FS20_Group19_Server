package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class FilterPostDTO {
    private boolean fountains;

    private boolean fireplaces;

    private boolean recyclingStations;

    public boolean showFireplaces() {
        return fireplaces;
    }

    public boolean showFountains() {
        return fountains;
    }

    public boolean showRecyclingStations() {
        return recyclingStations;
    }

    public void setFountains(boolean fountains) {
        this.fountains = fountains;
    }

    public void setFireplaces(boolean fireplaces) {
        this.fireplaces = fireplaces;
    }

    public void setRecyclingStations(boolean recyclingStations) {
        this.recyclingStations = recyclingStations;
    }
}
