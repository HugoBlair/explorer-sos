package com.example.explorersos.feature_trip.domain.util


/**
 * Represents the different orders that can be used to sort alerts.
 * @param orderType The type of order (ascending or descending).
 *
 * This is a slightly useless class right now, but it will be useful in the future if I
 * want to add different sorting methods to alerts.
 */
sealed class AlertOrder(val orderType: OrderType) {

    class Date(orderType: OrderType) : AlertOrder(orderType)

    fun copy(orderType: OrderType): AlertOrder {
        return when (this) {
            is Date -> Date(orderType)
        }

    }
}