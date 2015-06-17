package com.liazadoyan.eventfinder.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.liazadoyan.eventfinder.Adapters.EventListAdapter;
import com.liazadoyan.eventfinder.ApiObjects.Event;
import com.liazadoyan.eventfinder.R;

/**
 * Created by liazadoyan on 6/14/15.
 */
public class EventListFragment extends Fragment {

    ListView mListView;
    EventListAdapter mListAdapter;
    Event[] mEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEvents = new Event[]{};
        mListAdapter = new EventListAdapter(getActivity(),R.layout.view_list_item, mEvents);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mListView = (ListView) v.findViewById(R.id.eventList);
        mListAdapter = new EventListAdapter(getActivity(), android.R.id.text1, mEvents);
        mListView.setAdapter(mListAdapter);
        return v;
    }


    public void setEvents(Event[] events) {
        mEvents = events;
        mListAdapter.setEvents(mEvents);
    }

}
