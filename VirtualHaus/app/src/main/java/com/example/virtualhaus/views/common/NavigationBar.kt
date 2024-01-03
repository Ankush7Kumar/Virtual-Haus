package com.example.virtualhaus.views.common

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.virtualhaus.ui.theme.BluePrimary
import com.example.virtualhaus.ui.theme.BodyFont
import com.example.virtualhaus.views.destinations.DirectionDestination

interface NavigationBarItem {
    val direction: DirectionDestination
    val icon: Int
    val label: Int
}

@Composable
fun NavigationBar(navController: NavHostController, items: List<NavigationBarItem>) {
    val currentRoute = navController.currentDestination()?.route

    BottomNavigation(backgroundColor = BluePrimary, contentColor = Color.White) {
        items.forEachIndexed { index, item ->
            ItemDivider(index = index)

            BottomNavigationItem(
                selected = currentRoute == item.direction.route,
                onClick = { navController.navigate(item.direction) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = stringResource(item.label),
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = item.label),
                        fontFamily = BodyFont,
                        fontSize = 8.sp,
                    )
                },
            )
        }
    }
}

@Composable
fun ItemDivider(index: Int) {
    if (index == 0) return
    Divider(
        color = Color.White,
        modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .alpha(0.1F),
    )
}