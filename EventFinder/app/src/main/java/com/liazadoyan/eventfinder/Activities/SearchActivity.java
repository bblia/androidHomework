package com.liazadoyan.eventfinder.Activities;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.liazadoyan.eventfinder.ApiObjects.Event;
import com.liazadoyan.eventfinder.Utils.EventLocation;
import com.liazadoyan.eventfinder.Fragments.EventListFragment;
import com.liazadoyan.eventfinder.Fragments.SearchDialogFragment;
import com.liazadoyan.eventfinder.Utils.BusProvider;
import com.liazadoyan.eventfinder.Events.SearchEvents;
import com.liazadoyan.eventfinder.Fragments.MapFragment;
import com.liazadoyan.eventfinder.R;
import com.liazadoyan.eventfinder.ApiObjects.SearchApi;
import com.liazadoyan.eventfinder.ApiObjects.SearchService;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RestAdapter;



public class SearchActivity extends AppCompatActivity implements AppCompatCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private AppCompatDelegate delegate;

    private Bus mBus = BusProvider.getInstance();
    private MapFragment mMapFrag;
    private EventListFragment mListFrag;
    private SearchService mSearchService;

    LocationManager locManager;
    LocationListener locListener;

    protected GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private TextView mSearchArea;
    private ImageButton mViewToggle;
    private Fragment mCurrentFrag;
    private Event[] mEvents;
    private String mKeyword = "";
    private Place mCurrentPlace;
    private Double mCurrentLat;
    private Double mCurrentLng;
    private Location mLastLocation;
    private boolean myLocation;
    private EventLocation mSelectedEventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar= (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mMapFrag = new MapFragment();
        mListFrag = new EventListFragment();

        addListFragment();
        addMapFragment();
        mCurrentFrag = mListFrag;
        myLocation = true;

        mSearchArea = (TextView)toolbar.findViewById(R.id.search_area);

        mViewToggle = (ImageButton)toolbar.findViewById(R.id.view_toggle_icon);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mSearchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialogFragment dialog = SearchDialogFragment.newInstance(mKeyword, mSelectedEventLocation);
                dialog.setEnterTransition(R.anim.abc_slide_in_top);
                dialog.setExitTransition(R.anim.abc_slide_out_top);
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        mViewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchViews();
            }
        });


    }

    private void switchViews() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out);

        if (mCurrentFrag == mListFrag) {
            transaction.hide(mListFrag);
            transaction.show(mMapFrag);
            mViewToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_sort_white_36dp));
            mCurrentFrag = mMapFrag;
        } else {
            transaction.hide(mMapFrag);
            transaction.show(mListFrag);
            mViewToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_map_white_36dp));
            mCurrentFrag = mListFrag;
        }
        transaction.commit();
    }


    @Override
    protected void onPause() {
        mBus.unregister(this); //listen for "global" events
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchService = new SearchService(buildApi(), mBus);
        mBus.register(this); //listen for "global" events

        if (myLocation) {
            //query my location
            getLastCurrentLocation();
        } else { //load last location we were looking at
            myLocation = false;
            getBus().post(new SearchEvents.LoadSearchResultsEvent(
                    mKeyword, mSelectedEventLocation.getLatitude(), mSelectedEventLocation.getLongitude(), true, mSelectedEventLocation.getAddress()));
        }


    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

    private void addListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentContainer, mListFrag);
        transaction.commit();
    }

    private void addMapFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentContainer, mMapFrag);
        transaction.commit();
    }

    private SearchApi buildApi() {
        return new RestAdapter.Builder()
                .setEndpoint("https://www.eventbriteapi.com/v3/")
                .build()
                .create(SearchApi.class);
    }

    @Subscribe
    public void startLoad(SearchEvents.LoadSearchResultsEvent event) {
        startLoading();
        myLocation = event.isMyLocation();
        mSelectedEventLocation = new EventLocation();
        mSelectedEventLocation.setLatitude(event.getLatitude());
        mSelectedEventLocation.setLongitude(event.getLongitude());
        if (event.isMyLocation()) {
            mSelectedEventLocation.setAddress("Current Location");
        } else {
            mSelectedEventLocation.setAddress(event.getAddress());
        }
    }

    @Subscribe
    public void loadEvents(SearchEvents.EventsLoadedEvent event) {

        mEvents = event.getResponse().getEvents();
        mListFrag.setEvents(mEvents);
        mMapFrag.setEvents(mEvents);

        stopLoading();
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void updateEditTextWithPlace(String keyword, String place) {
        if (keyword.equals("") || keyword == null) {
            keyword = "All Events";
        }
        mSearchArea.setText("\"" + keyword + "\"" + " near " + place);
    }

    private void startLoading() {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }


    @Override
    public void onConnected(Bundle bundle) {
        getLastCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void getLastCurrentLocation() {
        if (myLocation) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                getBus().post(new SearchEvents.LoadSearchResultsEvent(
                        mKeyword, mLastLocation.getLatitude(), mLastLocation.getLongitude(), true, "Current Location"));


                setEventLocationToMyLocation();
            } else {
                mSelectedEventLocation = new EventLocation();
            }
            updateEditTextWithPlace(mKeyword, mSelectedEventLocation.getAddress());
        }
    }

    private void copySelectionIntoEventLocation(Place place) {

        mSelectedEventLocation = new EventLocation();
        mSelectedEventLocation.setLatitude(place.getLatLng().latitude);
        mSelectedEventLocation.setLongitude(place.getLatLng().longitude);
        mSelectedEventLocation.setAddress(place.getAddress().toString());
    }

    private void setEventLocationToMyLocation() {

        mSelectedEventLocation = new EventLocation();
        mSelectedEventLocation.setLatitude(mLastLocation.getLatitude());
        mSelectedEventLocation.setLongitude(mLastLocation.getLongitude());
        mSelectedEventLocation.setAddress("Current Location");
    }

}
