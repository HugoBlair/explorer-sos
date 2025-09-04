package com.example.explorersos.feature_trip.presentation.contacts.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.explorersos.feature_trip.domain.util.ContactOrder
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.presentation.common.components.BaseOrderSection

/**
 * A specific implementation of the order section UI for sorting Contacts.
 * It uses the generic BaseOrderSection to build its UI, providing
 * Contact-specific sorting options (First Name, Last Name).
 *
 * @param modifier Modifier for this composable.
 * @param contactOrder The current sorting state for contacts.
 * @param onOrderChange A callback invoked when the user selects a new sorting option.
 */
@Composable
fun ContactOrderSection(
    modifier: Modifier = Modifier,
    contactOrder: ContactOrder = ContactOrder.LastName(OrderType.Ascending),
    onOrderChange: (ContactOrder) -> Unit
) {
    BaseOrderSection(
        modifier = modifier,
        orderOptions = listOf(
            "First Name" to ContactOrder.FirstName(contactOrder.orderType),
            "Last Name" to ContactOrder.LastName(contactOrder.orderType)
        ),
        selectedOrder = contactOrder,
        onOrderOptionClick = { newOrder -> onOrderChange(newOrder) },
        orderType = contactOrder.orderType,
        onOrderTypeClick = { newOrderType -> onOrderChange(contactOrder.copy(newOrderType)) }
    )
}