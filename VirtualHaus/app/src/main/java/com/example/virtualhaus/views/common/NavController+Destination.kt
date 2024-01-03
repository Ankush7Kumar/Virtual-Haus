package com.example.virtualhaus.views.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.virtualhaus.views.destinations.DirectionDestination

@Composable
fun NavController.currentDestination(): NavDestination? {
    return currentBackStackEntryFlow.collectAsState(initial = null).value?.destination
}

fun NavController.navigate(direction: DirectionDestination) {
    if (currentDestination?.route == direction.route) return
    if (popBackStack(direction.route, false)) return
    navigate(direction.route)
}
