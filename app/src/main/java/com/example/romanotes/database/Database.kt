package com.example.romanotes.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.romanotes.models.NoteItem

class Database(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    private const val DATABASE_VERSION = 1
    private const val DATABASE_NAME = "Database"
    private const val TABLE_NAME = "TableName"

    private const val KEY_ID = "_id"
    private const val KEY_TITLE = "title"
    private const val KEY_NOTE = "note"
    private const val KEY_TIMESTAMP = "timeStamp"
  }

  override fun onCreate(db: SQLiteDatabase?) {
    val CREATE_TABLE = ("CREATE_TABLE $TABLE_NAME($KEY_ID INTEGER PRIMARY KEY,$KEY_TITLE TEXT,$KEY_NOTE TEXT,$KEY_TIMESTAMP TEXT)")
    db?.execSQL(CREATE_TABLE)
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
  }

  fun addNote(noteItem: NoteItem) : Long{
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_TITLE, noteItem.title)
    contentValues.put(KEY_TIMESTAMP, noteItem.timeStamp)
    contentValues.put(KEY_NOTE, noteItem.note)
    contentValues.put(KEY_ID, noteItem.id)

    val result = db.insert(TABLE_NAME, null, contentValues)

    db.close()
    return result
  }

  fun editNote(note: String, title: String, id: Int) : Int{
    val db = this.writableDatabase

    val contentValues = ContentValues()
    contentValues.put(KEY_TITLE, title)
    contentValues.put(KEY_NOTE, note)

    val result = db.update(TABLE_NAME, contentValues, "_id=$id", null)

    db.close()
    return result
  }

  @SuppressLint("Range")
  fun getNotes(): ArrayList<NoteItem> {

    val noteList: ArrayList<NoteItem> = ArrayList()

    val selectQuery = "SELECT  * FROM $TABLE_NAME"

    val db = this.readableDatabase

    try {
      val cursor: Cursor = db.rawQuery(selectQuery, null)
      if (cursor.moveToFirst()) {
        do {
          val note = NoteItem(
            id = cursor.getInt((cursor.getColumnIndex(KEY_ID))),
            title = cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
            note = cursor.getString(cursor.getColumnIndex(KEY_NOTE)),
            timeStamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))

          )
          noteList.add(note)

        } while (cursor.moveToNext())
      }
      cursor.close()
    } catch (e: SQLiteException) {
      db.execSQL(selectQuery)
      return ArrayList()
    }
    return noteList
  }

  fun deleteNote(id: Int): Int {
    val db = this.writableDatabase
    val delete = db.delete(TABLE_NAME, "$KEY_ID=$id", null)

    db.close()
    return delete
  }
}