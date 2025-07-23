package com.example.explorersos.feature_trip.domain.util

enum class CheckInStatus {
    PENDING, // The check-in is upcoming
    COMPLETED, // The user has successfully checked in
    MISSED // The due time has passed without a check-in
}