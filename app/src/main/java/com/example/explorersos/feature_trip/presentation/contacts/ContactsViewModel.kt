package com.example.explorersos.feature_trip.presentation.contacts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Contact
import com.example.explorersos.feature_trip.domain.use_case.contact.ContactUseCases
import com.example.explorersos.feature_trip.domain.util.ContactOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ContactsViewModel(private val contactUseCases: ContactUseCases) : ViewModel() {
    private val _state = mutableStateOf(ContactsState())
    val state: State<ContactsState> = _state

    private var recentlyDeletedContact: Contact? = null
    private var getContactsJob: Job? = null

    private val _eventflow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventflow.asSharedFlow()

    init {
        getContacts(ContactOrder.LastName(OrderType.Ascending))

    }

    private fun onEvent(event: ContactsEvent) {
        when (event) {
            is ContactsEvent.Order -> {
                if (state.value.contactOrder::class == event.contactOrder::class &&
                    state.value.contactOrder.orderType == event.contactOrder.orderType
                ) {
                    return
                }
                getContacts(event.contactOrder)
            }

            is ContactsEvent.DeleteContact -> {
                viewModelScope.launch {
                    contactUseCases.deleteContact(event.contact)
                    recentlyDeletedContact = event.contact
                    _eventflow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Contact deleted", "Undo"
                        )
                    )
                }
            }

            is ContactsEvent.RestoreContact -> {
                viewModelScope.launch {
                    contactUseCases.addContact(recentlyDeletedContact ?: return@launch)
                    recentlyDeletedContact = null
                }
            }

            is ContactsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String, val action: String? = null) : UiEvent()
    }

    private fun getContacts(order: ContactOrder) {
        getContactsJob?.cancel()
        getContactsJob = contactUseCases.getContacts(order)
            .onEach { contacts ->
                _state.value = state.value.copy(
                    contacts = contacts,
                    contactOrder = order
                )
            }.launchIn(viewModelScope)
    }
}

