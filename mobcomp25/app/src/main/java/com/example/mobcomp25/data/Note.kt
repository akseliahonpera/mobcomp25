package com.example.mobcomp25.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "note_contents") val noteContents: String,
    @ColumnInfo(name = "image_uri") val imageUri: String
)
