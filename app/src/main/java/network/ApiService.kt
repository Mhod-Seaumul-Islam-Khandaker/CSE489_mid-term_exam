package com.example.test.network

import com.example.test.model.Landmark
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api.php")
    fun getLandmarks(): Call<List<Landmark>>
}