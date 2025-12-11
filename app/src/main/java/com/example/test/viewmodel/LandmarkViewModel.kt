package com.example.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.model.Landmark

class LandmarkViewModel : ViewModel() {

    // MutableLiveData to hold the list of landmarks
    private val _landmarks = MutableLiveData<List<Landmark>>()
    val landmarks: LiveData<List<Landmark>> get() = _landmarks

    // For error/success messages
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    // For loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Function to update landmarks (for GET operations)
    fun setLandmarks(newLandmarks: List<Landmark>) {
        _landmarks.value = newLandmarks
    }

    // Function to add a new landmark locally (before API call)
    fun addLandmarkLocally(landmark: Landmark) {
        val currentList = _landmarks.value?.toMutableList() ?: mutableListOf()
        currentList.add(landmark)
        _landmarks.value = currentList
        _message.value = "Landmark added locally"
    }

    // Function to update a landmark locally (before API call)
    fun updateLandmarkLocally(updatedLandmark: Landmark) {
        val currentList = _landmarks.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == updatedLandmark.id }
        if (index != -1) {
            currentList[index] = updatedLandmark
            _landmarks.value = currentList
            _message.value = "Landmark updated locally"
        }
    }

    // Function to delete a landmark locally (before API call)
    fun deleteLandmarkLocally(landmarkId: Int) {
        val currentList = _landmarks.value?.toMutableList() ?: mutableListOf()
        currentList.removeAll { it.id == landmarkId }
        _landmarks.value = currentList
        _message.value = "Landmark deleted locally"
    }

    // Function to clear all landmarks
    fun clearLandmarks() {
        _landmarks.value = emptyList()
    }

    // Function to get specific landmark by ID
    fun getLandmarkById(id: Int): Landmark? {
        return _landmarks.value?.find { it.id == id }
    }

    // Set loading state
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    // Set message
    fun setMessage(msg: String) {
        _message.value = msg
    }

    // Clear message
    fun clearMessage() {
        _message.value = ""
    }
}