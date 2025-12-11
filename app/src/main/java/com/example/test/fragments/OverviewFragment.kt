package com.example.test.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.test.R
import com.example.test.model.Landmark
import com.example.test.viewmodel.LandmarkViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class OverviewFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var viewModel: LandmarkViewModel
    private val markers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity()).get(LandmarkViewModel::class.java)

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

        // Observe landmarks from ViewModel
        viewModel.landmarks.observe(viewLifecycleOwner, Observer { landmarks ->
            updateMarkersOnMap(landmarks)
        })

        // Remove the sample marker call since we're using real data from ViewModel
        // addMarker(bangladesh, "Dhaka, Bangladesh", "Capital of Bangladesh")
    }

    private fun updateMarkersOnMap(landmarks: List<Landmark>) {
        // Clear existing markers
        markers.forEach { marker ->
            mapView.overlays.remove(marker)
        }
        markers.clear()

        // Add new markers for each landmark
        landmarks.forEach { landmark ->
            val geoPoint = GeoPoint(landmark.latitude, landmark.longitude)
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.title = landmark.title
            marker.snippet = "ID: ${landmark.id}\n${landmark.getFormattedCoordinates()}"
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            // Set click listener
            marker.setOnMarkerClickListener { clickedMarker, mapView ->
                showBottomSheet(landmark)
                true
            }

            mapView.overlays.add(marker)
            markers.add(marker)
        }

        // Refresh the map
        mapView.invalidate()
    }

    private fun showBottomSheet(landmark: Landmark) {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_landmark, null)

        // Set landmark details
        view.findViewById<TextView>(R.id.bottomSheetTitle).text = landmark.title
        view.findViewById<TextView>(R.id.bottomSheetCoordinates).text = landmark.getFormattedCoordinates()
        view.findViewById<TextView>(R.id.bottomSheetId).text = "ID: ${landmark.id}"

        // Set close button listener
        view.findViewById<Button>(R.id.closeButton).setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
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