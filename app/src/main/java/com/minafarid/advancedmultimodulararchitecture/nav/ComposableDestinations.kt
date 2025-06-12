package com.minafarid.advancedmultimodulararchitecture.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.shang.home.HomeScreen
import com.shang.login.presentation.view.LoginScreen
import com.shang.navigator.core.AppNavigator
import com.shang.navigator.destination.HomeDestination
import com.shang.navigator.destination.LoginDestination
import com.shang.navigator.destination.NavigationDestination
import com.shang.navigator.destination.SignUpDestination
import com.shang.signup.SignUpScreen

private val composableDestinations: Map<NavigationDestination, @Composable (AppNavigator, NavHostController) -> Unit> =
    mapOf(
        SignUpDestination() to { _, _ -> SignUpScreen() },
        HomeDestination to { _, navHostController -> HomeScreen(navHostController) },
        LoginDestination() to { appNavigator, _ -> LoginScreen(appNavigator = appNavigator) },
    )

fun NavGraphBuilder.addComposableDestinations(
    appNavigator: AppNavigator,
    navHostController: NavHostController,
) {
    composableDestinations.forEach { entry ->
        val destination = entry.key
        composable(
            route = destination.destination(),
            arguments = destination.arguments,
            deepLinks = destination.deepLink,
        ) {
            entry.value(appNavigator, navHostController)
        }
    }
}
