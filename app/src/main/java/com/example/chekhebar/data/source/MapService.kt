package com.example.chekhebar.data.source

import com.example.chekhebar.data.MainResponse
import com.example.chekhebar.data.PlaceDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MapService {
    @GET("v2/venues/explore")
    suspend fun getNearbyPlaces(
        @Query("ll") latLong: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("client_id") clientId: String = "C5BWYQRSL1EWPQ0IU3D22P3OSKVGHF3SLJJSQR2H0MTVQZAF",
        @Query("client_secret") clientSecret: String = "KMMQQGDDXCFLYAOJRGUV1QDUWSQURMGAUV3NK0EJRWZ0OUQZ",
        @Query("v") version: String = "20180323"
    ): Response<MainResponse>

    @GET("v2/venues/{venueId}")
    suspend fun getPlaceDetails(
        @Path("venueId") placeId: String,
        @Query("client_id") clientId: String = "C5BWYQRSL1EWPQ0IU3D22P3OSKVGHF3SLJJSQR2H0MTVQZAF",
        @Query("client_secret") clientSecret: String = "KMMQQGDDXCFLYAOJRGUV1QDUWSQURMGAUV3NK0EJRWZ0OUQZ",
        @Query("v") version: String = "20180323"
    ): Response<PlaceDetailResponse>

}