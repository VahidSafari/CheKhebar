package com.example.chekhebar.data.source

import androidx.room.*
import com.example.chekhebar.data.PlaceEntity

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(placeList: List<PlaceEntity>)

    @Query("SELECT * FROM places_table")
    suspend fun getAllPlaces(): List<PlaceEntity>

    @Query("DELETE FROM places_table")
    suspend fun removeAllPlaces()
}