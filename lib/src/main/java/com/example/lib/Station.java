package com.example.lib;


public class Station {
    private String stationName;
    private int availableDocks;
    private int availableBikes;
    private String statusValue;
    private double stationLatitude;
    private double stationLongitude;
    public double distance;

    public Station(String setStationName, int setAvailableDocks, int setAvailableBikes, String setStatusValue, double setLatitude, double setLongitude) {
        stationName = setStationName;
        availableDocks = setAvailableDocks;
        availableBikes = setAvailableBikes;
        statusValue = setStatusValue;
        stationLatitude = setLatitude;
        stationLongitude = setLongitude;
    }
    public void setDistance() {
        double lon = stationLongitude - getLongitude();
        double lat = stationLatitude - getLatitude();
        distance = Math.sqrt((lon * lon) + (lat * lat));
    }
    /**
     * return the station's name.
     * @return station name
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * get the number of docks.
     * @return int representing the number of available docks.
     */
    public int getAvailableDocks() {
        return availableDocks;
    }

    /**
     * Return the number of available bikes
     * @return int representing the number of available bikes.
     */
    public int getAvailableBikes() {
        return availableBikes;
    }

    /**
     * Return the status of operation of the stop location.
     * @return String stating the status.
     */
    public String getStatusValue() {
        return statusValue;
    }

    /**
     * Get the latitude of the station location.
     * @return double representing the latitude of the stop.
     */
    public double getLatitude() {
        return stationLatitude;
    }

    /**
     * Get the latitude of the station location.
     * @return boi (hey andy, you are the worst)
     */
    public double getLongitude() {
        return stationLongitude;
    }
}
