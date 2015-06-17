package com.liazadoyan.eventfinder.ApiObjects;

import com.liazadoyan.eventfinder.ApiObjects.Event;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class SearchResponse {
    private Event[] events;

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public Event[] getEvents() {
        return events;
    }
}
