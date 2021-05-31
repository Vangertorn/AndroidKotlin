package com.example.myapplication.support

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

interface OnSystemBarsSizeChangedListener {
    val insets: VerticalInset
    fun insetsChanged(statusBarSize: Int, navigationBarSize: Int, hasKeyboard: Boolean)
}

data class VerticalInset(val top: Int, val bottom: Int, val hasKeyboard: Boolean) {
    companion object {
        fun empty() = VerticalInset(0, 0, false)
    }
}

fun Dialog.setWindowTransparency(
    listener: OnSystemBarsSizeChangedListener
) {
    window?.decorView?.overrideSystemInsets(listener)
    window?.navigationBarColor = Color.TRANSPARENT
    window?.statusBarColor = Color.TRANSPARENT
}

fun Activity.setWindowTransparency(listener: OnSystemBarsSizeChangedListener) {
    window.decorView.overrideSystemInsets(listener)
    window.navigationBarColor = Color.TRANSPARENT
    window.statusBarColor = Color.TRANSPARENT
}


private fun View.overrideSystemInsets(listener: OnSystemBarsSizeChangedListener) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->

        val desiredBottomInset = calculateDesiredBottomInset(
            this,
            insets.systemWindowInsetTop,
            insets.systemWindowInsetBottom,
            listener
        )

        ViewCompat.onApplyWindowInsets(
            this,
            WindowInsetsCompat.Builder(insets)
                .setSystemWindowInsets(Insets.of(0, 0, 0, desiredBottomInset))
                .build()
        )
    }
}

fun calculateDesiredBottomInset(
    view: View,
    systemWindowInsetTop: Int,
    systemWindowInsetBottom: Int,
    listener: OnSystemBarsSizeChangedListener
): Int {
    val hasKeyboard = view.isKeyboardAppeared(systemWindowInsetBottom)
    val desiredBottomInset = if (hasKeyboard) systemWindowInsetBottom else 0
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
        listener.insetsChanged(systemWindowInsetTop, systemWindowInsetBottom, hasKeyboard)
    } else {
        listener.insetsChanged(
            systemWindowInsetTop,
            if (hasKeyboard) 0 else systemWindowInsetBottom,
            hasKeyboard
        )
    }
    return desiredBottomInset
}

private fun View.isKeyboardAppeared(systemWindowInsetBottom: Int): Boolean {
    return systemWindowInsetBottom / resources.displayMetrics.heightPixels.toDouble() > .25
}
