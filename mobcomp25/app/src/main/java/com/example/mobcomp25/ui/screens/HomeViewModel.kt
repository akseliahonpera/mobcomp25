package com.example.mobcomp25.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mobcomp25.data.Note
import com.example.mobcomp25.data.NoteRepoTest
import com.example.mobcomp25.data.NotesRepository
import com.example.mobcomp25.data.OfflineNotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(private val repository:NoteRepoTest) :ViewModel(){
 //   private val _noteContents = mutableListOf<Note>()
 //   val noteContents : MutableList<Note> = _noteContents
    private val _noteContents = mutableListOf<Note>()
    val noteContents: Flow<List<Note>> = repository.noteContents

}