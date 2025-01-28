package com.example.mobcomp25.data

import android.content.Context


/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(DataBaseHost.getDataBase(context).noteDao())
    }

}
