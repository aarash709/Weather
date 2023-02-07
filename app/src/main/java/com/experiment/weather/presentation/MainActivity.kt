package com.experiment.weather.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.experiment.weather.presentation.ui.WeatherApp
import com.weather.core.design.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//        private val viewModel: MainPageViewModel by lazy {
//        ViewModelProvider(this).get(MainPageViewModel::class.java)
//    }
//    private lateinit var binding: ActivityMainBinding
//    val vm: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//            }
//        }
        setContent {
            WeatherApp()
        }
    }
}
