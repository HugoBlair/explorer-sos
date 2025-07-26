package com.example.explorersos.di

import androidx.room.Room
import com.example.explorersos.feature_alertrecipient.domain.use_case.alert_recipient.GetAlertRecipients
import com.example.explorersos.feature_trip.data.data_source.AlertDao
import com.example.explorersos.feature_trip.data.data_source.AlertRecipientDao
import com.example.explorersos.feature_trip.data.data_source.TripDao
import com.example.explorersos.feature_trip.data.data_source.TripDatabase
import com.example.explorersos.feature_trip.data.repository.AlertRecipientRepositoryImpl
import com.example.explorersos.feature_trip.data.repository.AlertRepositoryImpl
import com.example.explorersos.feature_trip.data.repository.TripRepositoryImpl
import com.example.explorersos.feature_trip.domain.repository.AlertRecipientRepository
import com.example.explorersos.feature_trip.domain.repository.AlertRepository
import com.example.explorersos.feature_trip.domain.repository.TripRepository
import com.example.explorersos.feature_trip.domain.use_case.alert.AddAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.AlertUseCases
import com.example.explorersos.feature_trip.domain.use_case.alert.DeleteAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.GetAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.GetAlerts
import com.example.explorersos.feature_trip.domain.use_case.alert_recipient.AddAlertRecipient
import com.example.explorersos.feature_trip.domain.use_case.alert_recipient.AlertRecipientUseCases
import com.example.explorersos.feature_trip.domain.use_case.alert_recipient.DeleteAlertRecipient
import com.example.explorersos.feature_trip.domain.use_case.alert_recipient.GetAlertRecipient
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
        ).fallbackToDestructiveMigration(true).build()
        TODO("REMOVE FALLBACK TO DEStructive migration")
    }

    viewModel<TripsViewModel> { TripsViewModel(get()) }

    viewModel<AddEditTripViewModel> { AddEditTripViewModel(get(), get(), get()) }

    /**
     * Trip data/model related dependencies
     */
    single<TripDao> {
        get<TripDatabase>().tripDao
    }

    single<TripRepository> { TripRepositoryImpl(get<TripDao>()) }

    single<TripUseCases> {
        TripUseCases(
            getTrips = GetTrips(get<TripRepository>()),
            deleteTrip = DeleteTrip(get<TripRepository>()),
            addTrip = AddTrip(get<TripRepository>()),
            getTrip = GetTrip(get<TripRepository>())
        )
    }

    /**
     * Alert data/model related dependencies
     */
    single<AlertDao> {
        get<TripDatabase>().alertDao
    }

    single<AlertRepository> { AlertRepositoryImpl(get<AlertDao>()) }

    single<AlertUseCases> {
        AlertUseCases(
            deleteAlert = DeleteAlert(get<AlertRepository>()),
            addAlert = AddAlert(get<AlertRepository>()),
            getAlert = GetAlert(get<AlertRepository>()),
            getAlerts = GetAlerts(get<AlertRepository>())
        )
    }

    /**
     * AlertRecipient data/model related dependencies
     */
    single<AlertRecipientDao> {
        get<TripDatabase>().alertRecipientDao
    }

    single<AlertRecipientRepository> { AlertRecipientRepositoryImpl(get<AlertRecipientDao>()) }

    single<AlertRecipientUseCases> {
        AlertRecipientUseCases(
            getAlertRecipients = GetAlertRecipients(get<AlertRecipientRepository>()),
            deleteAlertRecipient = DeleteAlertRecipient(get<AlertRecipientRepository>()),
            addAlertRecipient = AddAlertRecipient(get<AlertRecipientRepository>()),
            getAlertRecipient = GetAlertRecipient(get<AlertRecipientRepository>())
        )
    }


}