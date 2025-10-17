package com.example.drivenextapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.drivenextapp.util.ConnectivityLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityLiveData: ConnectivityLiveData
    private var isNavControllerReady = false

    // Кешируем NavController и BottomNavigationView
    private var navController: NavController? = null
    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализируем NavController и BottomNavigationView (если есть в layout)
        initNav()

        // Инициализируем наблюдение за сетью
        connectivityLiveData = ConnectivityLiveData(this)
        connectivityLiveData.observe(this) { connected ->
            handleNetworkChange(connected)
        }
    }

    private fun initNav() {
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            navController = navHostFragment?.navController

            val controller = navController ?: return

            bottomNav = findViewById(R.id.bottom_nav)
            bottomNav?.let { bn ->
                NavigationUI.setupWithNavController(bn, controller)
            }
            bottomNav?.itemIconTintList = null
            bottomNav?.itemBackground = null



            // Слушаем смену destination, чтобы скрывать/показывать bottom nav на нужных экранах
            controller.addOnDestinationChangedListener { _, destination, _ ->
                handleDestinationChanged(destination.id)
            }

            isNavControllerReady = true
        } catch (e: Exception) {
            e.printStackTrace()
            navController = null
            isNavControllerReady = false
        }
    }

    private fun handleDestinationChanged(destinationId: Int) {
        val bottom = bottomNav ?: return

        val hideOn = setOf(
            R.id.gettingStartedFragment,
            R.id.loginFragment,
            R.id.signUpFragment1,
            R.id.signUpFragment2,
            R.id.signUpFragment3,
            R.id.signUpSuccessFragment,
            R.id.noConnectionFragment,
            R.id.splashFragment,
            R.id.onboardingFragment1,
            R.id.onboardingFragment2,
            R.id.onboardingFragment3
        )

        if (hideOn.contains(destinationId)) {
            bottom.visibility = View.GONE
        } else {
            bottom.visibility = View.VISIBLE
        }
    }

    private fun getNavController(): NavController? {
        return navController ?: try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            navHostFragment?.navController
        } catch (e: Exception) {
            null
        }
    }

    private fun handleNetworkChange(connected: Boolean) {
        val controller = getNavController() ?: return

        val currentDestination = controller.currentDestination?.id

        if (!connected) {
            // Показываем экран "Нет соединения" только если его еще нет
            if (currentDestination != R.id.noConnectionFragment) {
                try {
                    // Используем безопасную навигацию
                    controller.navigate(R.id.noConnectionFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            // Сеть появилась - закрываем экран "Нет соединения"
            if (currentDestination == R.id.noConnectionFragment) {
                try {
                    controller.popBackStack()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Добавляем наблюдение за жизненным циклом фрагментов
    override fun onResume() {
        super.onResume()
        // При возвращении в активность проверяем состояние сети
        connectivityLiveData.value?.let { connected ->
            handleNetworkChange(connected)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return getNavController()?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
