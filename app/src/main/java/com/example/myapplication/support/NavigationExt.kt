package com.example.myapplication.support

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
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

