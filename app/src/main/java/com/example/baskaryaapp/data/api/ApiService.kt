package com.example.baskaryaapp.data.api

import com.example.baskaryaapp.data.response.ArticlesResponse
import com.example.baskaryaapp.data.response.BatikResponse
import com.example.baskaryaapp.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//    @Multipart
//    @POST("/")
//    suspend fun recommendation(
//        @Part file: MultipartBody.Part
//    ): Response<CameraResponse>

    @GET("/api/articles")
    suspend fun article(
    ): ArticlesResponse

    @GET("/api/batik")
    suspend fun batik(
    ): BatikResponse

    @GET("api/search")
    fun search(
        @Query("batik") query: String,
    ): Call<SearchResponse>

}