package com.example.explorersos.feature_trip.presentation.alerts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_trip.domain.util.AlertOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.presentation.trips.components.DefaultRadioButton

@Composable
fun AlertOrderSection(
    modifier: Modifier = Modifier,
    alertOrder: AlertOrder = AlertOrder.Date(OrderType.Ascending),
    onOrderChange: (AlertOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sorting by Date Created")
        }
        Modifier.padding(vertical = 8.dp)
        HorizontalDivider(modifier, 1.dp, Color.Black)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = alertOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(alertOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = alertOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(alertOrder.copy(OrderType.Descending)) }
            )
        }
    }
}