package com.example.findind_by_geolocation.place

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.findind_by_geolocation.R
import com.example.findind_by_geolocation.manager.BitmapHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

/**
 * A custom cluster renderer for Place objects.
 */
class NaturPlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<NaturPlace>
) : DefaultClusterRenderer<NaturPlace>(context, map, clusterManager) {

    /**
     * The icon to use for each cluster item
     */
    private val naturIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(context, R.color.green
        )
        BitmapHelper.vectorToBitmap(
            context,
            R.drawable.forest,
            color
        )
    }

    /**
     * Method called before the cluster item (i.e. the marker) is rendered. This is where marker
     * options should be set
     */
    override fun onBeforeClusterItemRendered(item: NaturPlace, markerOptions: MarkerOptions) {
        markerOptions.title(item.name)
            .position(item.latLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.forest))
    }

    /**
     * Method called right after the cluster item (i.e. the marker) is rendered. This is where
     * properties for the Marker object should be set.
     */
    override fun onClusterItemRendered(clusterItem: NaturPlace, marker: Marker) {
        marker.tag = clusterItem
    }
}