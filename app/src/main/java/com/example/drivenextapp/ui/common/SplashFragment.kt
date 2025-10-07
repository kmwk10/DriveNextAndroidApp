package com.example.drivenextapp.ui.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    // Handler и runnable, чтобы можно было отменить отложенный переход
    private val handler = Handler(Looper.getMainLooper())
    private val splashDelay = 2000L
    private var isScheduled = false
    private var hasNavigated = false

    private val navigateRunnable = Runnable {
        // Выполняем навигацию только если ещё не навигировали
        if (!hasNavigated) {
            navigateNext()
        }
    }

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
            if (connected && !hasNavigated && !isScheduled) {
                // запланировать переход
                isScheduled = true
                handler.postDelayed(navigateRunnable, splashDelay)
            } else if (!connected) {
                // если сеть пропала до навигации — отменяем запланированный переход
                if (isScheduled) {
                    handler.removeCallbacks(navigateRunnable)
                    isScheduled = false
                }
            }
            // При наличии сети MainActivity / NavHost продолжит работу
        }
    }

    private fun navigateNext() {
        val navController = findNavController()
        val prefs = PrefsManager(requireContext())

        // Пометка, что уже навигировали — чтобы избежать двойного вызова
        hasNavigated = true
        isScheduled = false
        // Безопасная проверка текущей destination перед навигацией
        val currentId = navController.currentDestination?.id
        if (currentId != R.id.splashFragment) {
            Log.w("SplashFragment", "Current destination is not splashFragment (id=$currentId). Skip navigate.")
            return
        }

        try {
            when {
                prefs.isFirstLaunch() -> navController.navigate(R.id.action_splashFragment_to_onboardingFragment1)
                !prefs.isAccessTokenValid() -> navController.navigate(R.id.action_splashFragment_to_gettingStartedFragment)
                else -> navController.navigate(R.id.action_splashFragment_to_homepageFragment)
            }
        } catch (e: IllegalArgumentException) {
            Log.e("SplashFragment", "Navigation failed: ${e.message}", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Обязательно удаляем отложенные callbacks, чтобы не вызвать навигацию уже после уничтожения view
        handler.removeCallbacks(navigateRunnable)
        isScheduled = false
    }
}
