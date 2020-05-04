package ch.uzh.ifi.seal.soprafs20.rest.dto;

public class FilterPostDTO {
    private boolean fountains;

    private boolean fireplaces;

    private boolean recyclingStations;

    private boolean toilets;
    private boolean tableTennis;
    private boolean bench;

    public void setBench(boolean bench) {
        this.bench = bench;
    }

    public void setTableTennis(boolean tableTennis) {
        this.tableTennis = tableTennis;
    }

    public void setToilets(boolean toilets) {
        this.toilets = toilets;
    }

    public boolean showToilets() {
        return toilets;
    }
    public boolean showTableTennis(){return  tableTennis;}
    public boolean showBenches(){return  bench;}



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
