// hugoblair-explorer-sos/app/src/main/java/com/example/explorersos/feature_trip/presentation/add_edit_contact/AddEditContactScreen.kt
package com.example.explorersos.feature_trip.presentation.add_edit_contact

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_trip.presentation.add_edit_trip.components.TransparentHintTextField
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    navController: NavController,
    viewModel: AddEditContactViewModel = koinViewModel(),
    contactId: Int = -1
) {
    val firstNameState = viewModel.firstName.value
    val lastNameState = viewModel.lastName.value
    val emailState = viewModel.email.value
    val phoneState = viewModel.phoneNumber.value
    val notesState = viewModel.notes.value
    val hasUnsavedChanges by viewModel.hasUnsavedChanges

    val snackbarHostState = remember { SnackbarHostState() }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val onBackPress: () -> Unit = {
        if (hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            navController.navigateUp()
        }
    }

    BackHandler(enabled = true, onBack = onBackPress)

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text("Discard Changes?") },
            text = { Text("If you go back, your changes will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDiscardDialog = false
                        navController.navigateUp()
                    }
                ) { Text("Discard") }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Contact?") },
            text = { Text("Are you sure you want to permanently delete this contact?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onEvent(AddEditContactEvent.DeleteContact)
                        showDeleteDialog = false
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditContactViewModel.UiEvent.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                is AddEditContactViewModel.UiEvent.SaveContactSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", event.message)
                    navController.navigateUp()
                }

                is AddEditContactViewModel.UiEvent.DeleteContactSuccess -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", event.message)
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contactId == -1) "New Contact" else "Edit Contact") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (contactId != -1) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Contact")
                        }
                    }
                }
            )
        },
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