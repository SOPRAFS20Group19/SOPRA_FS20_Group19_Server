package ch.uzh.ifi.seal.soprafs20.rest.dto;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;


public class LocationPostDTO {
    private LocationType locationType;
    private double longitude;
    private double latitude;
    private int baujahr;
    private String art_txt;
    private String brunnenart_txt;

    public int getBaujahr() {
        return baujahr;
    }

    public String getArt_txt() {
        return art_txt;
    }

    public String getBrunnenart_txt() {
        return brunnenart_txt;
    }

    public void setArt_txt(String art_txt) {
        this.art_txt = art_txt;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public void setBrunnenart_txt(String brunnenart_txt) {
        this.brunnenart_txt = brunnenart_txt;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
