package com.example.chekhebar.core.di

import com.example.chekhebar.MapApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RoomModule::class,
        RetrofitModule::class,
        AndroidInjectionModule::class,
        ContributeModule::class,
        FusedModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<MapApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: MapApp): Builder

        fun build(): ApplicationComponent
    }

    override fun inject(app: MapApp)

}
