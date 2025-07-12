import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * A composable that allows the user to pick a date and time, and returns the result
 * as a Unix timestamp (Long).
 *
 * @param initialTimestamp The initial timestamp to display in the pickers.
 * @param onTimestampSelected Callback invoked when a new date and time is selected,
 * providing the result as a Long (epoch milliseconds).
 * @param content The composable content that, when interacted with, should trigger the picker.
 * It receives a lambda to launch the picker dialog.
 */
@Composable
fun DateTimePicker(
    initialTimestamp: Long,
    onTimestampSelected: (Long) -> Unit,
    content: @Composable (launchPicker: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // The content provided by the caller, with a lambda to trigger the dialog
    content {
        showDialog = true
    }

    if (showDialog) {
        // We need to show the picker in the user's local time, so convert the
        // initial UTC timestamp to a ZonedDateTime in the system's default timezone.
        val initialInstant = Instant.ofEpochMilli(initialTimestamp)
        val localInitialDateTime = ZonedDateTime.ofInstant(initialInstant, ZoneId.systemDefault())

        // --- Date Picker ---
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // --- Time Picker ---
                // Month is 0-based in the picker, but 1-based in java.time
                val selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0)

                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        // Combine date and time
                        val finalDateTime = selectedDate.withHour(hourOfDay).withMinute(minute)

                        // Convert the selected local date/time back to a UTC Instant,
                        // then to a Unix timestamp.
                        val zonedDateTime = finalDateTime.atZone(ZoneId.systemDefault())
                        val newTimestamp = zonedDateTime.toInstant().toEpochMilli()

                        onTimestampSelected(newTimestamp)
                        showDialog = false // Close dialog
                    },
                    localInitialDateTime.hour,
                    localInitialDateTime.minute,
                    true // 24-hour format
                )
                timePickerDialog.setOnCancelListener { showDialog = false }
                timePickerDialog.show()
            },
            localInitialDateTime.year,
            localInitialDateTime.monthValue - 1, // Month is 0-based
            localInitialDateTime.dayOfMonth
        )
        datePickerDialog.setOnCancelListener { showDialog = false }
        datePickerDialog.show()
    }
}