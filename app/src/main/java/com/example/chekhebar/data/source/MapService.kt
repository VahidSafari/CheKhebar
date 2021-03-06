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
        @Query("client_id") clientId: String = "2XXI1JQRV5XLYYSV5B0N5NY4L0CPFBL4ZCQVKXV1S4O4OY2E",
        @Query("client_secret") clientSecret: String = "U501IGOROIEEK2DN2WQYNJRKXFZBQDI4SHSQBTBJRFGBDZAC",
        @Query("v") version: String = "20180323"
    ): Response<MainResponse>

    @GET("v2/venues/{venueId}")
    suspend fun getPlaceDetails(
        @Path("venueId") placeId: String,
        @Query("client_id") clientId: String = "2XXI1JQRV5XLYYSV5B0N5NY4L0CPFBL4ZCQVKXV1S4O4OY2E",
        @Query("client_secret") clientSecret: String = "U501IGOROIEEK2DN2WQYNJRKXFZBQDI4SHSQBTBJRFGBDZAC",
        @Query("v") version: String = "20180323"
    ): Response<PlaceDetailResponse>

}