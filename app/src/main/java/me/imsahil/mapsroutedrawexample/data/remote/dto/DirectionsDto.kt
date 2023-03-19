package me.imsahil.mapsroutedrawexample.data.remote.dto

import com.google.android.gms.maps.model.LatLng

data class DirectionsDto(
    val geocoded_waypoints: List<GeocodedWaypoint>,
    val routes: List<Route>,
    val status: String
){
    data class GeocodedWaypoint(
        val geocoder_status: String,
        val place_id: String,
        val types: List<String>
    )
    data class Route(
        val bounds: Bounds,
        val copyrights: String,
        val legs: List<Leg>,
        val overview_polyline: OverviewPolyline,
        val summary: String,
        val warnings: List<Any>,
        val waypoint_order: List<Any>
    ){
        data class Bounds(
            val northeast: Northeast,
            val southwest: Southwest
        ){

            data class Northeast(
                val lat: Double,
                val lng: Double
            )

            data class Southwest(
                val lat: Double,
                val lng: Double
            )
        }

        data class Leg(
            val distance: Distance,
            val duration: Duration,
            val end_address: String,
            val end_location: EndLocation,
            val start_address: String,
            val start_location: StartLocation,
            val steps: List<Step>,
            val traffic_speed_entry: List<Any>,
            val via_waypoint: List<Any>
        ){
            data class Distance(
                val text: String,
                val value: Int
            )

            data class Duration(
                val text: String,
                val value: Int
            )

            data class EndLocation(
                val lat: Double,
                val lng: Double
            )

            data class StartLocation(
                val lat: Double,
                val lng: Double
            )

            data class Step(
                val distance: Distance,
                val duration: Duration,
                val end_location: EndLocation,
                val html_instructions: String,
                val maneuver: String,
                val polyline: Polyline,
                val start_location: StartLocation,
                val travel_mode: String
            ){
                data class Polyline(
                    val points: String
                ){

                    /* to decode polyline String to list of latLng
                    * so we can draw route using the coordinates */

                    fun decodePolyline(encoded: String): List<LatLng> {
                        val poly = ArrayList<LatLng>()
                        var index = 0
                        val len = encoded.length
                        var lat = 0
                        var lng = 0
                        while (index < len) {
                            var b: Int
                            var shift = 0
                            var result = 0
                            do {
                                b = encoded[index++].code - 63
                                result = result or (b and 0x1f shl shift)
                                shift += 5
                            } while (b >= 0x20)
                            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                            lat += dlat
                            shift = 0
                            result = 0
                            do {
                                b = encoded[index++].code - 63
                                result = result or (b and 0x1f shl shift)
                                shift += 5
                            } while (b >= 0x20)
                            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
                            lng += dlng
                            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
                            poly.add(latLng)
                        }
                        return poly
                    }

                }
            }

        }
        data class OverviewPolyline(
            val points: String
        )
    }
}