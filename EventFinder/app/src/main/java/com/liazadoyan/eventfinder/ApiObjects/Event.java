package com.liazadoyan.eventfinder.ApiObjects;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class Event {
    private Venue venue;
    private Name name;
    private Description description;
    private Logo logo;
    private DatetimeTZ start;

    public Venue getVenue() {
        return venue;
    }

    public Name getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Logo getLogo() {
        return logo;
    }

    public DatetimeTZ getStart() {
        return start;
    }
}
