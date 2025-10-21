package com.example.drivenextapp.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.data.CarData
import com.example.drivenextapp.data.Result
import com.example.drivenextapp.ui.common.LoaderFragment

class HomepageFragment : Fragment(R.layout.fragment_homepage) {

    private val vm: HomeViewModel by viewModels()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CarAdapter
    private lateinit var etSearch: EditText
    private lateinit var layoutError: View
    private lateinit var tvErrorMessage: TextView

    private var isNavigating = false // флаг, чтобы не менять фрагмент дважды

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация UI
        recycler = view.findViewById(R.id.recyclerCars)
        etSearch = view.findViewById(R.id.etSearch)
        layoutError = view.findViewById(R.id.layoutError)
        tvErrorMessage = view.findViewById(R.id.tvErrorMessage)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = CarAdapter(listOf(), onBook = { onBookClicked(it) }, onDetails = { onDetailsClicked(it) })
        recycler.adapter = adapter

        setupListeners()
        observeViewModel()

        vm.loadCars() // запускаем загрузку
    }

    private fun setupListeners() {
        etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event?.keyCode == KeyEvent.KEYCODE_ENTER)) {
                performSearch(etSearch.text.toString())
                true
            } else false
        }
    }

    private fun observeViewModel() {
        vm.cars.observe(viewLifecycleOwner) { cars ->
            adapter.submitList(cars)
            recycler.visibility = if (cars.isEmpty()) View.GONE else View.VISIBLE
            layoutError.visibility = if (cars.isEmpty() && vm.errorMessage.value == null) View.VISIBLE else View.GONE
            tvErrorMessage.text = if (cars.isEmpty()) "Ничего не найдено" else ""
        }

        vm.isLoading.observe(viewLifecycleOwner) { isLoading ->
            val loader = childFragmentManager.findFragmentById(R.id.loaderContainer)
            if (isLoading && loader == null) {
                childFragmentManager.beginTransaction()
                    .replace(R.id.loaderContainer, LoaderFragment())
                    .commit()
            } else if (!isLoading && loader != null) {
                childFragmentManager.beginTransaction()
                    .remove(loader)
                    .commit()
            }
        }

        vm.errorMessage.observe(viewLifecycleOwner) { error ->
            layoutError.visibility = if (error != null) View.VISIBLE else View.GONE
            tvErrorMessage.text = error ?: ""

            // Показываем Toast при ошибке
            error?.let {
                android.widget.Toast.makeText(requireContext(), "Не удалось загрузить данные. Попробуйте снова.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performSearch(brand: String) {
        if (brand.isBlank() || isNavigating) return

        layoutError.visibility = View.GONE
        recycler.visibility = View.GONE
        isNavigating = true

        vm.searchBrand(brand) { result ->
            isNavigating = false

            when (result) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        layoutError.visibility = View.VISIBLE
                        tvErrorMessage.text = "Ничего не найдено"
                    } else {
                        val args = bundleOf(
                            "brand" to brand,
                            "cars" to ArrayList(result.data)
                        )
                        findNavController().navigate(R.id.action_homepageFragment_to_resultFragment, args)
                    }
                }
                is Result.Error -> {
                    layoutError.visibility = View.VISIBLE
                    tvErrorMessage.text = result.message
                    android.widget.Toast.makeText(requireContext(), "Не удалось выполнить поиск. Попробуйте снова.", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onBookClicked(car: CarData) {
        val args = bundleOf("carId" to car.id)
        findNavController().navigate(R.id.action_homepageFragment_to_checkoutFragment, args)
    }

    private fun onDetailsClicked(car: CarData) {
        val args = bundleOf("carId" to car.id)
        findNavController().navigate(R.id.action_homepageFragment_to_carDetailsFragment, args)
    }
}
