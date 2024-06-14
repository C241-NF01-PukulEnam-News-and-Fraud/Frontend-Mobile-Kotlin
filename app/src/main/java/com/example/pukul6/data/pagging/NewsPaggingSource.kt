package com.example.pukul6.data.pagging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pukul6.data.api.ApiService
import com.example.pukul6.data.response.Post
import retrofit2.HttpException

class NewsPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getPosts(currentPage, params.loadSize, "Basic $token")
            val data = response

            Log.d("StoryPagingSource", "Loaded page: $currentPage, data size: ${data.size}")

            LoadResult.Page(
                data = data,
                prevKey = if(currentPage == 1 ) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }
}
