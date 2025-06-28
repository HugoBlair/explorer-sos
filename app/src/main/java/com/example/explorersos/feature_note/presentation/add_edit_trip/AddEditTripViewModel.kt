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

    // Start Date
    private val _tripStartDate =
        mutableStateOf(TripTextFieldState(hint = "Enter your trip's start date"))
    val tripStartDate: State<TripTextFieldState> = _tripStartDate

    // Expected End Date
    private val _tripExpectedEndDate =
        mutableStateOf(TripTextFieldState(hint = "Enter your trip's expected end date"))
    val tripExpectedEndDate: State<TripTextFieldState> = _tripExpectedEndDate

    // Start Time
    private val _tripStartTime =
        mutableStateOf(TripTextFieldState(hint = "Enter your trip's start time"))
    val tripStartTime: State<TripTextFieldState> = _tripStartTime

    // Expected End Time
    private val _tripExpectedEndTime =
        mutableStateOf(TripTextFieldState(hint = "Enter your trip's expected end time"))
    val tripExpectedEndTime: State<TripTextFieldState> = _tripExpectedEndTime

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
                        _tripStartDate.value = tripStartDate.value.copy(
                            text = trip.startDate,
                            isHintVisible = false
                        )
                        _tripExpectedEndDate.value = tripExpectedEndDate.value.copy(
                            text = trip.expectedEndDate,
                            isHintVisible = false
                        )
                        _tripStartTime.value = tripStartTime.value.copy(
                            text = trip.startTime,
                            isHintVisible = false
                        )
                        _tripExpectedEndTime.value = tripExpectedEndTime.value.copy(
                            text = trip.expectedEndTime,
                            isHintVisible = false
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

            is AddEditTripEvent.EnteredStartDate -> {
                _tripStartDate.value = tripStartDate.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeStartDateFocus -> {
                _tripStartDate.value = tripStartDate.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripStartDate.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredExpectedEndDate -> {
                _tripExpectedEndDate.value = tripExpectedEndDate.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeExpectedEndDateFocus -> {
                _tripExpectedEndDate.value = tripExpectedEndDate.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripExpectedEndDate.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredStartTime -> {
                _tripStartTime.value = tripStartTime.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeStartTimeFocus -> {
                _tripStartTime.value = tripStartTime.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripStartTime.value.text.isBlank()
                )
            }

            is AddEditTripEvent.EnteredExpectedEndTime -> {
                _tripExpectedEndTime.value = tripExpectedEndTime.value.copy(
                    text = event.value
                )
            }

            is AddEditTripEvent.ChangeExpectedEndTimeFocus -> {
                _tripExpectedEndTime.value = tripExpectedEndTime.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            tripExpectedEndTime.value.text.isBlank()
                )
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
                                startDate = tripStartDate.value.text,
                                expectedEndDate = tripExpectedEndDate.value.text,
                                startTime = tripStartTime.value.text,
                                expectedEndTime = tripExpectedEndTime.value.text,
                                isActive = isActive.value,
                                description = if (tripDescription.value.text.isNotBlank()) {
                                    tripDescription.value.text
                                } else {
                                    // Use default description from Trip constructor if no custom description
                                    "I am ${if (isActive.value) "currently on" else "planning"}" +
                                            " the \"${tripTitle.value.text}\" trip from ${tripStartLocation.value.text} to $finalEndLocation . " +
                                            "I am planning to leave on ${tripStartDate.value.text}" +
                                            " and return on ${tripExpectedEndDate.value.text} at ${tripExpectedEndTime.value.text}."
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