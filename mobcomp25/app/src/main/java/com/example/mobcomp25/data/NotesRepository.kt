package com.example.mobcomp25.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotesStream(): Flow<List<Note>> //suspend fun??????

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note:Note)
}