package com.example.mobcomp25.data

import androidx.room.Query

interface NotesDao {
    @Query("SELECT * FROM notes")
    fun getAll(): List<Note>

    @Query()
}