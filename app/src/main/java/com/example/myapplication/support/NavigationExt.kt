package com.example.myapplication.support

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun NavController.navigateSafe(
    navDirections: NavDirections,
    navOptions: NavOptions? = null
) {
    val action = currentDestination?.getAction(navDirections.actionId)
    if (action != null) navigate(navDirections, navOptions)
}