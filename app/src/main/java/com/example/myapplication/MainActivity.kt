package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)


    }

    override fun onResume() {
        super.onResume()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val viewModel = MainViewModel()

        viewModel.listLiveData.observe(this, {
            recyclerView.adapter = NotesRecyclerViewAdapter(it)
        })

        viewBinding.button1.setOnClickListener {
            viewModel.addNotes(viewBinding.textView1.text.toString())
            viewBinding.textView1.setText("")
        }
    }


}