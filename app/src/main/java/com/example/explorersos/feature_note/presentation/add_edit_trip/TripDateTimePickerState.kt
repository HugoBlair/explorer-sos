package com.example.explorersos.feature_note.presentation.add_edit_trip

import java.time.Instant

data class TripDateTimePickerState(
    val selectedDateTime: Instant? = null,
    val isDateTimePickerVisible: Boolean = false,
    val hint: String = ""
)