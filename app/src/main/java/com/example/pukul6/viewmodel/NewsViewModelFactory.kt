package com.example.pukul6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pukul6.data.api.APIConfig
import com.example.pukul6.data.pagging.NewsRepository

class NewsViewModelFactory(
    private val token: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            val apiService = APIConfig.getApiService1()
            val repository = NewsRepository(apiService, token)
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
