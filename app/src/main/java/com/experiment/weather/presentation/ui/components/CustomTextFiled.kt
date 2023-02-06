package com.experiment.weather.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weather.core.design.theme.WeatherTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    placeHolder: @Composable () -> Unit,
    cursorBrush: Brush = SolidColor(Color.Black),
    singleLine: Boolean,
    maxLine: Int,
) {
//    var value by remember {
//        mutableStateOf("")
//    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier,
        maxLines = maxLine,
        singleLine = singleLine,
        cursorBrush = cursorBrush
    ) { innerTextField ->
        Row(modifier = Modifier
            .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.padding(vertical = 8.dp)) {
                if (value.isBlank())
                    placeHolder()
                innerTextField()
            }
            Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "")

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    var value by remember {
        mutableStateOf("")
    }
    WeatherTheme {
        CustomTextField(
            value = value,
            onValueChange = { value = it },
            modifier = Modifier.background(
                color = Color.Gray,
                shape = CircleShape
            ),
            placeHolder = { Text(text = "place", textAlign = TextAlign.Center) },
            singleLine = true,
            maxLine = 1
        )
    }
}