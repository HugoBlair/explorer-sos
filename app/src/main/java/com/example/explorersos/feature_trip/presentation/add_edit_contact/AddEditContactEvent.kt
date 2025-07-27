package com.example.explorersos.feature_trip.presentation.add_edit_contact

import androidx.compose.ui.focus.FocusState

sealed class AddEditContactEvent {
    data class EnteredFirstName(val value: String) : AddEditContactEvent()
    data class ChangeFirstNameFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredLastName(val value: String) : AddEditContactEvent()
    data class ChangeLastNameFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredPhoneNumber(val value: String) : AddEditContactEvent()
    data class ChangePhoneNumberFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredEmail(val value: String) : AddEditContactEvent()
    data class ChangeEmailFocus(val focusState: FocusState) : AddEditContactEvent()

    data class EnteredNotes(val value: String) : AddEditContactEvent()
    data class ChangeNotesFocus(val focusState: FocusState) : AddEditContactEvent()

    object SaveContact : AddEditContactEvent()

}