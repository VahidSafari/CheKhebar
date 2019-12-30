package com.example.chekhebar.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chekhebar.ui.PlaceView

@Entity(
    tableName = "places_table"
)
class PlaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val distance: Int
) {
    fun toPlaceView() = PlaceView(id, name, distance)
}