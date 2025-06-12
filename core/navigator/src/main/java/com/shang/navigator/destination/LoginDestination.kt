package com.shang.navigator.destination

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink

const val LOGIN_ROUTE = "LoginScreenRoute"
class LoginDestination:NavigationDestination {
    override fun destination(): String  = LOGIN_ROUTE
}