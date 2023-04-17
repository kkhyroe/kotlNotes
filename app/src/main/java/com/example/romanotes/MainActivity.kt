package com.example.romanotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.romanotes.adapter.Adapter
import com.example.romanotes.adapter.NoteClickListener
import com.example.romanotes.database.Database
import com.example.romanotes.dialogs.AddNoteListener
import com.example.romanotes.dialogs.Dialog
import com.example.romanotes.models.NoteItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AddNoteListener, NoteClickListener {

  private lateinit var db : Database

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    db = Database(this)
    getNotesFromLocalDB()

    val floatingbutton = findViewById<FloatingActionButton>(R.id.notes_fab)

    floatingbutton.setOnClickListener {
      val dialog =
        Dialog(this, null)
      dialog.show(supportFragmentManager, "Добавить заметку")
    }
  }

  private fun getNotesFromLocalDB() {

    val getNoteList = db.getNotes()

    if (getNoteList.size > 0) {
      setupRecyclerView(getNoteList)
    } else {
      val emptylist = ArrayList<NoteItem>()
      setupRecyclerView(emptylist)

    }
  }

  private fun setupRecyclerView(
    notelist: ArrayList<NoteItem>
  ) {

    val rv = findViewById<RecyclerView>(R.id.notes_view)
    val horizontalLayoutManagaer =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    rv.layoutManager = horizontalLayoutManagaer

    val adapter = Adapter(notelist, this)
    rv.adapter = adapter
  }

  private fun showDeleteDialog(noteId: Int){
    val builder = AlertDialog.Builder(this)
    builder.setMessage("Вы уверены, что хотите удалить заметку?")
      .setCancelable(false)
      .setPositiveButton("Да") { dialog, _ ->
        val result = db.deleteNote(noteId)

        if (result > 0){
          getNotesFromLocalDB()
          Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show()
        } else{
          Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show()
          dialog.dismiss()
        }
      }

      .setNegativeButton("Нет") { dialog, _ ->
        dialog.dismiss()
      }
    val dialog = builder.create()
    dialog.show()
  }

  override fun addNote(note: NoteItem) {
    db.addNote(note)
    getNotesFromLocalDB()
  }

  override fun editNote(note: String, title: String, id: Int) {
    db.editNote(note,title, id)
    getNotesFromLocalDB()
  }

  override fun onClick(noteItem: NoteItem) {
    val dialog =
      Dialog(this, noteItem)
    dialog.show(supportFragmentManager, "Изменить заметку")
  }

  override fun onDelete(noteItem: NoteItem) {
    noteItem.id?.let { showDeleteDialog(it) }
  }
}