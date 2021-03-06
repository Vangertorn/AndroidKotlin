package com.example.myapplication.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Note

class NotesRecyclerViewAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NotesRecyclerViewAdapter.NoteViewHolder>(NoteAdapterDiffCallBack()) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = NoteViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false),
        ::onItemClick,
    )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun onItemClick(position: Int) {
        onClick(getItem(position))
    }

    inner class NoteViewHolder(
        itemView: View,
        private val itemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val ivCloud = itemView.findViewById<ImageView>(R.id.ivCloudIndicate)
        private val ivAlarm = itemView.findViewById<ImageView>(R.id.ivAlarm)
        private val ivPostscript = itemView.findViewById<TextView>(R.id.tvPostscript)


        init {
            itemView.setOnClickListener {
                itemClick(adapterPosition)
            }
        }

        fun bind(item: Note) {
            tvTitle.text = item.title
            tvDate.text = item.date
            ivCloud.isVisible = item.cloud
            ivAlarm.isVisible = item.alarmEnabled
            ivPostscript.text = item.postscript
        }
    }
}


class NoteAdapterDiffCallBack : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.date == newItem.date &&
                oldItem.title == newItem.title &&
                oldItem.cloud == newItem.cloud &&
                oldItem.alarmEnabled == newItem.alarmEnabled &&
                oldItem.postscript == newItem.postscript&&
                oldItem.position==newItem.position
    }

}




