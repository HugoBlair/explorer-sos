package com.example.explorersos.feature_trip.presentation.add_edit_contact

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.model.Contact.InvalidContactException
import com.example.explorersos.feature_trip.domain.use_case.contact.ContactUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddEditContactViewModel(
    private val useCases: ContactUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _firstName = mutableStateOf(ContactTextFieldState(hint = "Enter first name"))
    private val firstName: State<ContactTextFieldState> = _firstName

    private val _lastName = mutableStateOf(ContactTextFieldState(hint = "Enter last name"))
    val lastName: State<ContactTextFieldState> = _lastName

    private val _phoneNumber = mutableStateOf(ContactTextFieldState(hint = "Enter phone number"))
    val phoneNumber: State<ContactTextFieldState> = _phoneNumber

    private val _email = mutableStateOf(ContactTextFieldState(hint = "Enter email"))
    val email: State<ContactTextFieldState> = _email

    private val _notes = mutableStateOf(ContactTextFieldState(hint = "Enter notes (optional)"))
    val notes: State<ContactTextFieldState> = _notes

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentContactId: Int? = null

    init {
        savedStateHandle.get<Int>("contactId")?.let { contactId ->
            if (contactId != -1) {
                viewModelScope.launch {
                    useCases.getContact(contactId)?.also { contact ->
                        currentContactId = contact.id
                        _firstName.value = firstName.value.copy(
                            text = contact.firstName,
                            isHintVisible = false
                        )

                        _lastName.value = lastName.value.copy(
                            text = contact.lastName,
                            isHintVisible = false
                        )

                        _phoneNumber.value = phoneNumber.value.copy(
                            text = contact.phone ?: "",
                            isHintVisible = false
                        )

                        _email.value = email.value.copy(
                            text = contact.email ?: "",
                            isHintVisible = false
                        )

                        _notes.value = notes.value.copy(
                            text = contact.notes ?: "",
                            isHintVisible = false
                        )
                    }
                }
            }
        }

        fun onEvent(event: AddEditContactEvent) {
            when (event) {
                is AddEditContactEvent.EnteredFirstName -> {
                    _firstName.value = firstName.value.copy(
                        text = event.value
                    )
                }

                is AddEditContactEvent.ChangedFirstNameFocus -> {
                    _firstName.value = firstName.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                firstName.value.text.isBlank()
                    )
                }

                is AddEditContactEvent.EnteredLastName -> {
                    _lastName.value = lastName.value.copy(
                        text = event.value
                    )
                }

                is AddEditContactEvent.ChangedLastNameFocus -> {
                    _lastName.value = lastName.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                lastName.value.text.isBlank()
                    )
                }

                is AddEditContactEvent.EnteredPhoneNumber -> {
                    _phoneNumber.value = phoneNumber.value.copy(
                        text = event.value
                    )
                }

                is AddEditContactEvent.ChangedPhoneNumberFocus -> {
                    _phoneNumber.value = phoneNumber.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                phoneNumber.value.text.isBlank()
                    )
                }

                is AddEditContactEvent.EnteredEmail -> {
                    _email.value = email.value.copy(
                        text = event.value
                    )
                }

                is AddEditContactEvent.ChangedEmailFocus -> {
                    _email.value = email.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                email.value.text.isBlank()
                    )
                }

                is AddEditContactEvent.EnteredNotes -> {
                    _notes.value = notes.value.copy(
                        text = event.value
                    )
                }

                is AddEditContactEvent.ChangedNotesFocus -> {
                    _notes.value = notes.value.copy(
                        isHintVisible = !event.focusState.isFocused &&
                                notes.value.text.isBlank()
                    )
                }

                is AddEditContactEvent.SaveContact -> {
                    viewModelScope.launch {
                        try {
                            useCases.addContact(
                                Contact(
                                    firstName = firstName.value.text,
                                    lastName = lastName.value.text,
                                    phone = phoneNumber.value.text,
                                    email = email.value.text,
                                    notes = notes.value.text
                                )
                            )
                            _eventFlow.emit(UiEvent.SaveContactSuccess)
                        } catch (e: InvalidContactException) {
                            _eventFlow.emit(
                                UiEvent.ShowErrorSnackbar(
                                    message = e.message ?: "Couldn't save contact"
                                )
                            )
                        }
                    }

                }
            }
        }


    }


    sealed class UiEvent {
        data class ShowErrorSnackbar(val message: String) : UiEvent()
        object SaveContactSuccess : UiEvent()
    }
}
