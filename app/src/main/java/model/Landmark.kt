package com.example.test.model

import com.google.gson.annotations.SerializedName

data class Landmark(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("lat")
    val latitude: Double,

    @SerializedName("lon")
    val longitude: Double,

    @SerializedName("image")
    val imagePath: String
) {
    // Helper method to get full image URL
    fun getFullImageUrl(): String {
        return "https://labs.anontech.info/cse489/t3/$imagePath"
    }

    // Format coordinates for display
    fun getFormattedCoordinates(): String {
        return "Lat: $latitude, Lon: $longitude"
    }
}