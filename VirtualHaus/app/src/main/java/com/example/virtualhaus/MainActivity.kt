package com.example.virtualhaus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.virtualhaus.models.UserManager
import com.example.virtualhaus.views.MainTabView
import com.example.virtualhaus.views.common.INITIAL_DESTINATION_AFTER_LOGIN
import com.example.virtualhaus.views.destinations.InitialWelcomeScreenDestination
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val userManager = UserManager.shared
        userManager.loadUserInfo(context = this)

        val startDestination = if (userManager.homeId == null) {
            InitialWelcomeScreenDestination
        } else {
            INITIAL_DESTINATION_AFTER_LOGIN
        }

        setContent {
            MainTabView(startDestination = startDestination)
        }
    }
}