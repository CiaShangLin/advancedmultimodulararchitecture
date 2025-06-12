package com.shang.navigator.event

import androidx.navigation.NavOptionsBuilder

sealed class NavigatorEvent {
    data object NavigateUp : NavigatorEvent()

    data object NavigateBack : NavigatorEvent()

    class Directions(val direction: String, val builder: NavOptionsBuilder.() -> Unit) :
        NavigatorEvent()
    class Navigate(val route: String, val builder: NavOptionsBuilder.() -> Unit) :
        NavigatorEvent()
}