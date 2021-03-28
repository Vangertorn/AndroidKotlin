package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
//    private lateinit var tvCount: TextView
//    private lateinit var  btn: Button
//    private lateinit var tvCount1:TextView
//    private lateinit var btn1: Button
//    private lateinit var tvCount2:TextView
//    private lateinit var btn2:Button
//
//    private var count1 =0
//    set(value) {
//        field = value
//        tvCount1.text=field.toString()
//    }
//
//    private var count = 0
//    set(value) {
//        field = value
//        tvCount.text = field.toString()
//    }
//    private var sum =0
//    set(value){
//        field = value
//        tvCount2.text = field.toString()
//    }
    private lateinit var tvText: EditText
    private lateinit var tvButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val viewModel = MainViewModel()
        recyclerView.adapter = NotesRecyclerViewAdapter(viewModel.notes)
        tvText = findViewById(R.id.textView1)
        tvButton = findViewById(R.id.button1)
        tvButton.setOnClickListener {
            if (tvText.text.toString() != ""){
                viewModel.addNotes(Note(tvText.text.toString()))
            }

        }



//        tvCount = findViewById(R.id.tvCount)
//        btn = findViewById(R.id.btn)
//        tvCount1 = findViewById(R.id.tvCount1)
//        btn1 = findViewById(R.id.btn1)
//
//        btn.setOnClickListener {
//            count++
//        }
//        btn1.setOnClickListener {
//            count1++
//        }
//        tvCount2 = findViewById(R.id.tvCount2)
//        btn2 = findViewById(R.id.btn2)
//
//        btn2.setOnClickListener {
//            sum = count+count1
//        }
    }

}