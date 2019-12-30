package com.example.chekhebar.data.source

import com.example.chekhebar.data.MainResponse
import javax.inject.Inject
import com.example.chekhebar.data.Result
class MapRemoteDataSource @Inject constructor(private val mapService: MapService) {

    suspend fun getNearbyPlaces(lat: Double, long: Double): Result<MainResponse>? {
        var result: Result<MainResponse>? = null
        val serviceResponse = mapService.getNearbyPlaces("$lat,$long")
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
        return result
    }
}