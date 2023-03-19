package me.imsahil.mapsroutedrawexample.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import dagger.hilt.android.AndroidEntryPoint
import me.imsahil.mapsroutedrawexample.presentation.ui.MainScreenEvent
import me.imsahil.mapsroutedrawexample.presentation.ui.theme.MapsRouteDrawExampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MapsRouteDrawExampleTheme {

                val viewModel by viewModels<MainViewModel>()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val context = LocalContext.current
                    val uiState by viewModel.mainUiState.collectAsState()

                    uiState.uiMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.onEvent(MainScreenEvent.UiMessageDisplayed)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {

                        val singapore = LatLng(1.35, 103.87)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(singapore, 15f)
                        }



                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                        ) {
                            when (uiState.currentStep) {
                                CurrentStep.SetOriginCoordinates, CurrentStep.SetDestinationCoordinates -> {
                                    Marker(state = MarkerState(position = cameraPositionState.position.target))
                                }
                                else -> {}
                            }

                            uiState.originLatLng?.let {
                                Marker(
                                    title = "Origin",
                                    state = MarkerState(position = it)
                                )
                            }

                            uiState.destinationLatLng?.let {
                                Marker(
                                    title = "Destination",
                                    state = MarkerState(position = it)
                                )
                            }

                            if (uiState.routeCoordinates.isNotEmpty()) {
                                uiState.routeCoordinates.forEach {
                                    Polyline(points = it, color = Color.Red)
                                }
                            }

                        }

                        Button(modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(50.dp),
                            onClick = {

                                when (uiState.currentStep) {
                                    CurrentStep.SetOriginCoordinates, CurrentStep.SetDestinationCoordinates -> {
                                        viewModel.onEvent(
                                            MainScreenEvent.SetCoordinates(
                                                coordinates = cameraPositionState.position.target
                                            )
                                        )
                                    }
                                    CurrentStep.DrawPath -> viewModel.onEvent(MainScreenEvent.DrawRoute)
                                    null -> viewModel.onEvent(MainScreenEvent.ClearMap)
                                }

                            }) {
                            Text(
                                text = when (uiState.currentStep) {
                                    CurrentStep.SetOriginCoordinates -> "Set Origin"
                                    CurrentStep.SetDestinationCoordinates -> "Set Destination"
                                    CurrentStep.DrawPath -> "Draw Path"
                                    null -> "Clear Map"
                                }
                            )
                        }


                        Text(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(10.dp).background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(50)
                                ).padding(10.dp),
                            text = when (uiState.currentStep) {
                                CurrentStep.SetOriginCoordinates -> "Set Origin by dragging map"
                                CurrentStep.SetDestinationCoordinates -> "Set Destination By dragging map"
                                CurrentStep.DrawPath -> "Now you are ready to draw path"
                                null -> "Reset map by pressing clear map button"
                            },
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                        )
                    }

                }
            }
        }
    }
}

