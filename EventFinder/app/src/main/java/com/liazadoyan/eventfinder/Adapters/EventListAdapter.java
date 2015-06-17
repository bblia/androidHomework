package com.liazadoyan.eventfinder.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.liazadoyan.eventfinder.ApiObjects.Event;
import com.liazadoyan.eventfinder.ApiObjects.Logo;
import com.liazadoyan.eventfinder.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by liazadoyan on 6/14/15.
 */
public class EventListAdapter extends ArrayAdapter<Event> {

    private Event[] mItems;
    private final Context mContext;
    private LayoutInflater inflater;

    public EventListAdapter(Context context, int resource, Event[] dataSet) {
        super(context, resource, dataSet);
        this.mContext = context;
        this.mItems = dataSet;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Event getItem(int position) {
        return mItems[position];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Event event = mItems[position];

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.view_list_item, null);
        }

        final TextView title = (TextView)convertView.findViewById(R.id.event_title);
        final TextView startTime = (TextView)convertView.findViewById(R.id.event_description);
        final ImageView eventLogo = (ImageView)convertView.findViewById(R.id.event_image);

        title.setText(event.getName().getText());

        Logo logo = event.getLogo();
        if (logo != null) {
            String logoUrl = logo.getUrl();
            if (logoUrl != null) {
                Picasso.with(mContext).load(logoUrl).into(eventLogo);
            }
        }

        try {
            startTime.setText(event.getStart().getTimeString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void setEvents(Event[] events) {
        mItems = events;
        notifyDataSetChanged();
    }

}
