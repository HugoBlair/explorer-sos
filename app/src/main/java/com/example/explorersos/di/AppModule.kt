package com.example.explorersos.di

import androidx.room.Room
import com.example.explorersos.feature_trip.data.data_source.TripDao
import com.example.explorersos.feature_trip.data.data_source.TripDatabase
import com.example.explorersos.feature_trip.data.repository.TripRepositoryImpl
import com.example.explorersos.feature_trip.domain.repository.TripRepository
import com.example.explorersos.feature_trip.domain.use_case.trip.AddTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.DeleteTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.GetTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.GetTrips
import com.example.explorersos.feature_trip.domain.use_case.trip.TripUseCases
import com.example.explorersos.feature_trip.presentation.add_edit_trip.AddEditTripViewModel
import com.example.explorersos.feature_trip.presentation.trips.TripsViewModel
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Places.createClient(androidApplication()) }

    single<TripDatabase> {
        Room.databaseBuilder(
            androidApplication(),
            TripDatabase::class.java,
            TripDatabase.DATABASE_NAME
        ).build()
    }

    single<TripDao> {
        get<TripDatabase>().tripDao
    }

    single<TripRepository> { TripRepositoryImpl(get<TripDao>()) }

    viewModel<TripsViewModel> { TripsViewModel(get()) }

    viewModel<AddEditTripViewModel> { AddEditTripViewModel(get(), get(), get()) }

    single<TripUseCases> {
        TripUseCases(
            getTrips = GetTrips(get<TripRepository>()),
            deleteTrip = DeleteTrip(get<TripRepository>()),
            addTrip = AddTrip(get<TripRepository>()),
            getTrip = GetTrip(get<TripRepository>())
        )
    }
}