package com.example.drivenextapp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.util.ConnectivityLiveData

class NoConnectionFragment : Fragment() {

    private lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_no_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityLiveData = ConnectivityLiveData(requireContext())

        val retryButton = view.findViewById<Button>(R.id.btnRetry)
        retryButton.setOnClickListener {
            val isConnected = connectivityLiveData.value ?: false
            if (isConnected) {
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Сеть недоступна", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
