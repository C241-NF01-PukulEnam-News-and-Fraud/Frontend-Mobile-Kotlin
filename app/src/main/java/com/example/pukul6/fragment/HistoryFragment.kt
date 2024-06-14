package com.example.pukul6.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pukul6.R
import com.example.pukul6.adapter.FraudAdaptor
import com.example.pukul6.data.database.ClassificationResult
import com.example.pukul6.viewmodel.FraudViewModel

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FraudAdaptor
    private lateinit var fraudViewModel: FraudViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fraudViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(FraudViewModel::class.java)

        fraudViewModel.getAllClassificationResults().observe(viewLifecycleOwner, Observer { results ->
            adapter = FraudAdaptor(results.toMutableList(), ::onDeleteClick)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        })
    }

    private fun onDeleteClick(classificationResult: ClassificationResult) {
        fraudViewModel.delete(classificationResult)
    }
}
