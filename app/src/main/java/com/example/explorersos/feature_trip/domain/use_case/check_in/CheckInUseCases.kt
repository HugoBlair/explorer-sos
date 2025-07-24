package com.example.explorersos.feature_trip.domain.use_case.check_in

data class CheckInUseCases(
    val addCheckIn: AddCheckIn,
    val deleteCheckIn: DeleteCheckIn,
    val getCheckInsForTrip: GetCheckInsForTrip,
    val updateCheckInStatus: UpdateCheckInStatus,
    val getOverdueCheckIns: GetOverdueCheckIns
)