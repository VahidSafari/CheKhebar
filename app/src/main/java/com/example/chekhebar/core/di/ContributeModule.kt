package com.example.chekhebar.core.di

import com.example.chekhebar.data.source.MapLocalDataSource
import com.example.chekhebar.data.source.MapRemoteDataSource
import com.example.chekhebar.data.source.MapRepository
import com.example.chekhebar.ui.MainActivity
import com.example.chekhebar.ui.MapViewModel
import com.example.chekhebar.ui.PlaceDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
interface ContributeModule {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun contributeMapRepository(): MapRepository

    @ContributesAndroidInjector
    fun contributeRemoteDataSource(): MapRemoteDataSource

    @ContributesAndroidInjector
    fun contributeLocalDataSource(): MapLocalDataSource

    @ContributesAndroidInjector
    fun contributeViewModel(): MapViewModel

    @ContributesAndroidInjector
    fun contributePlaceDetailActivity(): PlaceDetailActivity

}