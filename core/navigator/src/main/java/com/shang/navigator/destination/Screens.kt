package com.shang.navigator.destination


sealed class Screens(val route: String) {

    data object LoginScreenRoute : Screens(LOGIN_ROUTE)
    data object SignUpScreenRoute : Screens(SIGNUP_ROUTE)
    data object HomeScreenRoute :
        Screens(route = "$HOME_ROUTE/${USER_PARAM}/${USER_FULL_NAME}/${USER_AGE}")
}