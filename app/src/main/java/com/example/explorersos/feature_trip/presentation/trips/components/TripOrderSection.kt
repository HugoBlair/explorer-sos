package com.example.explorersos.feature_trip.presentation.trips.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.explorersos.feature_trip.domain.util.TripOrder
import com.example.explorersos.feature_trip.presentation.common.components.BaseOrderSection


@Composable
fun TripOrderSection(
    modifier: Modifier = Modifier,
    tripOrder: TripOrder,
    onOrderChange: (TripOrder) -> Unit
) {
    BaseOrderSection(
        modifier = modifier,
        orderOptions = listOf(
            "Title" to TripOrder.Title(tripOrder.orderType),
            "Date" to TripOrder.Date(tripOrder.orderType)
        ),
        selectedOrder = tripOrder,
        onOrderOptionClick = { newOrder -> onOrderChange(newOrder) },
        orderType = tripOrder.orderType,
        onOrderTypeClick = { newOrderType -> onOrderChange(tripOrder.copy(newOrderType)) }
    )
}