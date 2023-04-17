package com.example.romanotes.dialogs

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.romanotes.R
import com.example.romanotes.models.NoteItem
import java.time.Instant
import java.time.format.DateTimeFormatter


class Dialog (private val noteListener: AddNoteListener, private val note: NoteItem?): DialogFragment() {

  private var editMode: Boolean = false

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val rootview = layoutInflater.inflate(R.layout.note_dialog, container, false)

    val button = rootview.findViewById<Button>(R.id.note_dialog_button)
    val notetext = rootview.findViewById<EditText>(R.id.note_dialog_body)
    val notetitle = rootview.findViewById<EditText>(R.id.note_dialog_title)

    note?.let {
      editMode = true
      notetitle.setText(it.title)
      notetext.setText(it.note)
    }

    if (editMode){
      button.text = getString(R.string.save)
    }

    button.setOnClickListener {
      activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

      if (notetitle.text.toString().isEmpty() || notetext.text.toString().isEmpty()){
        Toast.makeText(requireContext(), "Укажите название и текст заметки", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
      }

      if (editMode && note != null){
        noteListener.editNote(notetext.text.toString(), notetitle.text.toString(), note.id!!)
        return@setOnClickListener
      }

      val note = NoteItem(
        title = notetitle.text.toString(),
        note = notetext.text.toString(),
        timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).toString()
      )
      noteListener.addNote(note)
    }
    return rootview
  }
}

interface AddNoteListener {
  fun addNote(note: NoteItem)
  fun editNote(note: String, title: String, id: Int)
}