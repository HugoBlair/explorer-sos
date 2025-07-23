package com.example.explorersos.feature_trip.domain.util

sealed class AlertRecipientOrder(val orderType: OrderType) {
    class FirstName(orderType: OrderType) : AlertRecipientOrder(orderType)
    class LastName(orderType: OrderType) : AlertRecipientOrder(orderType)

    fun copy(orderType: OrderType): AlertRecipientOrder {
        return when (this) {
            is FirstName -> FirstName(orderType)
            is LastName -> LastName(orderType)
        }

    }
}