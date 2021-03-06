package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.support.SupportActivityInset
import com.example.myapplication.support.setWindowTransparency

class MainActivity : SupportActivityInset<ActivityMainBinding>() {

    override lateinit var viewBinding: ActivityMainBinding
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }
    private val handler = Handler(Looper.getMainLooper())
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setWindowTransparency(this)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onBackPressed() {
        if (navHostFragment.childFragmentManager.backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, getString(R.string.Press_back), Toast.LENGTH_SHORT).show()
            handler.postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        } else navController.popBackStack()
    }

    override fun getActiveFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments[0]
    }
}


