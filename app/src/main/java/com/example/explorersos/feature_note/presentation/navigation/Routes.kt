package com.example.explorersos.feature_note.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object NotesScreenRoute

@Serializable
data class AddEditNoteScreenRoute(
    val noteId: Int = -1, // Use a default value to make it optional
    val noteColor: Int = -1
)