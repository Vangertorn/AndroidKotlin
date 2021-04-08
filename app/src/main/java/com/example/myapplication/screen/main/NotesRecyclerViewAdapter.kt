package com.example.myapplication.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.support.navigateSafe

class NotesRecyclerViewAdapter(
    private val items: List<Note>,
    private val itemClick: (Int) -> Unit
) : RecyclerView.Adapter<NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return NoteViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}

class NoteViewHolder(
    itemView: View,
    private val itemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
    private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
    fun bind(item: Note) {
        tvTitle.text = item.title
        tvDate.text = item.date
        itemView.setOnClickListener {
            itemClick(adapterPosition)
        }

    }


}

