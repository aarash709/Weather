package com.weather.feature.managelocations.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.weather.model.ManageLocationsData

@Composable
internal fun LocationItem(
	modifier: Modifier = Modifier,
	data: ManageLocationsData,
	inSelectionMode: Boolean,
	selected: Boolean,
) {
	val isFavorite = data.isFavorite
	Surface(
		modifier = modifier
			.fillMaxWidth(),
		shape = RoundedCornerShape(32.dp),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			AnimatedVisibility(
				visible = inSelectionMode,
				modifier = Modifier,
				enter = fadeIn(animationSpec = tween(100)) + expandHorizontally(animationSpec = spring()),
				exit = fadeOut(animationSpec = tween(25)) + shrinkHorizontally(),
				label = "draggable icon"
			) {
				Icon(
					imageVector = Icons.Default.DragHandle,
					modifier = Modifier
						.padding(end = 16.dp),
					contentDescription = "draggable icon"
				)
			}
			Column(
				modifier = Modifier.weight(1f),
				horizontalAlignment = Alignment.Start
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = data.locationName,
						fontSize = 18.sp
					)
					Spacer(modifier = Modifier.width(16.dp))
					if (isFavorite) {
						Icon(
							imageVector = Icons.Default.Star,
							modifier = Modifier.size(20.dp),
							tint = Color.Yellow,
							contentDescription = "selected icon star"
						)
					}
				}
			}
			Row(
				modifier = Modifier,
				horizontalArrangement = Arrangement.Start,
				verticalAlignment = Alignment.CenterVertically
			) {
				AsyncImage(
					model = "https://openweathermap.org/img/wn/${data.weatherIcon}@2x.png",
					modifier = Modifier,
					contentDescription = "weather icon"
				)
				Text(
					text = "${data.currentTemp}°",
					modifier = Modifier.width(60.dp),
					textAlign = TextAlign.End,
					fontSize = 28.sp
				)
			}
			AnimatedVisibility(
				visible = inSelectionMode,
				modifier = Modifier,
				enter = fadeIn(animationSpec = tween(100)) + expandHorizontally(animationSpec = spring()),
				exit = fadeOut(animationSpec = tween(25)) + shrinkHorizontally(),
				label = "selection button"
			) {
				Icon(
					imageVector = Icons.Filled.CheckCircle,
					modifier = Modifier
						.padding(start = 16.dp),
					contentDescription = "selected Icon",
					tint =
					if (selected)
						MaterialTheme.colorScheme.primary
					else
						MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
				)
			}
		}
	}
}
