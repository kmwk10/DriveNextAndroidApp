package com.example.drivenextapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.drivenextapp.util.ConnectivityLiveData

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityLiveData: ConnectivityLiveData
    private var isNavControllerReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Инициализируем наблюдение за сетью
        connectivityLiveData = ConnectivityLiveData(this)
        connectivityLiveData.observe(this) { connected ->
            handleNetworkChange(connected)
        }
    }

    private fun getNavController(): NavController? {
        return try {
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
}