package com.liazadoyan.eventfinder.ApiObjects;

import com.liazadoyan.eventfinder.ApiObjects.SearchResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by liazadoyan on 6/14/15.
 */
public interface SearchApi {
    @GET("/events/search")
    void getEventsAtLocation(
            @Query("q") String query, @Query("location.within") String radius,
                             @Query ("location.latitude") Double latitude,
                             @Query("location.longitude") Double longitude, @Query("token") String token,
                             Callback<SearchResponse> callback);
}
