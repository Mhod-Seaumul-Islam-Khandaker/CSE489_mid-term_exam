package com.example.test

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.LandmarkAdapter
import com.example.test.fragments.OverviewFragment
import com.example.test.model.Landmark
import com.example.test.network.RetrofitClient
import com.example.test.viewmodel.LandmarkViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

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
                    // For now, just show list view
                    showListView()
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
        showLoading(true)

        RetrofitClient.apiService.getLandmarks().enqueue(object : Callback<List<Landmark>> {
            override fun onResponse(call: Call<List<Landmark>>, response: Response<List<Landmark>>) {
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val landmarks = response.body()!!

                    // Store landmarks in ViewModel (NEW LINE ADDED)
                    viewModel.setLandmarks(landmarks)

                    if (landmarks.isEmpty()) {
                        showEmptyState(true, "No landmarks found")
                    } else {
                        showEmptyState(false, "")
                        adapter.setLandmarks(landmarks)
                    }
                } else {
                    showEmptyState(true, "Failed to load data")
                }
            }

            override fun onFailure(call: Call<List<Landmark>>, t: Throwable) {
                showLoading(false)
                showEmptyState(true, "Network error: ${t.message}")
            }
        })
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            recyclerView.visibility = View.GONE
            emptyStateText.visibility = View.GONE
        }
    }

    private fun showEmptyState(show: Boolean, message: String) {
        emptyStateText.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        emptyStateText.text = message
    }
}
