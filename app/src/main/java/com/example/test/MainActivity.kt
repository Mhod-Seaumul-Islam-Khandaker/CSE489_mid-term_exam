package com.example.test

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.LandmarkAdapter
import com.example.test.model.Landmark  // ← ADD THIS LINE
import com.example.test.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateText: TextView
    private lateinit var adapter: LandmarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.landmarksRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        emptyStateText = findViewById(R.id.emptyStateText)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LandmarkAdapter()
        recyclerView.adapter = adapter

        // Setup bottom navigation (for future tabs)
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.overview -> {
                    // Already on overview, refresh data
                    fetchLandmarks()
                    true
                }
                R.id.records -> {
                    // Will implement later for Records tab
                    true
                }
                R.id.new_entry -> {
                    // Will implement later for New Entry tab
                    true
                }
                else -> false
            }
        }

        // Fetch landmarks from API
        fetchLandmarks()
    }

    private fun fetchLandmarks() {
        showLoading(true)

        RetrofitClient.apiService.getLandmarks().enqueue(object : Callback<List<Landmark>> {
            override fun onResponse(call: Call<List<Landmark>>, response: Response<List<Landmark>>) {
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    val landmarks = response.body()!!

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