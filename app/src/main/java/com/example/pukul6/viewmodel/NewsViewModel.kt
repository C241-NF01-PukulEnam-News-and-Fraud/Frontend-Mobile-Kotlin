package com.example.pukul6.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pukul6.data.pagging.NewsRepository
import com.example.pukul6.data.response.Post

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    val story: LiveData<PagingData<Post>> =
        newsRepository.getNews().cachedIn(viewModelScope).also {
        }
}
