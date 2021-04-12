package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.fragment.NavHostFragment

import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

    }
}


