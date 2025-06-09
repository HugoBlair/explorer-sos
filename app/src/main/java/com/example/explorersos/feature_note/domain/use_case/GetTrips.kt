package com.example.explorersos.feature_note.domain.use_case

import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.repository.TripRepository
import com.example.explorersos.feature_note.domain.util.OrderType
import com.example.explorersos.feature_note.domain.util.TripOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTrips(private val repository: TripRepository) {
    operator fun invoke(
        tripOrder: TripOrder = TripOrder.Date(OrderType.Descending)
    ): Flow<List<Trip>> {
        return repository.getTrips().map { trips ->
            when (tripOrder.orderType) {
                is OrderType.Ascending -> {
                    when (tripOrder) {
                        is TripOrder.Title -> trips.sortedBy { it.title.lowercase() }
                        is TripOrder.Date -> trips.sortedBy { it.createdAt }
                    }
                }

                is OrderType.Descending -> {
                    when (tripOrder) {
                        is TripOrder.Title -> trips.sortedByDescending { it.title.lowercase() }
                        is TripOrder.Date -> trips.sortedByDescending { it.createdAt }
                    }
                }
            }
        }
    }
}