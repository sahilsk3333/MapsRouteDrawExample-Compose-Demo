package me.imsahil.mapsroutedrawexample.presentation

import com.google.android.gms.maps.model.LatLng

data class MainUiState(
    val routeCoordinates: List<List<LatLng>> =  emptyList(),
    val originLatLng: LatLng? = null,
    val destinationLatLng: LatLng? = null,
    val uiMessage: String? = null,
    val currentStep: CurrentStep? = CurrentStep.SetOriginCoordinates
)

enum class CurrentStep {
    SetOriginCoordinates,
    SetDestinationCoordinates,
    DrawPath
}
