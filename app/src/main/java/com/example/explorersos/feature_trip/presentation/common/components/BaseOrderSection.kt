package com.example.explorersos.feature_trip.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.presentation.trips.components.DefaultRadioButton

@Composable
fun <T : Any> BaseOrderSection(
    modifier: Modifier = Modifier,
    orderOptions: List<Pair<String, T>>, // List of ("Label", EnumValue)
    selectedOrder: T,
    onOrderOptionClick: (T) -> Unit,
    orderType: OrderType,
    onOrderTypeClick: (OrderType) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            orderOptions.forEach { (label, orderValue) ->
                DefaultRadioButton(
                    text = label,
                    selected = selectedOrder::class == orderValue::class,
                    onSelect = { onOrderOptionClick(orderValue) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color.Gray)

        Row(modifier = Modifier.fillMaxWidth()) {
            DefaultRadioButton(
                text = "Ascending",
                selected = orderType is OrderType.Ascending,
                onSelect = { onOrderTypeClick(OrderType.Ascending) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = orderType is OrderType.Descending,
                onSelect = { onOrderTypeClick(OrderType.Descending) }
            )
        }
    }
}