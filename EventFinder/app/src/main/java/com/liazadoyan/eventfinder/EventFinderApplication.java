package com.liazadoyan.eventfinder;

import android.app.Application;
import android.util.Log;

import com.liazadoyan.eventfinder.ApiObjects.SearchService;
import com.liazadoyan.eventfinder.Events.SearchEvents;
import com.squareup.otto.Subscribe;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class EventFinderApplication extends Application {
    private SearchService mSearchService;


    @Override
    public void onCreate() {
        super.onCreate();


    }



    @Subscribe
    public void onApiError(SearchEvents.ApiErrorEvent event) {
        //toast("Something went wrong, please try again.");
        Log.e("ReaderApp", event.getErrorMessage());
    }
}
