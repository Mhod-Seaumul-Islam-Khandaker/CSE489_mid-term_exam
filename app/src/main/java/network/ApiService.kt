package com.example.test.network

import com.example.test.model.Landmark
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    // GET - Read all landmarks
    @GET("api.php")
    fun getLandmarks(): Call<List<Landmark>>

    // POST - Create new landmark (without image for now)
    @FormUrlEncoded
    @POST("api.php")
    fun createLandmark(
        @Field("title") title: String,
        @Field("lat") latitude: Double,
        @Field("lon") longitude: Double
    ): Call<Map<String, Any>> // Response will be like {"id": 123}

    // PUT - Update existing landmark (without image for now)
    @FormUrlEncoded
    @PUT("api.php")
    fun updateLandmark(
        @Field("id") id: Int,
        @Field("title") title: String,
        @Field("lat") latitude: Double,
        @Field("lon") longitude: Double
    ): Call<Map<String, Any>> // Response will be like {"success": true}

    // DELETE - Remove landmark
    @FormUrlEncoded
    @DELETE("api.php")
    fun deleteLandmark(
        @Field("id") id: Int
    ): Call<Map<String, Any>> // Response will be like {"success": true}

    // NOTE: For image uploads, we would use @Multipart instead of @FormUrlEncoded
    // But we're keeping it simple for now
}