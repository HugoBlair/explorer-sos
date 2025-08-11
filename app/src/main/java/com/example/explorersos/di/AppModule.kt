package com.example.explorersos.di

import androidx.room.Room
import com.example.explorersos.feature_trip.data.data_source.AlertDao
import com.example.explorersos.feature_trip.data.data_source.ContactDao
import com.example.explorersos.feature_trip.data.data_source.TripDao
import com.example.explorersos.feature_trip.data.data_source.TripDatabase
import com.example.explorersos.feature_trip.data.repository.AlertRepositoryImpl
import com.example.explorersos.feature_trip.data.repository.ContactRepositoryImpl
import com.example.explorersos.feature_trip.data.repository.TripRepositoryImpl
import com.example.explorersos.feature_trip.domain.repository.AlertRepository
import com.example.explorersos.feature_trip.domain.repository.ContactRepository
import com.example.explorersos.feature_trip.domain.repository.TripRepository
import com.example.explorersos.feature_trip.domain.use_case.alert.AddAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.AlertUseCases
import com.example.explorersos.feature_trip.domain.use_case.alert.DeleteAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.GetAlert
import com.example.explorersos.feature_trip.domain.use_case.alert.GetAlerts
import com.example.explorersos.feature_trip.domain.use_case.contact.AddContact
import com.example.explorersos.feature_trip.domain.use_case.contact.ContactUseCases
import com.example.explorersos.feature_trip.domain.use_case.contact.DeleteContact
import com.example.explorersos.feature_trip.domain.use_case.contact.GetContact
import com.example.explorersos.feature_trip.domain.use_case.contact.GetContacts
import com.example.explorersos.feature_trip.domain.use_case.trip.AddTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.DeleteTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.GetTrip
import com.example.explorersos.feature_trip.domain.use_case.trip.GetTrips
import com.example.explorersos.feature_trip.domain.use_case.trip.TripUseCases
import com.example.explorersos.feature_trip.presentation.add_edit_contact.AddEditContactViewModel
import com.example.explorersos.feature_trip.presentation.add_edit_trip.AddEditTripViewModel
import com.example.explorersos.feature_trip.presentation.contacts.ContactsViewModel
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

    }

    viewModel<AddEditTripViewModel> { AddEditTripViewModel(get(), get(), get()) }
    viewModel<AddEditContactViewModel> { AddEditContactViewModel(get(), get()) }


    /**
     * Trip data/model related dependencies
     */
    viewModel<TripsViewModel> { TripsViewModel(get()) }

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
     * Contact data/model related dependencies
     */
    viewModel<ContactsViewModel> { ContactsViewModel(get()) }


    single<ContactDao> {
        get<TripDatabase>().contactDao
    }

    single<ContactRepository> { ContactRepositoryImpl(get<ContactDao>()) }

    single<ContactUseCases> {
        ContactUseCases(
            getContacts = GetContacts(get<ContactRepository>()),
            deleteContact = DeleteContact(get<ContactRepository>()),
            addContact = AddContact(get<ContactRepository>()),
            getContact = GetContact(get<ContactRepository>())
        )
    }


}