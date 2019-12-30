package com.example.chekhebar.data.source

import com.example.chekhebar.data.MainResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {
    @GET("v2/venues/explore")
    suspend fun getNearbyPlaces(
        @Query("ll") latLong: String = "36.323062,59.515162",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("client_id") clientId: String = "W00ZHLNRAT41DYG31IIUUQTI5FTHVGZI0NGCKMZHHPMTGZVX",
        @Query("client_secret") clientSecret: String = "AA1KUV2Y2DHNSEKI20SIFB4JFK03UQ5KDVZAI5BU5I5QKTHH",
        @Query("v") version: String = "20180323"
    ): Response<MainResponse>
}