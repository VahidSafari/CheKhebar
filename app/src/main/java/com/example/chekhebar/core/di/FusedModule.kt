package com.example.chekhebar.core.di

import com.example.chekhebar.MapApp
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FusedModule {

    @Provides
    @Singleton
    fun provideFusedLocationClient(app: MapApp): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app.applicationContext)

    @Provides
    @Singleton
    fun provideSettingClient(app: MapApp) =
        LocationServices.getSettingsClient(app.applicationContext)
}