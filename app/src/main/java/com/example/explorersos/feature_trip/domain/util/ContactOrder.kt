package com.example.explorersos.feature_trip.domain.util

sealed class ContactOrder(val orderType: OrderType) {
    class FirstName(orderType: OrderType) : ContactOrder(orderType)
    class LastName(orderType: OrderType) : ContactOrder(orderType)

    fun copy(orderType: OrderType): ContactOrder {
        return when (this) {
            is FirstName -> FirstName(orderType)
            is LastName -> LastName(orderType)
        }

    }
}