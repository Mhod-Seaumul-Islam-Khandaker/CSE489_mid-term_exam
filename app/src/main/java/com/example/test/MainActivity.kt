package com.example.test

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.adapter.LandmarkAdapter
import com.example.test.fragments.OverviewFragment
import com.example.test.model.Landmark
import com.example.test.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyStateText: TextView
    private lateinit var adapter: LandmarkAdapter
    private lateinit var currentContentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recyclerView = findViewById(R.id.landmarksRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        emptyStateText = findViewById(R.id.emptyStateText)
        currentContentView = findViewById(R.id.currentContentView)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = LandmarkAdapter()
        recyclerView.adapter = adapter

        // Setup bottom navigation
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.overview -> {
                    showFragment(OverviewFragment(), "overview")
                    true
                }
                R.id.records -> {
                    showRecordsView()
                    true
                }
                R.id.new_entry -> {
                    // For now, just show records view
                    showRecordsView()
                    true
                }
                else -> false
            }
        }

        // Start with Records view (your current list)
        showRecordsView()

        // Fetch landmarks from API
        fetchLandmarks()
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        // Hide the current content view (RecyclerView)
        currentContentView.visibility = View.GONE

        // Show the fragment container
        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)
        fragmentContainer.visibility = View.VISIBLE

        // Replace with new fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .commit()
    }

    private fun showRecordsView() {
        // Hide fragment container (map)
        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)
        fragmentContainer.visibility = View.GONE

        // Show the RecyclerView and its parent
        currentContentView.visibility = View.VISIBLE

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