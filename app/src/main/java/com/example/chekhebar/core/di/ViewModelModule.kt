package com.example.chekhebar.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chekhebar.ui.MapViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    abstract fun contributeMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}