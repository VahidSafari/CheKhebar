package com.example.chekhebar.data.source

import com.example.chekhebar.core.network.NetworkHandler
import com.example.chekhebar.data.PlaceEntity
import com.example.chekhebar.data.Result
import com.example.chekhebar.ui.PlaceView
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val remoteDataSource: MapRemoteDataSource,
    private val localDataSource: MapLocalDataSource,
    private val networkHandler: NetworkHandler
) {

    suspend fun getNearbyPlaces(
        lat: Double,
        long: Double,
        limit: Int,
        offset: Int,
        isInitialLoad: Boolean
    ): Result<List<PlaceView>>? {
        var result: Result<List<PlaceView>>? = null

        if (networkHandler.hasNetworkConnection()) {
            when (val remoteResponse = remoteDataSource.getNearbyPlaces(lat, long, limit, offset)) {
                is Result.Success -> {
                    val placeEntities = mutableListOf<PlaceEntity>()
                    remoteResponse.data.response.groups[0].items.forEach {
                        placeEntities.add(
                            PlaceEntity(
                                it.venue.id,
                                it.venue.name,
                                it.venue.location.distance,
                                it.venue.location.formattedAddress[0],
                                it.venue.categories[0].name
                            )
                        )
                    }
                    if (isInitialLoad)
                        localDataSource.removeAllPlaces()
                    localDataSource.insertPlaces(placeEntities)
                    result =
                        if (isInitialLoad)
                            Result.Success(localDataSource.getAllPlaces().map { it.toPlaceView() })
                        else Result.Success(placeEntities.map { it.toPlaceView() })
                }
                is Result.Error -> {
                    result = Result.Error(
                        remoteResponse.message,
                        localDataSource.getAllPlaces().map { it.toPlaceView() })
                }
            }
        } else {
            result = Result.Error(
                "خطا در اتصال به اینترنت",
                localDataSource.getAllPlaces().map { it.toPlaceView() })
        }
        return result
    }
}