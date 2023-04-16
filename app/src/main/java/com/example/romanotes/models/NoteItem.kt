package com.example.romanotes.models

data class NoteItem(
  val id: Int,
  val title: String? = null,
  val note: String? = null,
  val timeStamp: String? = null
)