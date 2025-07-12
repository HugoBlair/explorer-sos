package com.example.explorersos.feature_trip.domain.util

sealed class OrderType {
    object Ascending : OrderType()
    object Descending : OrderType()
}