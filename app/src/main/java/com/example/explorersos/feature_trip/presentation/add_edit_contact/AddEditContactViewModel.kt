// hugoblair-explorer-sos/app/src/main/java/com/example/explorersos/feature_trip/presentation/add_edit_contact/AddEditContactViewModel.kt
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
    val firstName: State<ContactTextFieldState> = _firstName

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
    private var initialContact: Contact? = null

    private val _hasUnsavedChanges = mutableStateOf(false)
    val hasUnsavedChanges: State<Boolean> = _hasUnsavedChanges

    init {
        savedStateHandle.get<Int>("contactId")?.let { contactId ->
            if (contactId != -1) {
                currentContactId = contactId
                viewModelScope.launch {
                    useCases.getContact(contactId)?.also { contact ->
                        initialContact = contact
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
                            isHintVisible = contact.phone.isNullOrBlank()
                        )
                        _email.value = email.value.copy(
                            text = contact.email ?: "",
                            isHintVisible = contact.email.isNullOrBlank()
                        )
                        _notes.value = notes.value.copy(
                            text = contact.notes ?: "",
                            isHintVisible = contact.notes.isNullOrBlank()
                        )
                    }
                }
            }
        }
    }

    private fun checkForUnsavedChanges() {
        val initial = initialContact
        if (initial == null) {
            _hasUnsavedChanges.value = firstName.value.text.isNotBlank() ||
                    lastName.value.text.isNotBlank() ||
                    phoneNumber.value.text.isNotBlank() ||
                    email.value.text.isNotBlank() ||
                    notes.value.text.isNotBlank()
            return
        }

        val firstNameChanged = initial.firstName != firstName.value.text
        val lastNameChanged = initial.lastName != lastName.value.text
        val phoneChanged = initial.phone != phoneNumber.value.text
        val emailChanged = initial.email != email.value.text
        val notesChanged = initial.notes != notes.value.text

        _hasUnsavedChanges.value =
            firstNameChanged || lastNameChanged || phoneChanged || emailChanged || notesChanged
    }

    fun onEvent(event: AddEditContactEvent) {
        when (event) {
            is AddEditContactEvent.EnteredFirstName -> _firstName.value =
                firstName.value.copy(text = event.value)

            is AddEditContactEvent.ChangeFirstNameFocus -> _firstName.value =
                firstName.value.copy(isHintVisible = !event.focusState.isFocused && firstName.value.text.isBlank())

            is AddEditContactEvent.EnteredLastName -> _lastName.value =
                lastName.value.copy(text = event.value)

            is AddEditContactEvent.ChangeLastNameFocus -> _lastName.value =
                lastName.value.copy(isHintVisible = !event.focusState.isFocused && lastName.value.text.isBlank())

            is AddEditContactEvent.EnteredPhoneNumber -> _phoneNumber.value =
                phoneNumber.value.copy(text = event.value)

            is AddEditContactEvent.ChangePhoneNumberFocus -> _phoneNumber.value =
                phoneNumber.value.copy(isHintVisible = !event.focusState.isFocused && phoneNumber.value.text.isBlank())

            is AddEditContactEvent.EnteredEmail -> _email.value =
                email.value.copy(text = event.value)

            is AddEditContactEvent.ChangeEmailFocus -> _email.value =
                email.value.copy(isHintVisible = !event.focusState.isFocused && email.value.text.isBlank())

            is AddEditContactEvent.EnteredNotes -> _notes.value =
                notes.value.copy(text = event.value)

            is AddEditContactEvent.ChangeNotesFocus -> _notes.value =
                notes.value.copy(isHintVisible = !event.focusState.isFocused && notes.value.text.isBlank())

            is AddEditContactEvent.SaveContact -> {
                viewModelScope.launch {
                    try {
                        useCases.addContact(
                            Contact(
                                id = currentContactId,
                                firstName = firstName.value.text,
                                lastName = lastName.value.text,
                                phone = phoneNumber.value.text,
                                email = email.value.text,
                                notes = notes.value.text
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveContactSuccess("Contact saved."))
                    } catch (e: InvalidContactException) {
                        _eventFlow.emit(
                            UiEvent.ShowErrorSnackbar(
                                e.message ?: "Couldn't save contact"
                            )
                        )
                    }
                }
            }

            is AddEditContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    initialContact?.let {
                        useCases.deleteContact(it)
                        _eventFlow.emit(UiEvent.DeleteContactSuccess("Contact deleted."))
                    }
                }
            }
        }
        checkForUnsavedChanges()
    }

    sealed class UiEvent {
        data class ShowErrorSnackbar(val message: String) : UiEvent()
        data class SaveContactSuccess(val message: String) : UiEvent()
        data class DeleteContactSuccess(val message: String) : UiEvent()
    }
}