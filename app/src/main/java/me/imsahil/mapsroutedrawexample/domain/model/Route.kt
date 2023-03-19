package me.imsahil.mapsroutedrawexample.domain.model

import com.google.android.gms.maps.model.LatLng

data class Route(
    val routePoints: List<List<LatLng>>
)
