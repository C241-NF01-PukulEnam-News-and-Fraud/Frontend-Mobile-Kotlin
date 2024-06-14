package com.example.pukul6.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pukul6.R
import com.example.pukul6.adapter.HorizontalRecyclerView
import com.example.pukul6.adapter.NewsAdaptor
import com.example.pukul6.viewmodel.NewsViewModel
import com.example.pukul6.viewmodel.NewsViewModelFactory
import com.example.submissionandroidintermediate.adapter.LoadingStateAdapter

class NewsFragment : Fragment() {
    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var adapterNews: NewsAdaptor
    private lateinit var viewModel: NewsViewModel

    private lateinit var adapterHorizontal: HorizontalRecyclerView
    private lateinit var recyclerViewHorizontal: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = "cHVrdWxlbmFtOnB1a3VsZW5hbVBBUyE" // Replace with your actual token
        val factory = NewsViewModelFactory(token)
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewNews = view.findViewById(R.id.listNews)
        adapterNews = NewsAdaptor()

        recyclerViewNews.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewNews.adapter = adapterNews.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { adapterNews.retry() },
            footer = LoadingStateAdapter { adapterNews.retry() }
        )

        viewModel.story.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData == null) {
                Log.d("NewsFragment", "No data available")
            } else {
                Log.d("NewsFragment", "Submitting data to adapter")
                adapterNews.submitData(lifecycle, pagingData)
            }
        }

        // Initialize and set up the horizontal RecyclerView
        recyclerViewHorizontal = view.findViewById(R.id.recyclerView) // Replace with the correct ID
        adapterHorizontal = HorizontalRecyclerView()
        recyclerViewHorizontal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewHorizontal.adapter = adapterHorizontal
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewsFragment()
    }
}