package com.shang.navigator.core

import androidx.navigation.NavOptionsBuilder
import com.shang.navigator.event.NavigatorEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AppNavigatorImp : AppNavigator {
    private val _navigateEvents = Channel<NavigatorEvent>()

    override fun navigateUp(): Boolean {
        return _navigateEvents.trySend(NavigatorEvent.NavigateUp).isSuccess
    }

    override fun popBackStack() {
        _navigateEvents.trySend(NavigatorEvent.PopupStack)
    }

    override fun navigate(destination: String, builder: NavOptionsBuilder.() -> Unit): Boolean {
        return _navigateEvents.trySend(NavigatorEvent.Directions(destination, builder)).isSuccess
    }

    override val destinationsFlow: Flow<NavigatorEvent>
        get() = _navigateEvents.receiveAsFlow()
}
