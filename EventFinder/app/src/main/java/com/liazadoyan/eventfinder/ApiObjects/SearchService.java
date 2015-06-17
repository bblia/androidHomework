/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liazadoyan.eventfinder.ApiObjects;

import com.liazadoyan.eventfinder.Events.SearchEvents;
import com.liazadoyan.eventfinder.Utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public final class SearchService {
    private SearchApi mApi;
    private Bus mBus;

    public SearchService(SearchApi api, Bus bus) {
        mApi = api;
        mBus = bus;
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onLoadSearchResults(SearchEvents.LoadSearchResultsEvent event) {
        mApi.getEventsAtLocation(event.getQuery(), event.getRadius(), event.getLatitude(), event.getLongitude(), event.getToken(), new Callback<SearchResponse>() {
            @Override
            public void success(SearchResponse response, Response rawResponse) {
                mBus.post(new SearchEvents.EventsLoadedEvent(response));
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new SearchEvents.ApiErrorEvent(error));
            }
        });
    }
}