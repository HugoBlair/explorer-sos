package com.example.explorersos.feature_note.domain.util

sealed class TripOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : TripOrder(orderType)
    class Date(orderType: OrderType) : TripOrder(orderType)

    fun copy(orderType: OrderType): TripOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }

    }
}