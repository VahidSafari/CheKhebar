package com.example.chekhebar.data.source

import com.example.chekhebar.data.PlaceEntity
import javax.inject.Inject

class MapLocalDataSource @Inject constructor(private val dao: MapDao) {

    suspend fun insertPlaces(placesList: List<PlaceEntity>) = dao.insertPlaces(placesList)

    suspend fun getAllPlaces() = dao.getAllPlaces()

    suspend fun removeAllPlaces() = dao.removeAllPlaces()
}