package com.example.mobcomp25.data

import kotlinx.coroutines.flow.Flow

class OfflineNotesRepository(private val noteDao:NotesDao):NotesRepository {

    override fun getAllNotesStream(): Flow<List<Note>> =noteDao.getAll()//suspend fun??????

    override suspend fun insertNote(note: Note)=noteDao.insertNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.delete(note)
}