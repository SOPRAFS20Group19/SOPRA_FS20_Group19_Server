package ch.uzh.ifi.seal.soprafs20.rest.dto;
import ch.uzh.ifi.seal.soprafs20.constant.LocationType;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;


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
    private String openingHours;
    private String cost;
    private String category;
    private int slabQuality;
    private String net;
    private int view;
    private int peace;
    private int romantics;
    private int comfort;

    public int getComfort() {
        return comfort;
    }

    public int getPeace() {
        return peace;
    }

    public int getRomantics() {
        return romantics;
    }

    public int getView() {
        return view;
    }

    public void setComfort(int comfort) {
        this.comfort = comfort;
    }

    public void setPeace(int peace) {
        this.peace = peace;
    }

    public void setRomantics(int romantics) {
        this.romantics = romantics;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getSlabQuality() {
        return slabQuality;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setSlabQuality(int slabQuality) {
        this.slabQuality = slabQuality;
    }

    public String getCategory() {
        return category;
    }

    public String getCost() {
        return cost;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

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
