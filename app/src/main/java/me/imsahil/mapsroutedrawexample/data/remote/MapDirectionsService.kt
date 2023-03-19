package me.imsahil.mapsroutedrawexample.data.remote

import me.imsahil.mapsroutedrawexample.BuildConfig
import me.imsahil.mapsroutedrawexample.data.remote.dto.DirectionsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MapDirectionsService {

    @GET("directions/json")
    suspend fun getDirections(
        @Query("origin") originLatLng: String,
        @Query("destination") destinationLatLang: String,
        @Query("key") apiKey: String = BuildConfig.MAPS_API_KEY
    ): Response<DirectionsDto>

}