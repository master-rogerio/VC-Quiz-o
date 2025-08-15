package com.example.vcquizo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.factory.ProfileViewModelFactory
import com.example.vcquizo.navigation.MyAppNavigation
import com.example.vcquizo.ui.theme.VCQuizoTheme
import com.example.vcquizo.view.model.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Criar instâncias do repositório e da factory
        val userRepository = UserRepository(applicationContext)
        val viewModelFactory = ProfileViewModelFactory(userRepository, application)

        // Usar a factory para obter a ViewModel
        val authViewModel : AuthViewModel by viewModels { viewModelFactory }

        setContent {
            VCQuizoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
                }
            }
        }
    }
}