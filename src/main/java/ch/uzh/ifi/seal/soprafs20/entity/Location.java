package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.LocationType;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private int id;
    private String address;
    private String[] additionalInformation;
    private double longitude;
    private double latitude;
    private double[] coordinates;
    private LocationType locationType;
    private String picture;
    private int baujahr;
    private String art_txt;
    private String brunnenart_txt;
    private String adresse;
    private String plz;
    private String ort;
    private String metall;
    private String glas;
    private String oel;
    private String ausstattung;
    private String holz;
    private String rost;
    private String tisch;
    private String trinkwasser;
    private String abfall;
    private String parkplatz;
    private String baden;
    private String hunde;
    private String kinderwagen;

    public String getAbfall() {
        return abfall;
    }

    public String getBaden() {
        return baden;
    }

    public String getHunde() {
        return hunde;
    }

    public String getKinderwagen() {
        return kinderwagen;
    }

    public String getParkplatz() {
        return parkplatz;
    }

    public String getTisch() {
        return tisch;
    }

    public String getTrinkwasser() {
        return trinkwasser;
    }

    public void setAbfall(String abfall) {
        this.abfall = abfall;
    }

    public void setBaden(String baden) {
        this.baden = baden;
    }

    public void setHunde(String hunde) {
        this.hunde = hunde;
    }

    public void setKinderwagen(String kinderwagen) {
        this.kinderwagen = kinderwagen;
    }

    public void setParkplatz(String parkplatz) {
        this.parkplatz = parkplatz;
    }

    public void setTisch(String tisch) {
        this.tisch = tisch;
    }

    public void setTrinkwasser(String trinkwasser) {
        this.trinkwasser = trinkwasser;
    }

    public String getHolz() {
        return holz;
    }

    public String getRost() {
        return rost;
    }

    public void setHolz(String holz) {
        this.holz = holz;
    }

    public void setRost(String rost) {
        this.rost = rost;
    }

    public String getAusstattung() {
        return ausstattung;
    }

    public void setAusstattung(String ausstattung) {
        this.ausstattung = ausstattung;
    }

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

    public Location(){}

    public void setBrunnenart_txt(String brunnenart_txt) {
        this.brunnenart_txt = brunnenart_txt;
    }

    public void setBaujahr(int baujahr) {
        this.baujahr = baujahr;
    }

    public void setArt_txt(String art_txt) {
        this.art_txt = art_txt;
    }

    public String getBrunnenart_txt() {
        return brunnenart_txt;
    }

    public int getBaujahr() {
        return baujahr;
    }

    public String getArt_txt() {
        return art_txt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String[] getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String[] additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
