package com.liazadoyan.eventfinder.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liazadoyan.eventfinder.ApiObjects.Event;
import com.liazadoyan.eventfinder.R;
import com.liazadoyan.eventfinder.Utils.MyLocationSource;

import java.util.Map;

/**
 * Created by liazadoyan on 6/13/15.
 */
public class MapFragment extends Fragment {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static MapView mMapView;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static Double latitude, longitude;
    private Event[] mEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap = mMapView.getMap();
        // Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
        setUpMapIfNeeded(); // For setting up the MapFragment
        return v;
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainer))
                    .getMap();
        }
        mMap.setMyLocationEnabled(true);
        setUpMap();
    }

    private void setUpMap() {
        // Set default zoom
        animateToLocation(new LatLng(34.5, -120.4));
    }

    public void setEvents(Event[] events) {
        this.mEvents = events;
        mMap.clear();
        addMarkersToMap(events);
    }

    public void animateToLocation(LatLng bounds) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds, 10));
    }

    private void addMarkersToMap(Event[] events) {
        for (Event event : events) {
            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(event.getVenue().getLat(), event.getVenue().getLng()))
                    .title(event.getName().getText()));
        }
        if (events.length > 0) {
            animateToLocation(new LatLng(events[events.length/2].getVenue().getLat(), events[events.length/2].getVenue().getLng()));
        }
    }
}