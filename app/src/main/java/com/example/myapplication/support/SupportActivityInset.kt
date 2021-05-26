package com.example.myapplication.support

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class SupportActivityInset<T : ViewBinding> : AppCompatActivity(), ViewBindable<T>,
    OnSystemBarsSizeChangedListener {

    override var insets: VerticalInset = VerticalInset.empty()

    abstract fun getActiveFragment(): Fragment?

    override fun insetsChanged(statusBarSize: Int, navigationBarSize: Int, hasKeyboard: Boolean) {
        insets = VerticalInset(statusBarSize, navigationBarSize, hasKeyboard)
        val fragment = getActiveFragment()
        if (fragment != null && fragment is SupportFragmentInset<*>) {
            fragment.onInsetsReceived(statusBarSize, navigationBarSize, hasKeyboard)
        }
    }
}