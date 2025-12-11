package com.example.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.model.Landmark
class LandmarkViewModel : ViewModel() {

    // MutableLiveData to hold the list of landmarks (private, so only ViewModel can modify)
    private val _landmarks = MutableLiveData<List<Landmark>>()

    // Public LiveData for fragments/activities to observe (read-only)
    val landmarks: LiveData<List<Landmark>> get() = _landmarks

    // Function to update landmarks
    fun setLandmarks(newLandmarks: List<Landmark>) {
        _landmarks.value = newLandmarks
    }

    // Function to clear all landmarks
    fun clearLandmarks() {
        _landmarks.value = emptyList()
    }

    // Optional: Get specific landmark by ID
    fun getLandmarkById(id: Int): Landmark? {
        return _landmarks.value?.find { it.id == id }
    }
}