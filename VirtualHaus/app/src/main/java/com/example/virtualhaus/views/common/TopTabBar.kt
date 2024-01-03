package com.example.virtualhaus.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.HeaderFont
import com.example.virtualhaus.views.destinations.DirectionDestination
import com.example.virtualhaus.views.destinations.HomeScreenDestination
import com.example.virtualhaus.views.destinations.SettingScreenDestination

data class Tab(val title: String, val destination: DirectionDestination)

data class TabGroup(val tabs: List<Tab>)

@Composable
fun TopTabBar(navController: NavController, tabGroups: Set<TabGroup>) {
    val currentRoute = navController.currentDestination()?.route
    val currentTabGroup = tabGroups.firstOrNull { group ->
        group.tabs.any { it.destination.route == currentRoute }
    }

    val iconWidth = 50
    val tabsWidth = LocalConfiguration.current.screenWidthDp - 2 * iconWidth

    TopAppBar(contentPadding = PaddingValues(0.dp), backgroundColor = BluePrimary) {
        IconButton(
            modifier = Modifier.width(iconWidth.dp),
            onClick = { navController.navigate(HomeScreenDestination) },
            content = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) }
        )

        currentTabGroup?.tabs?.forEach { tab ->
            Box(
                modifier = Modifier
                    .width(tabsWidth.dp / currentTabGroup.tabs.size)
                    .fillMaxHeight(),
            ) {
                PrimaryButton(
                    onClick = { navController.navigate(tab.destination) },
                    content = {
                        Text(
                            text = tab.title,
                            fontSize = 20.sp,
                            fontFamily = HeaderFont,
                            color = Color.White,
                        )
                    },
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxSize(),
                )

                // "Selection" highlighting
                if (currentRoute == tab.destination.route) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.05F)
                            .background(Color.Black),
                    )
                }
            }
        }

        IconButton(
            modifier = Modifier.width(iconWidth.dp),
            onClick = { navController.navigate(SettingScreenDestination) },
            content = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White) }
        )
    }
}