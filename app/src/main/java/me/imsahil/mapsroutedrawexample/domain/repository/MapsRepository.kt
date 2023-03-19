package me.imsahil.mapsroutedrawexample.domain.repository

import com.google.android.gms.maps.model.LatLng
import me.imsahil.mapsroutedrawexample.domain.model.Route
import me.imsahil.mapsroutedrawexample.utils.Resource

interface MapsRepository {

    suspend fun getDirections(
        origin: LatLng,
        destination: LatLng
    ): Resource<Route>

}