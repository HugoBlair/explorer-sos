package com.example.explorersos.feature_trip.presentation.alerts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.explorersos.feature_trip.presentation.alerts.components.AlertItem
import com.example.explorersos.feature_trip.presentation.alerts.components.AlertOrderSection
import com.example.explorersos.feature_trip.presentation.navigation.Routes
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    navController: NavController,
    viewModel: AlertsViewModel = koinViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    // Handles snackbars for undoable actions (e.g., delete)
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            if (event is AlertsViewModel.UiEvent.ShowSnackbar) {
                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action,
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(AlertsEvent.RestoreAlert)
                }
            }
        }
    }

    // Handles snackbars for one-off events (e.g., save/delete from edit screen)
    LaunchedEffect(key1 = true) {
        val message = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("snackbar_message")
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("snackbar_message")
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Routes.AddEditAlertScreenRoute()) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Alert")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your Alerts", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { viewModel.onEvent(AlertsEvent.ToggleOrderSection) }) {
                    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                AlertOrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    alertOrder = state.alertOrder,
                    onOrderChange = { viewModel.onEvent(AlertsEvent.Order(it)) }
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.alertsWithRecipients) { (alert, recipients) ->

                    AlertItem(
                        alert = alert,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        onDeleteClick = { viewModel.onEvent(AlertsEvent.DeleteAlert(alert)) },
                        onEditClick = {
                            navController.navigate(
                                Routes.AddEditAlertScreenRoute(alertId = alert.id ?: -1)
                            )
                        },
                        recipients = recipients
                    )


                }

            }

        }
    }
}