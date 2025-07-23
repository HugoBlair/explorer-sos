package com.example.explorersos.feature_trip.domain.repository

import com.example.explorersos.feature_trip.domain.model.CheckIn
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * Interface for the repository that handles data operations for CheckIn entities.
 * This acts as the single source of truth for check-in data for the rest of the app.
 */
interface CheckInRepository {

    /**
     * Retrieves a reactive stream of all check-ins for a specific trip.
     * The list is ordered by their due date.
     *
     * @param tripId The ID of the trip whose check-ins are to be fetched.
     * @return A Flow emitting a list of CheckIn objects.
     */
    fun getCheckInsForTrip(tripId: Int): Flow<List<CheckIn>>

    /**
     * Inserts a new check-in into the data source.
     *
     * @param checkIn The CheckIn object to be inserted.
     */
    suspend fun insertCheckIn(checkIn: CheckIn)

    /**
     * Updates an existing check-in in the data source.
     * Useful for changing the status (e.g., from PENDING to COMPLETED).
     *
     * @param checkIn The CheckIn object with updated information.
     */
    suspend fun updateCheckIn(checkIn: CheckIn)

    /**
     * Deletes a check-in from the data source.
     *
     * @param checkIn The CheckIn object to be deleted.
     */
    suspend fun deleteCheckIn(checkIn: CheckIn)

    /**
     * Fetches a list of all check-ins that are currently overdue.
     * This is a one-shot query, not a reactive stream, intended for background workers.
     *
     * @param currentTime The current time to compare against the check-ins' due dates.
     * @return A simple List of CheckIn objects that are overdue.
     */
    suspend fun getOverdueCheckIns(currentTime: Instant): List<CheckIn>
}