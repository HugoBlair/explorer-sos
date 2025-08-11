package com.example.explorersos.feature_trip.presentation.trips.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.domain.util.TripOrder

@Composable
fun TripOrderSection(
    modifier: Modifier = Modifier,
    tripOrder: TripOrder = TripOrder.Date(OrderType.Descending),
    onOrderChange: (TripOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Title",
                selected = tripOrder is TripOrder.Title,
                onSelect = {
                    onOrderChange(TripOrder.Title(tripOrder.orderType))
                })
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = tripOrder is TripOrder.Date,
                onSelect = {
                    onOrderChange(TripOrder.Title(tripOrder.orderType))

                })
        }
        HorizontalDivider(modifier, 1.dp, Color.Black)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = tripOrder.orderType is OrderType.Ascending,
                onSelect = {
                    onOrderChange(tripOrder.copy(OrderType.Ascending))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = tripOrder.orderType is OrderType.Descending,
                onSelect = {
                    onOrderChange(tripOrder.copy(OrderType.Descending))
                }
            )
        }
    }
}
