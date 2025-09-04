package com.example.explorersos.feature_trip.presentation.alerts.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.presentation.common.components.BaseOrderSection

/**
 * A specific implementation of the order section UI for sorting Alerts.
 * It uses the generic BaseOrderSection to build its UI, providing
 * Alert-specific sorting options.
 *
 * @param modifier Modifier for this composable.
 * @param alertOrder The current sorting state for alerts.
 * @param onOrderChange A callback invoked when the user selects a new sorting option.
 */
@Composable
fun AlertOrderSection(
    modifier: Modifier = Modifier,
    alertOrder: AlertOrder = AlertOrder.Date(com.example.explorersos.feature_trip.domain.util.OrderType.Descending),
    onOrderChange: (AlertOrder) -> Unit
) {
    BaseOrderSection(
        modifier = modifier,
        orderOptions = listOf(
            // Currently, AlertOrder only supports sorting by Date.
            // This can be easily expanded in the future by adding more options here.
            "Date" to AlertOrder.Date(alertOrder.orderType)
        ),
        selectedOrder = alertOrder,
        onOrderOptionClick = { newOrder -> onOrderChange(newOrder) },
        orderType = alertOrder.orderType,
        onOrderTypeClick = { newOrderType -> onOrderChange(alertOrder.copy(newOrderType)) }
    )
}