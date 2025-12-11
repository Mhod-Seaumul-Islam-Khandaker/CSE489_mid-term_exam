package com.example.test

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.LandmarkAdapter
import com.example.test.dialogs.LandmarkDialog
import com.example.test.fragments.OverviewFragment
import com.example.test.model.Landmark
import com.example.test.network.RetrofitClient
import com.example.test.viewmodel.LandmarkViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), LandmarkDialog.LandmarkDialogListener {

    private lateinit var viewModel: LandmarkViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateText: TextView
    private lateinit var adapter: LandmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(LandmarkViewModel::class.java)

        // Initialize views
        recyclerView = findViewById(R.id.landmarksRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        emptyStateText = findViewById(R.id.emptyStateText)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LandmarkAdapter()
        recyclerView.adapter = adapter

        // Observe ViewModel for landmarks
        viewModel.landmarks.observe(this, Observer { landmarks ->
            adapter.setLandmarks(landmarks)
        })

        // Observe loading state
        viewModel.isLoading.observe(this, Observer { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                recyclerView.visibility = View.GONE
                emptyStateText.visibility = View.GONE
            }
        })

        // Observe messages
        viewModel.message.observe(this, Observer { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessage()
            }
        })

        // Setup bottom navigation
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.overview -> {
                    showMapView()
                    true
                }
                R.id.records -> {
                    showListView()
                    true
                }
                R.id.new_entry -> {
                    // Show dialog to add new landmark
                    LandmarkDialog.showAddDialog(this, this)
                    // Stay in current view (don't switch)
                    true
                }
                else -> false
            }
        }

        // Start with List view
        showListView()
        fetchLandmarks()
    }

    private fun showMapView() {
        // Hide the list view
        findViewById<View>(R.id.currentContentView).visibility = View.GONE

        // Show and setup map fragment
        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)
        fragmentContainer.visibility = View.VISIBLE

        // Add fragment if not already added
        val existingFragment = supportFragmentManager.findFragmentByTag("overview")
        if (existingFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, OverviewFragment(), "overview")
                .commit()
        }
    }

    private fun showListView() {
        // Hide the map view
        findViewById<View>(R.id.fragmentContainer).visibility = View.GONE

        // Show the list view
        findViewById<View>(R.id.currentContentView).visibility = View.VISIBLE

        // Refresh data
        fetchLandmarks()
    }

    private fun fetchLandmarks() {
        viewModel.setLoading(true)

        RetrofitClient.apiService.getLandmarks().enqueue(object : Callback<List<Landmark>> {
            override fun onResponse(call: Call<List<Landmark>>, response: Response<List<Landmark>>) {
                viewModel.setLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val landmarks = response.body()!!

                    // Store landmarks in ViewModel
                    viewModel.setLandmarks(landmarks)

                    if (landmarks.isEmpty()) {
                        showEmptyState(true, "No landmarks found")
                    } else {
                        showEmptyState(false, "")
                    }
                } else {
                    showEmptyState(true, "Failed to load data")
                }
            }

            override fun onFailure(call: Call<List<Landmark>>, t: Throwable) {
                viewModel.setLoading(false)
                showEmptyState(true, "Network error: ${t.message}")
            }
        })
    }

    // Implement LandmarkDialogListener
    override fun onLandmarkSaved(landmark: Landmark, isEditMode: Boolean) {
        if (isEditMode) {
            updateLandmarkOnServer(landmark)
        } else {
            createLandmarkOnServer(landmark)
        }
    }

    private fun createLandmarkOnServer(landmark: Landmark) {
        viewModel.setLoading(true)

        RetrofitClient.apiService.createLandmark(
            title = landmark.title,
            latitude = landmark.latitude,
            longitude = landmark.longitude
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                viewModel.setLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    // Get the new ID from server response
                    val newId = (result["id"] as Double).toInt()

                    // Update landmark with real ID from server
                    val updatedLandmark = landmark.copy(id = newId)
                    viewModel.addLandmarkLocally(updatedLandmark)

                    Toast.makeText(this@MainActivity,
                        "Landmark created successfully! ID: $newId",
                        Toast.LENGTH_SHORT).show()

                    // Refresh data to get complete list from server
                    fetchLandmarks()

                } else {
                    Toast.makeText(this@MainActivity,
                        "Failed to create landmark",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                viewModel.setLoading(false)
                Toast.makeText(this@MainActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateLandmarkOnServer(landmark: Landmark) {
        viewModel.setLoading(true)

        RetrofitClient.apiService.updateLandmark(
            id = landmark.id,
            title = landmark.title,
            latitude = landmark.latitude,
            longitude = landmark.longitude
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                viewModel.setLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    viewModel.updateLandmarkLocally(landmark)

                    Toast.makeText(this@MainActivity,
                        "Landmark updated successfully!",
                        Toast.LENGTH_SHORT).show()

                    // Refresh data
                    fetchLandmarks()

                } else {
                    Toast.makeText(this@MainActivity,
                        "Failed to update landmark",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                viewModel.setLoading(false)
                Toast.makeText(this@MainActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEmptyState(show: Boolean, message: String) {
        emptyStateText.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        emptyStateText.text = message
    }
}
