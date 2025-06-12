package com.shang.navigator.destination

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

interface NavigationDestination {
    fun destination(): String
    val arguments: List<NamedNavArgument> get() = emptyList()
    val deepLink: List<NavDeepLink> get() = emptyList()
}