package com.example.explorersos.feature_trip.presentation.alerts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.use_case.alert.AlertUseCases
import com.example.explorersos.feature_trip.domain.use_case.contact.ContactUseCases
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AlertsViewModel(
    private val alertUseCases: AlertUseCases,
    private val contactUseCases: ContactUseCases
) : ViewModel() {
    private val _state = mutableStateOf(AlertsState())
    val state: State<AlertsState> = _state

    private var recentlyDeletedAlert: Alert? = null
    private var getAlertsJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getAlerts(AlertOrder.Date(OrderType.Ascending))
    }

    fun onEvent(event: AlertsEvent) {
        when (event) {
            is AlertsEvent.Order -> {
                if (state.value.alertOrder::class == event.alertOrder::class &&
                    state.value.alertOrder.orderType == event.alertOrder.orderType
                ) {
                    return
                }
                getAlerts(event.alertOrder)
            }

            is AlertsEvent.DeleteAlert -> {
                viewModelScope.launch {
                    alertUseCases.deleteAlert(event.alert)
                    recentlyDeletedAlert = event.alert
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Alert deleted", action = "Undo"
                        )
                    )
                }
            }

            is AlertsEvent.RestoreAlert -> {
                viewModelScope.launch {
                    alertUseCases.addAlert(recentlyDeletedAlert ?: return@launch)
                    recentlyDeletedAlert = null
                }
            }

            is AlertsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getAlerts(order: AlertOrder) {
        getAlertsJob?.cancel()
        getAlertsJob = alertUseCases.getAlerts(order)
            .combine(contactUseCases.getContacts()) { alerts, contacts ->
                val contactsById = contacts.associateBy { it.id }
                alerts.map { alert ->
                    val recipients = alert.recipientsId.mapNotNull { id ->
                        contactsById[id]
                    }
                    alert to recipients
                }
            }
            .onEach { alertsWithRecipients ->
                _state.value = state.value.copy(
                    alerts = alertsWithRecipients.map { it.first },
                    alertsWithRecipients = alertsWithRecipients,
                    alertOrder = order
                )
            }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String, val action: String? = null) : UiEvent()
    }
}