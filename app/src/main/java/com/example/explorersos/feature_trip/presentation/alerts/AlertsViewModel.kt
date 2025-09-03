package com.example.explorersos.feature_trip.presentation.alerts

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.use_case.alert.AlertUseCases
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AlertsViewModel(private val alertUseCases: AlertUseCases) : ViewModel() {
    private val _state = mutableStateOf(AlertsState())
    val state: State<AlertsState> = _state

    private var recentlyDeletedAlert: Alert? = null
    private var getAlertsJob: Job? = null

    private val _eventflow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventflow.asSharedFlow()

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
                    _eventflow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Alert deleted", "Undo"
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


    sealed class UiEvent {
        data class ShowSnackbar(val message: String, val action: String? = null) : UiEvent()
    }

    private fun getAlerts(order: AlertOrder) {
        getAlertsJob?.cancel()
        getAlertsJob = alertUseCases.getAlerts(order)
            .onEach { alerts ->
                _state.value = state.value.copy(
                    alerts = alerts,
                    alertOrder = order
                )
            }.launchIn(viewModelScope)
    }
}