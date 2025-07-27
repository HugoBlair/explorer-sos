package com.example.explorersos.feature_trip.presentation.add_edit_contact

import androidx.compose.ui.focus.FocusState

sealed class AddEditContactEvent {
    data class EnteredFirstName(val value: String) : AddEditContactEvent()
    data class ChangedFirstNameFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredLastName(val value: String) : AddEditContactEvent()
    data class ChangedLastNameFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredPhoneNumber(val value: String) : AddEditContactEvent()
    data class ChangedPhoneNumberFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredEmail(val value: String) : AddEditContactEvent()
    data class ChangedEmailFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredNotes(val value: String) : AddEditContactEvent()
    data class ChangedNotesFocus(val focusState: FocusState) : AddEditContactEvent()

    object SaveContact : AddEditContactEvent()

}