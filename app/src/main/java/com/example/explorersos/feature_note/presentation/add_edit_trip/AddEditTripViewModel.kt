package com.example.explorersos.feature_note.presentation.add_edit_trip

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.model.Trip.InvalidTripException
import com.example.explorersos.feature_note.domain.use_case.TripUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddEditTripViewModel(
    private val tripUseCases: TripUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var currentTripId: Int? = null

    // Title
    private val _tripTitle = mutableStateOf(TripTextFieldState(hint = "Enter title..."))
    val tripTitle: State<TripTextFieldState> = _tripTitle

    // Description
    private val _tripDescription =
        mutableStateOf(TripTextFieldState(hint = "(Optional) Enter a description of your trip"))
    val tripDescription: State<TripTextFieldState> = _tripDescription

    // Start Location
    private val _tripStartLocation =
        mutableStateOf(TripTextFieldState(hint = "Enter your start location"))
    val tripStartLocation: State<TripTextFieldState> = _tripStartLocation

    // End Location
    private val _tripEndLocation =
        mutableStateOf(TripTextFieldState(hint = "Enter your end location"))
    val tripEndLocation: State<TripTextFieldState> = _tripEndLocation

    private var _tripStartDateTime =
        mutableStateOf(TripDateTimePickerState(hint = "Pick your trip's starting date and time"))
    val tripStartDateTime: State<TripDateTimePickerState> = _tripStartDateTime

    private var _tripEndDateTime =
        mutableStateOf(TripDateTimePickerState(hint = "Pick your trip's finishing date and time"))
    val tripEndDateTime: State<TripDateTimePickerState> = _tripEndDateTime


    // Trip Status (Active/Inactive)
    private val _isActive = mutableStateOf(false)
    val isActive: State<Boolean> = _isActive

    // Round Trip Toggle (UI helper)
    private val _isRoundTrip = mutableStateOf(true)
    val isRoundTrip: State<Boolean> = _isRoundTrip

    // Event flow for UI events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveTrip : UiEvent()
    }

    init {
        savedStateHandle.get<Int>("tripId")?.let { tripId ->
            if (tripId != -1) {
                viewModelScope.launch {
                    tripUseCases.getTrip(tripId)?.also { trip ->
                        currentTripId = trip.id
                        _tripTitle.value = tripTitle.value.copy(
                            text = trip.title,
                            isHintVisible = false
                        )
                        _tripDescription.value = tripDescription.value.copy(
                            text = trip.description,
                            isHintVisible = false
                        )
                        _tripStartLocation.value = tripStartLocation.value.copy(
                            text = trip.startLocation,
                            isHintVisible = false
                        )
                        _tripEndLocation.value = tripEndLocation.value.copy(
                            text = trip.endLocation,
                            isHintVisible = false
                        )
                        _tripStartDateTime.value = tripStartDateTime.value.copy(
                            selectedDateTime = trip.startDateTime,
                        )

                        _tripEndDateTime.value = tripEndDateTime.value.copy(
                            selectedDateTime = trip.expectedEndDateTime,
                        )
                        _isActive.value = trip.isActive
                        _isRoundTrip.value = trip.startLocation == trip.endLocation
                    }
                }
            }
        }
    }

    // Events for handling UI interactions
    fun onEvent(event: AddEditTripEvent) {
        when (event) {
            is AddEditTripEvent.EnteredTitle -> {
                _tripTitle.value = tripTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeTitleFocus -> {
                _tripTitle.value = tripTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripTitle.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredDescription -> {
                _tripDescription.value = tripDescription.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeDescriptionFocus -> {
                _tripDescription.value = tripDescription.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripDescription.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredStartLocation -> {
                _tripStartLocation.value = tripStartLocation.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeStartLocationFocus -> {
                _tripStartLocation.value = tripStartLocation.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripStartLocation.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredEndLocation -> {
                _tripEndLocation.value = tripEndLocation.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeEndLocationFocus -> {
                _tripEndLocation.value = tripEndLocation.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripEndLocation.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredStartDateTime -> {
                _tripStartDateTime.value = tripStartDateTime.value.copy(
                    selectedDateTime = event.value
                )
            }

            is AddEditTripEvent.ChangeStartDateTimeFocus -> {
                _tripStartDateTime.value = tripStartDateTime.value.copy(
                    isDateTimePickerVisible = !event.focusState.isFocused &&
                            tripStartDateTime.value.selectedDateTime == null
                )

            }

            is AddEditTripEvent.EnteredEndDateTime -> {
                _tripEndDateTime.value = tripEndDateTime.value.copy(
                    selectedDateTime = event.value
                )
            }

            is AddEditTripEvent.ChangeEndDateTimeFocus -> {
                _tripEndDateTime.value = tripEndDateTime.value.copy(
                    isDateTimePickerVisible = !event.focusState.isFocused &&
                            tripEndDateTime.value.selectedDateTime == null
                )
            }

            is AddEditTripEvent.SetActiveStatus -> {
                _isActive.value = event.value
            }


            is AddEditTripEvent.ToggleActiveStatus -> {
                _isActive.value = !_isActive.value
            }

            is AddEditTripEvent.ToggleRoundTrip -> {
                _isRoundTrip.value = !_isRoundTrip.value
                // For round trip, set end location same as start location
                if (_isRoundTrip.value) {
                    _tripEndLocation.value = tripEndLocation.value.copy(
                        text = tripStartLocation.value.text
                    )
                } else {
                    // Clear end location when switching from round trip
                    _tripEndLocation.value = tripEndLocation.value.copy(text = "")
                }
            }

            is AddEditTripEvent.SaveTrip -> {
                viewModelScope.launch {
                    try {
                        // Determine end location based on round trip toggle
                        val finalEndLocation = if (isRoundTrip.value) {
                            tripStartLocation.value.text
                        } else {
                            tripEndLocation.value.text
                        }

                        tripUseCases.addTrip(
                            Trip(
                                id = currentTripId,
                                title = tripTitle.value.text,
                                startLocation = tripStartLocation.value.text,
                                endLocation = finalEndLocation,
                                startDateTime = tripStartDateTime.value.selectedDateTime!!,
                                expectedEndDateTime = tripEndDateTime.value.selectedDateTime!!,
                                isActive = isActive.value,
                                description = if (tripDescription.value.text.isNotBlank()) {
                                    tripDescription.value.text
                                } else {
                                    ""
                                }
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveTrip)
                    } catch (e: InvalidTripException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save trip"
                            )
                        )
                    }
                }
            }
        }
    }
}