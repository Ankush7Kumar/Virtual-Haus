package com.example.virtualhaus.views.common

import com.example.virtualhaus.views.destinations.*

val ONBOARDING_DESTINATIONS = setOf(
    InitialWelcomeScreenDestination,
    CreateHomeScreenDestination,
    JoinHomeScreenDestination,
    UniqueHomeCodeScreenDestination,
).map { it.route }

val INITIAL_DESTINATION_AFTER_LOGIN = HomeScreenDestination