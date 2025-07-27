package com.example.explorersos.feature_trip.presentation.add_edit_contact

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_trip.presentation.add_edit_trip.components.TransparentHintTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    navController: NavController,
    viewModel: AddEditContactViewModel = koinViewModel()
) {
    val firstNameState = viewModel.firstName.value
    val lastNameState = viewModel.lastName.value
    val emailState = viewModel.email.value
    val phoneState = viewModel.phoneNumber.value
    val notesState = viewModel.notes.value

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditContactViewModel.UiEvent.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is AddEditContactViewModel.UiEvent.SaveContactSuccess -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditContactEvent.SaveContact) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Contact")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = firstNameState.text,
                hint = firstNameState.hint,
                onValueChange = { viewModel.onEvent(AddEditContactEvent.EnteredFirstName(it)) },
                onFocusChange = { viewModel.onEvent(AddEditContactEvent.ChangeFirstNameFocus(it)) },
                isHintVisible = firstNameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = lastNameState.text,
                hint = lastNameState.hint,
                onValueChange = { viewModel.onEvent(AddEditContactEvent.EnteredLastName(it)) },
                onFocusChange = { viewModel.onEvent(AddEditContactEvent.ChangeLastNameFocus(it)) },
                isHintVisible = lastNameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = emailState.text,
                hint = emailState.hint,
                onValueChange = { viewModel.onEvent(AddEditContactEvent.EnteredEmail(it)) },
                onFocusChange = { viewModel.onEvent(AddEditContactEvent.ChangeEmailFocus(it)) },
                isHintVisible = emailState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = phoneState.text,
                hint = phoneState.hint,
                onValueChange = { viewModel.onEvent(AddEditContactEvent.EnteredPhoneNumber(it)) },
                onFocusChange = { viewModel.onEvent(AddEditContactEvent.ChangePhoneNumberFocus(it)) },
                isHintVisible = phoneState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = notesState.text,
                hint = notesState.hint,
                onValueChange = { viewModel.onEvent(AddEditContactEvent.EnteredNotes(it)) },
                onFocusChange = { viewModel.onEvent(AddEditContactEvent.ChangeNotesFocus(it)) },
                isHintVisible = notesState.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.height(120.dp)
            )
        }
    }
}