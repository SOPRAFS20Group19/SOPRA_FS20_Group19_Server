package ch.uzh.ifi.seal.soprafs20.rest.dto;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;


public class LocationPostDTO {
    private LocationType locationType;
    private double longitude;
    private double latitude;
    private int baujahr;
    private String art_txt;
    private String brunnenart_txt;
    private String adresse;
    private String plz;
    private String ort;
    private String metall;
    private String glas;
    private String oel;

    public String getAdresse() {
        return adresse;
    }

    public String getGlas() {
        return glas;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMetall() {
        return metall;
    }

    public String getOel() {
        return oel;
    }

    public String getOrt() {
        return ort;
    }

    public String getPlz() {
        return plz;
    }

    public void setGlas(String glas) {
        this.glas = glas;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setMetall(String metall) {
        this.metall = metall;
    }

    public void setOel(String oel) {
        this.oel = oel;
    }

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
