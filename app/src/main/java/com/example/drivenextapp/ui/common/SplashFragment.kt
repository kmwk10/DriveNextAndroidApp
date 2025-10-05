package com.example.drivenextapp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.drivenextapp.R
import com.example.drivenextapp.util.ConnectivityLiveData
import com.example.drivenextapp.util.PrefsManager

class SplashFragment : Fragment() {

    private lateinit var connectivityLiveData: ConnectivityLiveData
    private var hasNavigated = false  // Чтобы не выполнить переход дважды

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        connectivityLiveData = ConnectivityLiveData(requireContext())
        connectivityLiveData.observe(viewLifecycleOwner) { connected ->
            if (connected && !hasNavigated) {
                navigateNext()
            }
            // Если сети нет — ничего не делаем, MainActivity покажет NoConnectionFragment
        }
    }

    private fun navigateNext() {
        val navController = findNavController()
        val prefs = PrefsManager(requireContext())

        hasNavigated = true

        when {
            prefs.isFirstLaunch() -> {
                navController.navigate(R.id.action_splashFragment_to_onboardingFragment1)
            }
            !prefs.isAccessTokenValid() -> {
                navController.navigate(R.id.action_splashFragment_to_gettingStartedFragment)
            }
            else -> {
                navController.navigate(R.id.action_splashFragment_to_homepageFragment)
            }
        }
    }
}
