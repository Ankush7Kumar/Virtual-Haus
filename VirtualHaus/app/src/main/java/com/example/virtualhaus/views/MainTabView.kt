package com.example.virtualhaus.views

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.virtualhaus.R
import com.example.virtualhaus.views.common.*
import com.example.virtualhaus.views.destinations.*
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine

private val NAVIGATION_BAR_HEIGHT = 56.dp



@Composable
fun MainTabView(startDestination: DirectionDestination) {
    val engine = rememberNavHostEngine()
    val navController = engine.rememberNavController()
    val currentRoute = navController.currentDestination()?.route ?: startDestination.route

    Scaffold(
        topBar = {
            if (shouldShowNavigationBar(currentRoute)) {
                TopTabBar(navController = navController, tabGroups = tabGroups)
            }
        },
        bottomBar = {
            if (shouldShowNavigationBar(currentRoute)) {
                NavigationBar(navController = navController, items = MainTabItem.values().toList())
            }
        },
        content = {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                navController = navController,
                startDestination = startDestination,
                modifier = if (shouldShowNavigationBar(currentRoute)) {
                    Modifier.padding(bottom = NAVIGATION_BAR_HEIGHT)
                } else {
                    Modifier
                },
            )
        },
    )
}

private fun shouldShowNavigationBar(route: String): Boolean {
    return route !in ONBOARDING_DESTINATIONS
}

// The top tabs for each of the tabs.
// Order of the set DOES NOT MATTER.
// Order of each sub-list DOES MATTER.
private val tabGroups = setOf(
    TabGroup(listOf(Tab("Settings", SettingScreenDestination))),
    TabGroup(listOf(Tab("Today", HomeScreenDestination))),
    TabGroup(listOf(Tab("Common Space Events", CommonSpaceScreenDestination))),
    TabGroup(listOf(Tab("Groceries", GroceryScreenDestination), Tab("Cooking", CookingScreenDestination))),
    TabGroup(listOf(Tab("Washroom", WashroomScreenDestination))),
    TabGroup(listOf(Tab("Quiet Time", QuietTimeScreenDestination), Tab("At Home", StatusScreenDestination))),
    TabGroup(listOf(Tab("Finance", FinanceScreenDestination))),
    TabGroup(listOf(Tab("Laundry", LaundryScreenDestination))),
)

// Each tab for the app. ORDER MATTERS.
private enum class MainTabItem(
    override val direction: DirectionDestination,
    @DrawableRes override val icon: Int,
    @StringRes override val label: Int,
): NavigationBarItem {
    CommonSpaces(CommonSpaceScreenDestination, R.drawable.ic_couch, R.string.common_space_label),
    Kitchen(GroceryScreenDestination, R.drawable.ic_kitchen, R.string.kitchen_label),
    Washrooms(WashroomScreenDestination, R.drawable.ic_toilet, R.string.washroom_label),
    Bedrooms(QuietTimeScreenDestination, R.drawable.ic_bed, R.string.bedroom_label),
    Finances(FinanceScreenDestination, R.drawable.ic_money, R.string.finance_label),
    Laundry(LaundryScreenDestination, R.drawable.ic_washer, R.string.laundry_label),
}



