package com.example.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment

import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment }
    private val navController: NavController by lazy { navHostFragment.navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (navController.popBackStack().not()) {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press \"back\" again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
//        var exitToast: Toast? = null
//        if (navController.popBackStack().not()) {
//            if (backPressTime + 2000 > System.currentTimeMillis()) {
//                super.onBackPressed()
//            } else {
//                Toast.makeText(this, "Press \"back\" again to exit", Toast.LENGTH_SHORT).show()
//            }
//            val backPressTime = System.currentTimeMillis()
//            if (exitToast == null||exitToast!!.view ==null|| exitToast!!.view!!.windowToken==null) {
//                exitToast = Toast.makeText(this, "Press \"back\" again to exit", Toast.LENGTH_SHORT)
//                exitToast!!.addCallback(object : Toast.Callback() {
//                    override fun onToastHidden() {
//                        super.onToastHidden()
//                        exitToast == null
//                    }
//                })
//                exitToast!!.show()
//            } else {
//                exitToast!!.cancel()
//                finish()
//            }
//        }
//    }
    }
}


