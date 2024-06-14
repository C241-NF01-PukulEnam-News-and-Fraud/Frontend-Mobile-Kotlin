package com.example.pukul6.data.api

import com.example.pukul6.data.response.Post
import com.example.pukul6.data.response.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
        @GET("posts")
        suspend fun getPosts(
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Header("Authorization") token: String
        ): List<Post>
}
interface ApiService2 {
    @POST("classification")
    suspend fun classify(
        @Body requestBody: RequestBody,
        @Header("Authorization") token: String

    ): Response
}
