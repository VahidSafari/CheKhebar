package com.example.chekhebar.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chekhebar.data.PlaceEntity
import com.example.chekhebar.data.source.MapDao

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class MapDatabase : RoomDatabase() {
    abstract fun getMapDao(): MapDao
}