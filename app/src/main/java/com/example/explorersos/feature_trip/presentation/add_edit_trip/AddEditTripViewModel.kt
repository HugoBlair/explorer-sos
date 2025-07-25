package com.example.explorersos.feature_trip.presentation.add_edit_trip

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.model.Trip.InvalidTripException
import com.example.explorersos.feature_trip.domain.use_case.trip.TripUseCases
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AddEditTripViewModel(
    private val tripUseCases: TripUseCases,
    private val placesClient: PlacesClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val token = AutocompleteSessionToken.newInstance()


    private var currentTripId: Int? = null
    private var initialTrip: Trip? = null // To store the original state for comparison

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

    // Autocomplete
    private val _startLocationQuery = MutableStateFlow("")
    private val _startLocationPredictions =
        mutableStateOf<List<AutocompletePrediction>>(emptyList())
    val startLocationPredictions: State<List<AutocompletePrediction>> = _startLocationPredictions

    private val _endLocationQuery = MutableStateFlow("")
    private val _endLocationPredictions = mutableStateOf<List<AutocompletePrediction>>(emptyList())
    val endLocationPredictions: State<List<AutocompletePrediction>> = _endLocationPredictions


    // Trip Status (Active/Inactive)
    private val _isActive = mutableStateOf(false)
    val isActive: State<Boolean> = _isActive

    // Round Trip Toggle (UI helper)
    private val _isRoundTrip = mutableStateOf(true)
    val isRoundTrip: State<Boolean> = _isRoundTrip

    // New property to track unsaved changes
    private val _hasUnsavedChanges = mutableStateOf(false)
    val hasUnsavedChanges: State<Boolean> = _hasUnsavedChanges

    // Event flow for UI events
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // This sealed class is now simpler
    sealed class UiEvent {
        data class ShowErrorSnackbar(val message: String) : UiEvent()
        data class SaveTripSuccess(val message: String) : UiEvent()
        data class DeleteTripSuccess(val message: String) : UiEvent()
    }

    init {
        savedStateHandle.get<Int>("tripId")?.let { tripId ->
            if (tripId != -1) {
                currentTripId = tripId // Store the ID
                viewModelScope.launch {
                    tripUseCases.getTrip(tripId)?.also { trip ->
                        initialTrip = trip // Store the original trip state
                        _tripTitle.value = tripTitle.value.copy(
                            text = trip.title,
                            isHintVisible = false
                        )
                        _tripDescription.value = tripDescription.value.copy(
                            text = trip.description,
                            isHintVisible = trip.description.isBlank()
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
        observeLocationQueries()
    }

    private fun observeLocationQueries() {
        viewModelScope.launch {
            _startLocationQuery
                .debounce(300L)
                .distinctUntilChanged()
                .collect { query ->
                    searchPlaces(query, _startLocationPredictions)
                }
        }
        viewModelScope.launch {
            _endLocationQuery
                .debounce(300L)
                .distinctUntilChanged()
                .collect { query ->
                    searchPlaces(query, _endLocationPredictions)
                }
        }
    }

    private fun searchPlaces(
        query: String,
        predictionsState: MutableState<List<AutocompletePrediction>>
    ) {
        if (query.length < 3) {
            predictionsState.value = emptyList()
            return
        }
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            predictionsState.value = response.autocompletePredictions
        }.addOnFailureListener {
            predictionsState.value = emptyList()
        }
    }

    private fun checkForUnsavedChanges() {
        val initial = initialTrip

        // If it's a new trip, changes exist if any key field is filled.
        if (initial == null) {
            _hasUnsavedChanges.value = tripTitle.value.text.isNotBlank() ||
                    tripStartLocation.value.text.isNotBlank() ||
                    tripDescription.value.text.isNotBlank()
            return
        }

        // If it's an existing trip, compare current state to initial state.
        val titleChanged = initial.title != tripTitle.value.text
        val descriptionChanged = initial.description != tripDescription.value.text
        val startLocationChanged = initial.startLocation != tripStartLocation.value.text

        val endLocationChanged = if (isRoundTrip.value) {
            initial.startLocation != initial.endLocation // It changed if it was NOT a round trip before.
        } else {
            initial.endLocation != tripEndLocation.value.text || initial.startLocation == initial.endLocation // It changed if it was a round trip or text is different.
        }

        val startDateTimeChanged = initial.startDateTime != tripStartDateTime.value.selectedDateTime
        val endDateTimeChanged =
            initial.expectedEndDateTime != tripEndDateTime.value.selectedDateTime

        _hasUnsavedChanges.value = titleChanged || descriptionChanged || startLocationChanged ||
                endLocationChanged || startDateTimeChanged || endDateTimeChanged
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
                _tripStartLocation.value = _tripStartLocation.value.copy(text = event.value)
                _startLocationQuery.value = event.value
            }

            is AddEditTripEvent.ChangeStartLocationFocus -> {
                _tripStartLocation.value = _tripStartLocation.value.copy(
                    isHintVisible = !event.focusState.isFocused && _tripStartLocation.value.text.isBlank()
                )
                if (!event.focusState.isFocused) {
                    _startLocationPredictions.value = emptyList()
                }
            }

            is AddEditTripEvent.SelectStartLocation -> {
                _tripStartLocation.value = _tripStartLocation.value.copy(
                    text = event.prediction.getPrimaryText(null).toString(),
                    isHintVisible = false
                )
                _startLocationPredictions.value = emptyList()
            }

            is AddEditTripEvent.EnteredEndLocation -> {
                _tripEndLocation.value = _tripEndLocation.value.copy(text = event.value)
                _endLocationQuery.value = event.value
            }

            is AddEditTripEvent.ChangeEndLocationFocus -> {
                _tripEndLocation.value = _tripEndLocation.value.copy(
                    isHintVisible = !event.focusState.isFocused && _tripEndLocation.value.text.isBlank()
                )
                if (!event.focusState.isFocused) {
                    _endLocationPredictions.value = emptyList()
                }
            }

            is AddEditTripEvent.SelectEndLocation -> {
                _tripEndLocation.value = _tripEndLocation.value.copy(
                    text = event.prediction.getPrimaryText(null).toString(),
                    isHintVisible = false
                )
                _endLocationPredictions.value = emptyList()
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
                        val finalEndLocation =
                            if (isRoundTrip.value) _tripStartLocation.value.text else _tripEndLocation.value.text
                        tripUseCases.addTrip(
                            Trip(
                                id = currentTripId,
                                title = tripTitle.value.text,
                                startLocation = tripStartLocation.value.text,
                                endLocation = finalEndLocation,
                                startDateTime = tripStartDateTime.value.selectedDateTime!!,
                                expectedEndDateTime = tripEndDateTime.value.selectedDateTime!!,
                                isActive = isActive.value,
                                description = if (tripDescription.value.text.isNotBlank()) tripDescription.value.text else ""
                            )
                        )
                        val message = if (isActive.value) "Trip started!" else "Trip saved!"
                        _eventFlow.emit(UiEvent.SaveTripSuccess(message)) // Emit success with a message
                    } catch (e: InvalidTripException) {
                        _eventFlow.emit(
                            UiEvent.ShowErrorSnackbar(
                                message = e.message ?: "Couldn't save trip"
                            )
                        )
                    } catch (e: NullPointerException) {
                        _eventFlow.emit(UiEvent.ShowErrorSnackbar(message = "Please select an end date for your trip."))
                    }
                }

            }

            is AddEditTripEvent.DeleteTrip -> {
                viewModelScope.launch {
                    val tripIdToDelete = currentTripId ?: return@launch
                    tripUseCases.getTrip(tripIdToDelete)?.let { tripToDelete ->
                        tripUseCases.deleteTrip(tripToDelete)
                        _eventFlow.emit(UiEvent.DeleteTripSuccess("Trip deleted"))
                    }
                }
            }
        }
        // Check for changes after every event.
        checkForUnsavedChanges()
    }
}