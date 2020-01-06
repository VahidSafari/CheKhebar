package com.example.chekhebar.data.source

import com.example.chekhebar.data.MainResponse
import com.example.chekhebar.data.PlaceDetailResponse
import javax.inject.Inject
import com.example.chekhebar.data.Result
import java.lang.Exception

class MapRemoteDataSource @Inject constructor(private val mapService: MapService) {

    suspend fun getNearbyPlaces(lat: Double, long: Double, limit: Int, offset: Int): Result<MainResponse>? {
        var result: Result<MainResponse>? = null
        try {
            val serviceResponse = mapService.getNearbyPlaces("$lat,$long", limit, offset)
            when (serviceResponse.code()) {
                200 -> {
                    serviceResponse.body()?.let {
                        result = Result.Success(it)
                    }
                }
                400 -> {
                    serviceResponse.body()?.let {
                        result = Result.Error(it.meta.errorDetail ?: "Incorrect credentials")
                    }
                }
                410 -> {
                    serviceResponse.body()?.let {
                        result = Result.Error(it.meta.errorDetail ?: "invalid params")
                    }
                }
            }
        } catch (exception: Exception) {
            result = Result.Error(exception.message?:" EXCEPTION ")
        }
        return result
    }

    suspend fun getPlaceDetail(placeId: String): Result<PlaceDetailResponse>? {
        var result: Result<PlaceDetailResponse>? = null
        try {
            val serviceResponse = mapService.getPlaceDetails(placeId)
            when (serviceResponse.code()) {
                200 -> {
                    serviceResponse.body()?.let {
                        result = Result.Success(it)
                    }
                }
                else -> {
                    serviceResponse.body()?.let {
                        result = Result.Error(it.meta.errorDetail ?: "Incorrect credentials")
                    }
                }
            }
        } catch (exception: Exception) {
            result = Result.Error(exception.message?:" EXCEPTION ")
        }
        return result
    }
}