package com.example.explorersos.feature_note.presentation.add_edit_trip

import androidx.compose.ui.focus.FocusState
import java.time.Instant

sealed class AddEditTripEvent {
    // Title events
    data class EnteredTitle(val value: String) : AddEditTripEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditTripEvent()

    // Content/Description events
    data class EnteredDescription(val value: String) : AddEditTripEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditTripEvent()

    // Start Location events
    data class EnteredStartLocation(val value: String) : AddEditTripEvent()
    data class ChangeStartLocationFocus(val focusState: FocusState) : AddEditTripEvent()

    // End Location events
    data class EnteredEndLocation(val value: String) : AddEditTripEvent()
    data class ChangeEndLocationFocus(val focusState: FocusState) : AddEditTripEvent()

    // Start DateTime events
    data class EnteredStartDateTime(val value: Instant) : AddEditTripEvent()
    data class ChangeStartDateTimeFocus(val focusState: FocusState) : AddEditTripEvent()

    // End DateTime events
    data class EnteredEndDateTime(val value: Instant) : AddEditTripEvent()
    data class ChangeEndDateTimeFocus(val focusState: FocusState) : AddEditTripEvent()
    
    //Set Active Status
    data class SetActiveStatus(val value: Boolean) : AddEditTripEvent()


    // Toggle events
    object ToggleActiveStatus : AddEditTripEvent()
    object ToggleRoundTrip : AddEditTripEvent()

    // Action events
    object SaveTrip : AddEditTripEvent()
}