package com.example.explorersos.feature_trip.presentation.contacts.components

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
import com.example.explorersos.feature_trip.domain.util.ContactOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.presentation.trips.components.DefaultRadioButton

@Composable
fun ContactOrderSection(
    modifier: Modifier = Modifier,
    contactOrder: ContactOrder = ContactOrder.LastName(OrderType.Ascending),
    onOrderChange: (ContactOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "First Name",
                selected = contactOrder is ContactOrder.FirstName,
                onSelect = { onOrderChange(ContactOrder.FirstName(contactOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Last Name",
                selected = contactOrder is ContactOrder.LastName,
                onSelect = { onOrderChange(ContactOrder.LastName(contactOrder.orderType)) }
            )
        }
        Modifier.padding(vertical = 8.dp)
        HorizontalDivider(modifier, 1.dp, Color.Black)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = "Ascending",
                selected = contactOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(contactOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = contactOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(contactOrder.copy(OrderType.Descending)) }
            )
        }
    }
}