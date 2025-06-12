package com.shang.navigator.core

import androidx.navigation.NavOptionsBuilder
import com.shang.navigator.event.NavigatorEvent
import kotlinx.coroutines.flow.Flow

interface AppNavigator {

    fun navigateUp(): Boolean
    fun popBackStack()
    fun navigate(
        destination: String,
        builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
    ): Boolean

    val destinationsFlow: Flow<NavigatorEvent>
}