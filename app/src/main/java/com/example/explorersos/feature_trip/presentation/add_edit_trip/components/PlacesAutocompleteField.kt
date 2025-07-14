package com.example.explorersos.feature_trip.presentation.add_edit_trip.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.AutocompletePrediction

@Composable
fun PlacesAutocompleteTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String,
    isHintVisible: Boolean,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = true,
    predictions: List<AutocompletePrediction>,
    onPredictionSelected: (AutocompletePrediction) -> Unit
) {
    var hasFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        TransparentHintTextField(
            text = text,
            hint = hint,
            isHintVisible = isHintVisible,
            onValueChange = onValueChange,
            onFocusChange = {
                onFocusChange(it)
                hasFocus = it.isFocused
            },
            textStyle = textStyle,
            singleLine = singleLine
        )

        if (predictions.isNotEmpty() && hasFocus) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                items(predictions) { prediction ->
                    Text(
                        text = prediction.getFullText(null).toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onPredictionSelected(prediction)
                            }
                            .padding(16.dp)
                    )
                    HorizontalDivider(color = Color.LightGray)
                }
            }
        }
    }
}