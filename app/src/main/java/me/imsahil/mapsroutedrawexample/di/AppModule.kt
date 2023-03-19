package me.imsahil.mapsroutedrawexample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.imsahil.mapsroutedrawexample.BuildConfig
import me.imsahil.mapsroutedrawexample.data.remote.MapDirectionsService
import me.imsahil.mapsroutedrawexample.data.repository.MapsRepositoryImpl
import me.imsahil.mapsroutedrawexample.domain.repository.MapsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MAPS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesMapDirectionsService(
        retrofit: Retrofit
    ): MapDirectionsService = retrofit.create(MapDirectionsService::class.java)

    @Provides
    @Singleton
    fun providesMapsRepository(
        mapDirectionsService: MapDirectionsService,
    ): MapsRepository = MapsRepositoryImpl(mapDirectionsService)

}