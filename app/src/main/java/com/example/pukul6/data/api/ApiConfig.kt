package com.example.pukul6.data.api

import com.example.pukul6.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIConfig {
    companion object {
        private const val BASE_URL_1 = "https://pukulenam.id/wp-json/wp/v2/"
        private const val BASE_URL_2 = "https://pukul-enam-server-model-api-73t3gncdha-et.a.run.app/"

        private val loggingInterceptor =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        private val retrofit1 = Retrofit.Builder()
            .baseUrl(BASE_URL_1)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        private val retrofit2 = Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        fun getApiService1(): ApiService {
            return retrofit1.create(ApiService::class.java)
        }

        fun getApiService2(): ApiService2 {
            return retrofit2.create(ApiService2::class.java)
        }
    }
}