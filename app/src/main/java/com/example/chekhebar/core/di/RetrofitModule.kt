package com.example.chekhebar.core.di

import com.example.chekhebar.MapApp
import com.example.chekhebar.core.network.NetworkHandler
import com.example.chekhebar.data.source.MapService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request: Request = chain
                    .request()
                    .newBuilder()
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMapService(retrofit: Retrofit): MapService =
        retrofit.create(MapService::class.java)

    @Provides
    @Singleton
    fun provideNetworkHandler(app: MapApp) = NetworkHandler(app)
}