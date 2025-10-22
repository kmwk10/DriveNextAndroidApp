package com.example.drivenextapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.drivenextapp.R
import com.example.drivenextapp.data.CarData

class ResultFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CarAdapter
    private lateinit var btnBack: View
    private var brand: String? = null

    private var cars: List<CarData>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brand = arguments?.getString("brand")
        cars = arguments?.getParcelableArrayList<CarData>("cars")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_result, container, false)

        recycler = root.findViewById(R.id.recyclerResults)
        btnBack = root.findViewById(R.id.btnBack)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = CarAdapter(listOf(), onBook = { car -> onBookClicked(car) }, onDetails = { car -> onDetailsClicked(car) })
        recycler.adapter = adapter

        btnBack.setOnClickListener { findNavController().popBackStack() }

        // Отображаем карточки, если список пришёл через аргументы
        cars?.let {
            adapter.submitList(it)
        }

        return root
    }

    private fun onBookClicked(car: CarData) {
        val args = bundleOf("carId" to car.id)
        findNavController().navigate(R.id.action_resultFragment_to_checkoutFragment, args)
    }

    private fun onDetailsClicked(car: CarData) {
        val args = bundleOf("carId" to car.id)
        findNavController().navigate(R.id.action_resultFragment_to_carDetailsFragment, args)
    }
}

