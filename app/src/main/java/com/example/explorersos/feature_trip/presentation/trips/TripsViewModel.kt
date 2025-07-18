package com.example.explorersos.feature_trip.presentation.trips

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.use_case.TripUseCases
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.domain.util.TripOrder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TripsViewModel(private val tripUseCases: TripUseCases) : ViewModel() {
    private val _state = mutableStateOf(TripsState())
    val state: State<TripsState> = _state

    private var recentlyDeletedTrip: Trip? = null
    private var recentlyEndedTrip: Trip? = null
    private var getTripsJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getTrips(TripOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TripsEvent) {
        when (event) {
            is TripsEvent.Order -> {
                if (state.value.tripOrder::class == event.tripOrder::class &&
                    state.value.tripOrder.orderType == event.tripOrder.orderType
                ) {
                    return
                }
                getTrips(event.tripOrder)
            }

            is TripsEvent.DeleteTrip -> {
                viewModelScope.launch {
                    recentlyDeletedTrip = event.trip
                    tripUseCases.deleteTrip(event.trip)
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Trip deleted",
                            action = "Undo"
                        )
                    )
                }
            }

            is TripsEvent.RestoreTrip -> {
                viewModelScope.launch {
                    tripUseCases.addTrip(recentlyDeletedTrip ?: return@launch)
                    recentlyDeletedTrip = null
                }
            }

            is TripsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is TripsEvent.EndTrip -> {
                viewModelScope.launch {
                    recentlyEndedTrip = event.trip
                    tripUseCases.addTrip(event.trip.copy(isActive = false))
                    _eventFlow.emit(
                        UiEvent.ShowSnackbar(
                            message = "Trip ended",
                            action = "Undo"
                        )
                    )
                }
            }

            is TripsEvent.RestoreEndedTrip -> {
                viewModelScope.launch {
                    tripUseCases.addTrip(recentlyEndedTrip ?: return@launch)
                    recentlyEndedTrip = null
                }
            }
        }
    }

    private fun getTrips(tripOrder: TripOrder) {
        getTripsJob?.cancel()
        tripUseCases.getTrips(tripOrder).onEach { trips ->
            _state.value = state.value.copy(
                trips = trips,
                tripOrder = tripOrder
            )
        }.launchIn(viewModelScope)

    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String, val action: String? = null) : UiEvent()
    }
}