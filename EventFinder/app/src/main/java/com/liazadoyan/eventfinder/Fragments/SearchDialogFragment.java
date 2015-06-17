package com.liazadoyan.eventfinder.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.liazadoyan.eventfinder.Activities.SearchActivity;
import com.liazadoyan.eventfinder.Adapters.PlaceAutoCompleteAdapter;
import com.liazadoyan.eventfinder.Utils.EventLocation;
import com.liazadoyan.eventfinder.Events.SearchEvents;
import com.liazadoyan.eventfinder.R;
import com.liazadoyan.eventfinder.Utils.BusProvider;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class SearchDialogFragment extends DialogFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(32.7150, -117.1625), new LatLng(37.7833, -122.4167));

    private AutoCompleteTextView mAutocompleteView;
    private PlaceAutoCompleteAdapter mAdapter;
    protected GoogleApiClient mGoogleApiClient;
    private ImageButton mSearchButton;
    private EditText mKeywordEditText;
    private String mKeyword;
    private ImageButton myLocationButton;
    private EventLocation mSelectedEventLocation;
    private boolean myLocationEnabled;


    public SearchDialogFragment() {
        super();
    }

    public static SearchDialogFragment newInstance(String keyword, EventLocation selectedLocation) {
        SearchDialogFragment f = new SearchDialogFragment();
        f.mKeyword = keyword;
        f.mSelectedEventLocation = selectedLocation;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        attachViewToTop();



        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();

        myLocationButton = (ImageButton)v.findViewById(R.id.currentLocationIcon);

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocationEnabled = !myLocationEnabled;
                highlightMyLocation();
                if (!myLocationEnabled) {
                    mAutocompleteView.setText("");
                    mAutocompleteView.requestFocus();
                }
            }
        });


        mAutocompleteView = (AutoCompleteTextView) v.findViewById(R.id.atv_places);
        mAutocompleteView.setThreshold(1);

        mAutocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocationEnabled) {
                    clearMyLocation();

                }
            }
        });

        mAutocompleteView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (myLocationEnabled && hasFocus) {
                    clearMyLocation();
                }
            }
        });

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


        mAdapter = new PlaceAutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mAutocompleteView.setAdapter(mAdapter);

        mKeywordEditText = (EditText) v.findViewById(R.id.keyword_text);
        if (mKeyword != null) {
            mKeywordEditText.setText(mKeyword);
        }

        mSearchButton = (ImageButton) v.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new SearchEvents.LoadSearchResultsEvent(
                        mKeywordEditText.getText().toString(), mSelectedEventLocation.getLatitude(),
                        mSelectedEventLocation.getLongitude(), myLocationEnabled, mSelectedEventLocation.getAddress()));
                ((SearchActivity) getActivity()).updateEditTextWithPlace(mKeywordEditText.getText().toString(), mSelectedEventLocation.getAddress());
                dismiss();
            }
        });

        myLocationEnabled = mSelectedEventLocation == null || mSelectedEventLocation.getAddress().equals("Current Location"); //use my location in beginning
        highlightMyLocation();

        return v;
    }


    private void clearMyLocation() {
        myLocationEnabled = false;
        highlightMyLocation();
        mAutocompleteView.setText("");
        mAutocompleteView.clearListSelection();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            //copy from buffer so we can close
            copySelectionIntoEventLocation(place);

            places.release();
        }
    };

    private void copySelectionIntoEventLocation(Place place) {
        mSelectedEventLocation = new EventLocation();
        mSelectedEventLocation.setLatitude(place.getLatLng().latitude);
        mSelectedEventLocation.setLongitude(place.getLatLng().longitude);
        mSelectedEventLocation.setAddress(place.getAddress().toString());
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroyView() {
        mGoogleApiClient.disconnect();
        super.onDestroyView();
    }


    private void attachViewToTop() {
        getDialog().getWindow().setGravity(Gravity.START | Gravity.TOP);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();
        p.width = LinearLayout.LayoutParams.MATCH_PARENT;
        p.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        p.x = 500;
        getDialog().getWindow().setDimAmount(0.5f);
        getDialog().setCanceledOnTouchOutside(true);

        getDialog().getWindow().setAttributes(p);
    }

    private void highlightMyLocation() {
        tintButton(myLocationEnabled);

        if (myLocationEnabled || mSelectedEventLocation == null) {
            mAutocompleteView.setText("Current Location");
            mAutocompleteView.dismissDropDown();
        } else {
            mAutocompleteView.setText(mSelectedEventLocation.getAddress());
        }
    }

    private void tintButton(boolean myLocationEnabled) {
        int tint;
        if (myLocationEnabled) {
            tint = Color.parseColor("#FF03A9F4");
        } else {
            tint = Color.parseColor("#FF727272");
        }
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
        Drawable d = myLocationButton.getBackground();
        d.setColorFilter(tint ,mMode);
        myLocationButton.setBackgroundDrawable(d);
    }

}
