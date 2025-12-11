package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class OverviewFragment : Fragment() {

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize map
        mapView = view.findViewById(R.id.mapView)

        // Configure OpenStreetMap
        Configuration.getInstance().userAgentValue = requireContext().packageName

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Center on Bangladesh (23.6850°N, 90.3563°E)
        val bangladesh = GeoPoint(23.6850, 90.3563)
        mapView.controller.setZoom(7.0)  // Zoom level 7
        mapView.controller.setCenter(bangladesh)

        // Add a sample marker (you'll replace this with your API data)
        addMarker(bangladesh, "Dhaka, Bangladesh", "Capital of Bangladesh")
    }

    private fun addMarker(point: GeoPoint, title: String, snippet: String = "") {
        val marker = Marker(mapView)
        marker.position = point
        marker.title = title
        marker.snippet = snippet
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        // Optional: Set custom marker icon
        // marker.icon = resources.getDrawable(R.drawable.ic_marker, null)

        marker.setOnMarkerClickListener { marker, mapView ->
            // Show bottom sheet with landmark details (to be implemented)
            true
        }

        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}