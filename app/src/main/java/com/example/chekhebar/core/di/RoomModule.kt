package com.example.chekhebar.core.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chekhebar.MapApp
import com.example.chekhebar.core.db.MapDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    private val databaseName = "map.db"

    @Provides
    @Singleton
    fun provideRoomDatabase(app: MapApp) =
        Room.databaseBuilder(
            app,
            MapDatabase::class.java,
            databaseName
        ).build()

    @Provides
    @Singleton
    fun provideMapDao(db: MapDatabase) = db.getMapDao()

}