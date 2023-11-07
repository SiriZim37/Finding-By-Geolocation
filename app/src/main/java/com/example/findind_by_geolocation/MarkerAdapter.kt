package com.example.findind_by_geolocation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.findind_by_geolocation.place.NaturPlace
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.marker_adapter.view.*

class MarkerAdapter (private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        // 1. Get tag
        val place = marker.tag as? NaturPlace ?: return null

        // 2. Inflate view and set title, address and rating
        val view = LayoutInflater.from(context).inflate(R.layout.marker_adapter, null)
        view.text_view_title.text = place.name
        view.text_view_address.text = place.address
        view.text_view_rating.text = "Rating: %.2f".format(place.rating)

        return view
    }

    override fun getInfoWindow(marker: Marker): View? {
        // Return null to indicate that the default window (white bubble) should be used
        return null
    }
}