package com.example.explorersos.feature_trip.domain.use_case.alert_recipient

import com.example.explorersos.feature_alertrecipient.domain.use_case.alert_recipient.GetAlertRecipients

data class AlertRecipientUseCases(
    val getAlertRecipients: GetAlertRecipients,
    val deleteAlertRecipient: DeleteAlertRecipient,
    val addAlertRecipient: AddAlertRecipient,
    val getAlertRecipient: GetAlertRecipient
)
