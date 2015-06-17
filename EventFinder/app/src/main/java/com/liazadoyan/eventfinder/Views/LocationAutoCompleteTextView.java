package com.liazadoyan.eventfinder.Views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class LocationAutoCompleteTextView extends AutoCompleteTextView {
    public LocationAutoCompleteTextView(Context context) {
        super(context);
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LocationAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
