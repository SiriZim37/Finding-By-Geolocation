package com.example.findind_by_geolocation.place

import com.google.android.gms.maps.model.LatLng

data class NaturPlaceResponse(
    val geometry: Geometry,
    val name: String,
    val vicinity: String,
    val rating: Float
) {

    data class Geometry(
        val location: GeometryLocation
    )

    data class GeometryLocation(
        val lat: Double,
        val lng: Double
    )
}

fun NaturPlaceResponse.toPlace(): NaturPlace = NaturPlace(
    name = name,
    latLng = LatLng(geometry.location.lat, geometry.location.lng),
    address = vicinity,
    rating = rating
)
