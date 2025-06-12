package com.shang.navigator

import androidx.navigation.NavOptionsBuilder
import com.shang.navigator.event.NavigatorEvent
import kotlinx.coroutines.flow.Flow

interface AppNavigator {

    fun navigateUp(): Boolean
    fun popBackStack()
    fun navigate(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
    ): Boolean

    val destinationsFlow: Flow<NavigatorEvent>
}