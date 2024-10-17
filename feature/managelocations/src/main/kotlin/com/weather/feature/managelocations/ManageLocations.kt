package com.weather.feature.managelocations

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastMapIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.core.common.R
import com.weather.core.design.components.ShowLoadingText
import com.weather.core.design.modifiers.bouncyTapEffect
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.managelocations.components.LocationItem
import com.weather.feature.managelocations.components.LocationsBottomBar
import com.weather.feature.managelocations.components.LocationsTopbar
import com.weather.feature.managelocations.components.SearchBarCard
import com.weather.feature.managelocations.components.detectLongPressGesture
import com.weather.feature.managelocations.components.draggableItem
import com.weather.feature.managelocations.components.locationsClickable
import com.weather.feature.managelocations.components.rememberDragAndDropListItem
import com.weather.model.Coordinate
import com.weather.model.ManageLocationsData

@ExperimentalFoundationApi
@Composable
fun ManageLocationsRoute(
	viewModel: ManageLocationsViewModel = hiltViewModel(),
	onBackPressed: () -> Unit,
	onItemSelected: (String) -> Unit,
	onNavigateToSearch: () -> Unit,
) {
	//stateful
	val dataState by viewModel.locationsState.collectAsStateWithLifecycle()
	val context = LocalContext.current
	Card(modifier = Modifier) {
		ManageLocations(
			dataState = dataState,
			onBackPressed = onBackPressed,
			onItemSelected = { coordinate ->
				onItemSelected(coordinate.cityName.toString())
			},
			onDeleteItem = { cityNames ->
				viewModel.deleteWeatherByCityName(cityNames = cityNames, context = context)
			},
			onSetFavoriteItem = { favoriteCity ->
				viewModel.saveFavoriteCityCoordinate(cityName = favoriteCity, context = context)
			},
			onNavigateToSearch = { onNavigateToSearch() },
			onReorderEnd = { locations ->
				viewModel.reorderDataIndexes(locations)
			}
		)
	}

@OptIn(
	ExperimentalMaterial3Api::class
)
@Composable
fun ManageLocations(
	dataState: LocationsUIState,
	onBackPressed: () -> Unit,
	onItemSelected: (Coordinate) -> Unit,
	onDeleteItem: (List<String>) -> Unit,
	onSetFavoriteItem: (String) -> Unit,
	onNavigateToSearch: () -> Unit,
	onReorderEnd: (List<ManageLocationsData>) -> Unit,
) {
	var selectedCities by rememberSaveable {
		mutableStateOf(emptySet<String>())
	}
	val isInEditMode by remember {
		derivedStateOf { selectedCities.isNotEmpty() }
	}
	var locationsList by remember {
		mutableStateOf(emptyList<ManageLocationsData>())
	}
	var shouldSelectAll by rememberSaveable {
		mutableStateOf(false)
	}
	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	LaunchedEffect(key1 = shouldSelectAll) {
		if (shouldSelectAll) {
			selectedCities += locationsList.map { it.locationName }
		} else {
			selectedCities = emptySet()
		}
	}
	BackHandler(enabled = isInEditMode) {
		if (isInEditMode)
			selectedCities = emptySet()
	}
	Scaffold(
		modifier = Modifier,
		topBar = {
			LocationsTopbar(
				isInEditMode = isInEditMode,
				selectedCitySize = selectedCities.size,
				scrollBehavior = scrollBehavior,
				onBackPressed = { onBackPressed() },
				onIsAllSelected = { shouldSelectAll = !shouldSelectAll },
				onEmptyCitySelection = { selectedCities = emptySet() }
			)
		},
		bottomBar = {
			LocationsBottomBar(
				isInEditMode = isInEditMode,
				selectedCitySize = selectedCities.size,
				onDeleteItem = { onDeleteItem(selectedCities.toList()) },
				onEmptyCitySelection = { selectedCities = emptySet() },
				onSetFavoriteItem = {
					onSetFavoriteItem(selectedCities.first())
				})
		}
	) { padding ->
		when (dataState) {
			is LocationsUIState.Loading -> ShowLoadingText()
			is LocationsUIState.Success -> {
				LaunchedEffect(key1 = dataState) {
					locationsList = dataState.data
					if (locationsList.size == 1) {
						onSetFavoriteItem(locationsList.first().locationName)
					}
				}
				Column(
					modifier = Modifier
						.nestedScroll(scrollBehavior.nestedScrollConnection)
						.padding(padding)
						.padding(horizontal = 16.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Spacer(modifier = Modifier.height(16.dp))
					SearchBarCard(onClick = {
						onNavigateToSearch()
					})
					Spacer(modifier = Modifier.height(16.dp))

					val inSelectionMode by remember {
						derivedStateOf { selectedCities.isNotEmpty() }
					}
					if (dataState.data.isEmpty()) {
						EmptyListMassage()
					} else {
						var items by remember {
							mutableStateOf(dataState.data)
						}
						val lazyListState = rememberLazyListState()
						val dragDropState =
							rememberDragAndDropListItem(
								lazyListState = lazyListState,
								onUpdateData = { fromIndex, toIndex ->
									//do the reordering first then update order to the new list`s index
									items = items.toMutableList().apply {
										add(toIndex, removeAt(fromIndex))
									}.fastMapIndexed { index, item ->
										item.copy(listOrder = index)
									}
								},
								onReorderEnd = { _, _ ->
									onReorderEnd(items)
								}
							)
						LazyColumn(
							modifier = Modifier
								.fillMaxSize()
								.detectLongPressGesture(
									lazyListState = lazyListState,
									dragAndDropListItemState = dragDropState
								),
							state = lazyListState,
							verticalArrangement = Arrangement.spacedBy(16.dp)
						) {
							itemsIndexed(
								items = items,
								key = { _, item ->
									item.locationName
								}) { index, locationData ->
								val selected by remember(selectedCities) {
									mutableStateOf(locationData.locationName in selectedCities)
								}
								LocationItem(
									modifier = Modifier
										.bouncyTapEffect()
										.locationsClickable(
											inSelectionMode = inSelectionMode,
											onSelectionMode = {
												if (selected)
													selectedCities -= locationData.locationName
												else
													selectedCities += locationData.locationName
											},
											onItemSelected = {
												onItemSelected(
													Coordinate(
														locationData.locationName,
														locationData.latitude,
														locationData.longitude
													)
												)
											},
											onLongClick = { selectedCities += locationData.locationName }
										)
										.draggableItem(
											draggableState = dragDropState,
											listIndex = index
										)
											then if (dragDropState.currentDraggableItemIndex != index) {
										Modifier.animateItem()
									} else {
										Modifier
									},
									data = locationData,
									inSelectionMode = inSelectionMode,
									selected = selected,
								)
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun EmptyListMassage() {
	Text(
		text = stringResource(id = R.string.search_and_add_a_location),
		color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
		fontSize = 12.sp
	)
}

@ExperimentalFoundationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun ManageLocationsPreview() {
	val data = LocationsUIState.Success(
		listOf(
			ManageLocationsData(
				locationName = "Tehran",
				weatherIcon = "",
				latitude = 10.toString(),
				longitude = 10.toString(),
				currentTemp = "2",
				humidity = "46",
				feelsLike = "1",
				listOrder = 0,
				timezone = "mattis",
				timezoneOffset = 9795,
				isFavorite = false
			),
			ManageLocationsData(
				locationName = "Tabriz",
				weatherIcon = "",
				latitude = 10.toString(),
				longitude = 10.toString(),
				currentTemp = "0",
				humidity = "55",
				feelsLike = "0",
				listOrder = 1,
				timezone = "expetendis",
				timezoneOffset = 5367,
				isFavorite = false,

				)
		)
	)
	WeatherTheme {
		Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
			ManageLocations(
				dataState = data,
				onBackPressed = {},
				onItemSelected = {},
				onDeleteItem = {},
				onSetFavoriteItem = {},
				onNavigateToSearch = {},
				onReorderEnd = { _ -> },
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
internal fun SearchCardPreview() {
	WeatherTheme {
		SearchBarCard(onClick = {})
	}
}

@Preview(showBackground = false)
@Composable
internal fun CityItemPreview() {
	WeatherTheme {
		LocationItem(
			data = ManageLocationsData(
				locationName = "Tehran",
				weatherIcon = "",
				latitude = 10.toString(),
				longitude = 10.toString(),
				currentTemp = "2",
				humidity = "46",
				feelsLike = "1",
				listOrder = 0,
				timezone = "tation",
				timezoneOffset = 3337,
				isFavorite = false,

				),
			inSelectionMode = true,
			selected = false
		)
	}
}
