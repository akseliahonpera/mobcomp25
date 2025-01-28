package com.example.mobcomp25.data

import kotlinx.coroutines.flow.Flow

class NoteRepoTest (private val notesDao:NotesDao){
    val noteContents: Flow<List<Note>> = notesDao.getAll()
}