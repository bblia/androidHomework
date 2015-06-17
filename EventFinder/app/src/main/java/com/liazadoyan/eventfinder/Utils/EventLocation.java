package com.liazadoyan.eventfinder.Utils;

/**
 * Created by liazadoyan on 6/15/15.
 */
public class EventLocation {
    Double latitude;
    Double longitude;
    String address;

    public void setLatitude(Double lat) {
        this.latitude = lat;
    }

    public void setLongitude(Double lng) {
        this.longitude = lng;
    }

    public void address(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
