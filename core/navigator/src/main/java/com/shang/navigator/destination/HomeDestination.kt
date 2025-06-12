package com.shang.navigator.destination

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.navArgument

const val USER_PARAM = "user"
const val USER_AGE = "user_age"
const val USER_FULL_NAME = "user_full_name"
const val HOME_ROUTE = "HomeScreenRoute"

object HomeDestination : NavigationDestination {

    fun createHome(user: String, fullName: String, age: Int): String {
        return "$HOME_ROUTE/$user/$fullName/$age"
    }

    override fun destination(): String = HOME_ROUTE

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(USER_PARAM) {
                type = StringType
                defaultValue = "default_user"
            },
            navArgument(USER_AGE) {
                type = IntType
                defaultValue = 18
            },
            navArgument(USER_FULL_NAME) {
                type = StringType
                defaultValue = "Default User"
            },
        )

    override val deepLink: List<NavDeepLink> get() = listOf()
}
