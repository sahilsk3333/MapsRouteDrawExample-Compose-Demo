package me.imsahil.mapsroutedrawexample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.imsahil.mapsroutedrawexample.domain.repository.MapsRepository
import me.imsahil.mapsroutedrawexample.presentation.ui.MainScreenEvent
import me.imsahil.mapsroutedrawexample.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mapsRepository: MapsRepository
) : ViewModel() {

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.ClearMap -> {
                _mainUiState.update {
                    it.copy(
                        routeCoordinates = emptyList(),
                        originLatLng = null,
                        destinationLatLng = null,
                        currentStep = CurrentStep.SetOriginCoordinates
                    )
                }
            }
            MainScreenEvent.DrawRoute -> {
                getRoutePath()
            }
            MainScreenEvent.UiMessageDisplayed -> {
                _mainUiState.update {
                    it.copy(
                        uiMessage = null
                    )
                }
            }
            is MainScreenEvent.SetCoordinates -> {
                when (mainUiState.value.currentStep) {
                    CurrentStep.SetOriginCoordinates -> {
                        _mainUiState.update {
                            it.copy(
                                originLatLng = event.coordinates,
                                currentStep = CurrentStep.SetDestinationCoordinates
                            )
                        }
                    }
                    CurrentStep.SetDestinationCoordinates -> {
                        _mainUiState.update {
                            it.copy(
                                destinationLatLng = event.coordinates,
                                currentStep = CurrentStep.DrawPath
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getRoutePath() {

        if (mainUiState.value.originLatLng == null) {
            _mainUiState.update {
                it.copy(
                    uiMessage = "Please set origin marker"
                )
            }
            return
        }


        if (mainUiState.value.destinationLatLng == null) {
            _mainUiState.update {
                it.copy(
                    uiMessage = "Please set destination marker"
                )
            }
            return
        }

        viewModelScope.launch {

            val pathResult = mapsRepository.getDirections(
                origin = mainUiState.value.originLatLng!!,
                destination = mainUiState.value.destinationLatLng!!
            )

            when (pathResult) {
                is Resource.Error -> {
                    _mainUiState.update { it.copy(uiMessage = pathResult.message) }
                }
                is Resource.Success -> {
                    pathResult.data?.let { route ->
                        _mainUiState.update {
                            it.copy(
                                routeCoordinates = route.routePoints,
                                currentStep = null
                            )
                        }
                    }

                }
            }

        }
    }

}