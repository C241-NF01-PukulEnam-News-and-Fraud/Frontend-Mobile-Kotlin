package com.example.pukul6.data.pagging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.pukul6.data.api.ApiService
import com.example.pukul6.data.response.Post

class NewsRepository(private val apiService: ApiService, private val token: String) {
    fun getNews(): LiveData<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(apiService, token) }
        ).liveData
    }
}
