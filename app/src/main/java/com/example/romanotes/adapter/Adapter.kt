package com.example.romanotes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.romanotes.R
import com.example.romanotes.models.NoteItem


class Adapter(
  private var list: MutableList<NoteItem>,
  private var clickListener: NoteClickListener
)
  : RecyclerView.Adapter<ListViewHolder>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return ListViewHolder(inflater, parent)
  }

  override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
    val noteItem = list[position]
    val itemView = holder.itemView
    val TitleText = itemView.findViewById<TextView>(R.id.note_item_title)
    val MessageText = itemView.findViewById<TextView>(R.id.note_item_body)
    val button = itemView.findViewById<Button>(R.id.note_item_button)

    TitleText.text = noteItem.title
    MessageText.text = noteItem.note

    itemView.setOnClickListener {
      clickListener.onClick(noteItem)
    }
    button.setOnClickListener {
      clickListener.onDelete(noteItem)
    }
  }
  override fun getItemCount(): Int = list.size
}

interface NoteClickListener {
  fun onClick(noteItem: NoteItem)
  fun onDelete(noteItem: NoteItem)
}
class ListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
  RecyclerView.ViewHolder(inflater.inflate(R.layout.note_item, parent, false)) {
}