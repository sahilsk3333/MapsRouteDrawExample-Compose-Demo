package me.imsahil.mapsroutedrawexample.data.repository

import com.google.android.gms.maps.model.LatLng
import me.imsahil.mapsroutedrawexample.data.remote.MapDirectionsService
import me.imsahil.mapsroutedrawexample.domain.model.Route
import me.imsahil.mapsroutedrawexample.domain.repository.MapsRepository
import me.imsahil.mapsroutedrawexample.utils.Resource

class MapsRepositoryImpl(
    private val directionsService: MapDirectionsService
) : MapsRepository {
    override suspend fun getDirections(origin: LatLng, destination: LatLng): Resource<Route> {
       return try {

           val response = directionsService.getDirections(
               originLatLng = "${origin.latitude},${origin.longitude}",
               destinationLatLang = "${destination.latitude},${destination.longitude}",
           )


           if (response.isSuccessful && response.body() != null){

               val polyLinePoints = try {
                   response.body()!!.routes[0].legs[0].steps.map { step ->
                       step.polyline.decodePolyline(step.polyline.points)
                   }
               }catch (e: Exception){ emptyList() }
               Resource.Success(data = Route(routePoints = polyLinePoints))
           }else{
               Resource.Error(response.message())
           }

       }catch (e:Exception){
           Resource.Error("Something went wrong")
       }
    }
}