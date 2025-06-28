package com.example.explorersos.feature_note.presentation.add_edit_trip

import androidx.compose.ui.focus.FocusState

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

    // Start Date events
    data class EnteredStartDate(val value: String) : AddEditTripEvent()
    data class ChangeStartDateFocus(val focusState: FocusState) : AddEditTripEvent()

    // Expected End Date events
    data class EnteredExpectedEndDate(val value: String) : AddEditTripEvent()
    data class ChangeExpectedEndDateFocus(val focusState: FocusState) : AddEditTripEvent()

    // Start Time events
    data class EnteredStartTime(val value: String) : AddEditTripEvent()
    data class ChangeStartTimeFocus(val focusState: FocusState) : AddEditTripEvent()

    // Expected End Time events
    data class EnteredExpectedEndTime(val value: String) : AddEditTripEvent()
    data class ChangeExpectedEndTimeFocus(val focusState: FocusState) : AddEditTripEvent()

    // Toggle events
    object ToggleActiveStatus : AddEditTripEvent()
    object ToggleRoundTrip : AddEditTripEvent()

    // Action events
    object SaveTrip : AddEditTripEvent()
}