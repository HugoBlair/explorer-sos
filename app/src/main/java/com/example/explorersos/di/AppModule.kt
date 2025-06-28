package com.example.explorersos.di

import androidx.room.Room
import com.example.explorersos.feature_note.data.data_source.TripDao
import com.example.explorersos.feature_note.data.data_source.TripDatabase
import com.example.explorersos.feature_note.data.repository.TripRepositoryImpl
import com.example.explorersos.feature_note.domain.repository.TripRepository
import com.example.explorersos.feature_note.domain.use_case.AddTrip
import com.example.explorersos.feature_note.domain.use_case.DeleteTrip
import com.example.explorersos.feature_note.domain.use_case.GetTrip
import com.example.explorersos.feature_note.domain.use_case.GetTrips
import com.example.explorersos.feature_note.domain.use_case.TripUseCases
import com.example.explorersos.feature_note.presentation.add_edit_trip.AddEditTripViewModel
import com.example.explorersos.feature_note.presentation.trips.TripsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

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

    viewModel<AddEditTripViewModel> { AddEditTripViewModel(get(), get()) }

    single<TripUseCases> {
        TripUseCases(
            getTrips = GetTrips(get<TripRepository>()),
            deleteTrip = DeleteTrip(get<TripRepository>()),
            addTrip = AddTrip(get<TripRepository>()),
            getTrip = GetTrip(get<TripRepository>())
        )
    }
}