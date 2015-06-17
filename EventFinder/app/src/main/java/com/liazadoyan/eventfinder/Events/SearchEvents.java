package com.liazadoyan.eventfinder.Events;

import com.liazadoyan.eventfinder.ApiObjects.SearchResponse;

import retrofit.RetrofitError;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class SearchEvents {

    public static class LoadSearchResultsEvent {
        private String query;
        private String radius;
        private Double latitude;
        private Double longitude;
        private String token;
        private boolean myLocation;
        private String address;

        public LoadSearchResultsEvent(String query, Double latitude, Double longitude, boolean myLocation, String address){
            this.query = query;
            this.latitude = latitude;
            this.longitude = longitude;
            this.myLocation = myLocation;
            this.address = address;
        }

        public String getQuery() {
            return query;
        }

        public String getRadius() {
            return "15mi";
        }

        public Double getLatitude() {
            return latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public String getToken() {
            return "W22VYLDJ6NHHYRVUJSU4";
        }

        public boolean isMyLocation() {
            return myLocation;
        }

        public String getAddress() {
            return address;
        }
    }
    public static class ApiErrorEvent {
        RetrofitError errorMessage;
        public ApiErrorEvent(RetrofitError errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage.getMessage();
        }
    }

    public static class EventsLoadedEvent {
        private SearchResponse response;

        public EventsLoadedEvent(SearchResponse response) {
            this.response = response;
        }

        public SearchResponse getResponse() {
            return response;
        }
    }
}
