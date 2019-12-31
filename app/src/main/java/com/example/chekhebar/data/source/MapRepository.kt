package com.example.chekhebar.data.source

import com.example.chekhebar.core.network.NetworkHandler
import com.example.chekhebar.data.PlaceEntity
import com.example.chekhebar.data.Result
import com.example.chekhebar.ui.PlaceView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val remoteDataSource: MapRemoteDataSource,
    private val localDataSource: MapLocalDataSource,
    private val networkHandler: NetworkHandler
) {

    suspend fun getNearbyPlaces(lat: Double, long: Double, limit: Int, offset: Int): Result<List<PlaceView>>? {
        var result: Result<List<PlaceView>>? = null

        if (networkHandler.hasNetworkConnection()) {
            when (val remoteResponse = remoteDataSource.getNearbyPlaces(lat, long, limit, offset)) {
                is Result.Success -> {
                    val placeEntities = mutableListOf<PlaceEntity>()
                    remoteResponse.data.response.groups[0].items.forEach {
                        placeEntities.add(
                            PlaceEntity(
                                it.venue.categories[0].id,
                                it.venue.name,
                                it.venue.location.distance
                            )
                        )
                    }
                    result = Result.Success(placeEntities.map { it.toPlaceView() }.sortedBy { it.distance })
                    localDataSource.insertPlaces(placeEntities)
                }
                is Result.Error -> {
                    result = Result.Error(
                        remoteResponse.message,
                        localDataSource.getAllPlaces().map { it.toPlaceView() })
                }
            }
        } else {
            result = Result.Error(
                "No network connection :|",
                localDataSource.getAllPlaces().map { it.toPlaceView() })
        }
        return result
    }

}