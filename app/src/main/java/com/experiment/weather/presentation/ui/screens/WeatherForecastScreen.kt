package com.experiment.weather.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.experiment.weather.data.remote.model.weatherData.Current
import com.experiment.weather.data.remote.model.weatherData.OneCallCoordinates
import com.experiment.weather.data.remote.model.weatherData.WeatherData
import com.experiment.weather.presentation.ui.theme.WeatherTheme
import com.experiment.weather.presentation.viewmodel.ForecastViewModel
import com.experiment.weather.presentation.viewmodel.WeatherUIState
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun WeatherForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToSearch: () -> Unit,
    navigateToGetStarted: () -> Unit,
) {
    //stateful
    val databaseIsEmpty by viewModel.dataBaseOrCityIsEmpty.collectAsStateWithLifecycle()
    val weatherUIState by viewModel
        .weatherUIState.collectAsStateWithLifecycle()
    var showHome by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        key1 = databaseIsEmpty
    ) {
//        launch {
//            viewModel.checkDatabase()
//        }
        Timber.e("ui: $databaseIsEmpty")
//        showHome = !databaseIsEmpty
    }
    if (databaseIsEmpty) {
        LaunchedEffect(key1 = Unit) {
            navigateToGetStarted()
        }
    } else {
        WeatherForecastScreen(
            weatherUIState = weatherUIState,
            onNavigateToManageLocations = { navigateToSearch() }
        )
    }
}

@Composable
fun WeatherForecastScreen(
    weatherUIState: WeatherUIState,
    onNavigateToManageLocations: () -> Unit,
) {
    // stateless
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        when (weatherUIState) {
            WeatherUIState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Loading...",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }
            is WeatherUIState.Success -> {
                TopAppBar(
                    modifier = Modifier,
                    elevation = 0.dp
                ) {
                    TopBar(
                        cityName = weatherUIState.data.coordinates.name.toString(),
                        onNavigateToManageLocations = { onNavigateToManageLocations() }
                    )
                }
                CurrentWeather(
                    weatherData = weatherUIState.data.current
                )
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .size(size = 60.dp)
//                .background(Brush.horizontalGradient(listOf(Color.Red, Color.Yellow))),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "Sunset/Sunrise")
//        }
//        HourlyForecast()
//        Column(
//            modifier = Modifier,
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//            DailyForecastItem()
//        }
    }
}


@Composable
private fun TopBar(
    cityName: String,
    onNavigateToManageLocations: () -> Unit,
) {
    // TODO:  //handled in the gps handler later on
    val locationBased by remember {
        mutableStateOf<Boolean>(value = false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigateToManageLocations() }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        }
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            if (locationBased) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Location Icon"
                )
            }
            Text(
                text = cityName,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Location Picker Icon"
            )
        }
//        Switch(checked = false, onCheckedChange = {/*todo change theme*/ })
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Location Pick Icon"
        )
    }
}

@Composable
private fun DailyForecastItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Outlined.Cloud,
            contentDescription = "Weather Icon"
        )
        Text(
            text = "Tomorrow-Rainy",
            modifier = Modifier.padding(start = 16.dp)
        )
        Row {
            Text(
                text = "19°",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "28°",
                fontSize = 12.sp
            )
        }
    }
}

//Start use hourly component laterOn


//// end
@Composable
private fun CurrentWeather(
    weatherData: Current,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrentTempAndCondition(
                temp = weatherData.temp.minus(273.15).roundToInt().toString(),
                feelsLikeTemp = weatherData.feels_like.minus(273.15).roundToInt().toString()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        CurrentWeatherDetails(
            weatherData = weatherData
        )
    }
}

@Composable
fun CurrentWeatherDetails(
    weatherData: Current,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            image = Icons.Outlined.Air,
            value = "${weatherData.wind_speed}km/h",
            itemName = "Wind Speed"
        )
        WeatherDetailItem(
            image = Icons.Outlined.WaterDrop,
            value = "${weatherData.humidity}%",
            itemName = "Humidity"
        )
        WeatherDetailItem(
            image = Icons.Outlined.Visibility,
            value = "${weatherData.visibility}km",
            itemName = "Visibility"
        )
    }
}

@Composable
private fun WeatherDetailItem(
    image: ImageVector,
    value: String,
    itemName: String,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = image,
                contentDescription = itemName,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = value,
                fontSize = 10.sp
            )
        }
        Text(
            text = itemName,
            fontSize = 10.sp
        )
    }
}


@Composable
private fun CurrentTempAndCondition(
    temp: String,
    feelsLikeTemp: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Outlined.WbCloudy,
            contentDescription = "Current Weather",
            modifier = Modifier.size(128.dp)
        )
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Sunny",
                fontSize = 18.sp
            )
            Text(
                text = "$temp°",
                fontSize = 42.sp
            )
            Text(
                text = "Feels like $feelsLikeTemp°",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

//@Composable
//fun WeatherMain(viewModel: MainPageViewModel = viewModel()) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val scaffoldState = rememberScaffoldState()
//    val navHost = rememberNavController()
//
//    /**
//     * returns latest "geo coordinates" based on value we set through "geocoding"
//     * in our ViewModel
//     */
//    val addresses = viewModel.address.observeAsState()
//
//    val allOneCall = viewModel.oneCallDataList.observeAsState()
//    val allOneCallCurrent = viewModel.oneCallCurrentList.observeAsState()
//    val allOneCallDaily = viewModel.oneCallDailyList.observeAsState()
//    val allOneCallHourly = viewModel.oneCallHourlyListList.observeAsState()
//
//    var isSearchFieldEmpty by remember {
//        mutableStateOf(false)
//    }
//    var lat by remember {
//        mutableStateOf("")
//    }
//    var lon by remember {
//        mutableStateOf("")
//    }
//    var location by remember {
//        mutableStateOf("")
//    }
//    var databaseLocation by remember {
//        mutableStateOf("")
//    }
//    var searchClicked by remember {
//        mutableStateOf(false)
//    }
//    var selectedCity by remember {
//        mutableStateOf("")
//    }
//    var selectedOneCallIndex by remember {
//        mutableStateOf(0)
//    }
//    var selectedOneCallCurrentIndex by remember {
//        mutableStateOf(0)
//    }
//    LaunchedEffect(key1 = 1) {
//        viewModel.insertOneCallData(locationName = "tehran")
//    }
//    LaunchedEffect(selectedCity) {
//        allOneCall.value?.forEachIndexed() { index, oneCallData ->
//            if (selectedCity == oneCallData.cityName) {
//                selectedOneCallIndex = index
//            }
//        }
//        allOneCallCurrent.value?.forEachIndexed() { index, oneCallCurrent ->
//            if (selectedCity == oneCallCurrent.cityName) {
//                selectedOneCallCurrentIndex = index
//            }
//        }
//    }
//    LaunchedEffect(key1 = searchClicked) {
//        addresses.value?.forEach {
//            lat = it.latitude.toString()
//            lon = it.longitude.toString()
//
//            val countryName = it.countryName
//            val cityName = it.locality
//            databaseLocation = "$cityName, $countryName"
//
//            launch {
////                viewModel.getAllOneCallDataList()
////                viewModel.getAllOneCallCurrentList()
//                //this list is not working anymore i guess??
//                //viewModel.getOneCallWithLocationName(databaseLocation)
//                //viewModel.getOneCallCurrentWithLocationName(databaseLocation)
//            }
//        }
//    }
//    Scaffold(scaffoldState = scaffoldState,
//        bottomBar = {
//            BottomNavigationBar(
//                navController = navHost,
//                navigationItems = listOf(
//                    NavigationItem(
//                        "Places",
//                        Screens.allCities.route,
//                        painterResource(id = R.drawable.ic_options)
//                    ),
//                    NavigationItem(
//                        "Places",
//                        Screens.addCityPage.route,
//                        painterResource(id = R.drawable.ic_add)
//                    ),
//                    NavigationItem(
//                        "Add City",
//                        Screens.settingsPage.route,
//                        painterResource(id = R.drawable.ic_search)
//                    )
//                )
//            ) { navigationItem ->
//                navHost.navigate(navigationItem.route) {
//                    launchSingleTop = true
//                    restoreState = true
//                    popUpTo(Screens.currentPage.route) {
//                        //prevents recomposition on click
//                        saveState = true
//                    }
//                }
//            }
//        }) { padding ->
//        NavHost(
//            navController = navHost,
//            startDestination = Screens.currentPage.route,
//            modifier = Modifier.padding(paddingValues = padding)
//        ) {
//            composable(Screens.currentPage.route) {
//                CurrentScreen(
//                    allOneCall.value?.get(selectedOneCallIndex),
//                    allOneCallCurrent.value?.get(selectedOneCallCurrentIndex),
//                    allOneCallDaily.value?.firstOrNull(),
//                    allOneCallHourly.value,
//                    selectedCity
//                )
//            }
//            composable(Screens.addCityPage.route) {
//                AddCityScreen(
//                    context,
//                    databaseLocation,
//                    lat,
//                    lon,
//                    insertCallBack = { lat, lon, databaseLocation ->
//                        viewModel.insertOneCallData(lat, lon, databaseLocation)
//                    },
//                    updateCallBack = { lat, lon, databaseLocation ->
//                        viewModel.updateOneCallData(lat, lon, databaseLocation)
//                    },
//                    onSearchFieldEmpty = { isSearchFieldEmpty = it },
//                    onSearchClicked = {
//                        searchClicked = !searchClicked
//                        viewModel.reverseGeoCode(location, 1)
//                    },
//                    onLocationChange = { location = it },
//                    searchedLocation = location
//                )
//            }
//            composable(Screens.allCities.route) {
//                ShowAllCitiesScreen(
//                    Modifier,
//                    allOneCall.value,
//                    allOneCallCurrent.value,
//                    selectedCityName = selectedCity,
//                    onClick = {
//                        selectedCity = it.cityName.toString()
//                        //update all data or
//                        viewModel.updateAllDatabase(allOneCall.value)
//                        // TODO: 11/9/2021 Update the Whole database on city select
//                        // TODO: 11/9/2021 or we can update based on time which demands more work
//                        //this is for just updating the selected city
////                        lat = it.lat.toString()
////                        lon = it.lon.toString()
////                        viewModel.updateOneCallData(lat, lon, databaseLocation)
//                        navHost.navigate(Screens.currentPage.route) {
//                            popUpTo(Screens.currentPage.route)
//                        }
//                    }
//                )
//            }
//            composable(Screens.settingsPage.route) {
//                Text(text = "search")
//            }
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    WeatherTheme {
        val data = WeatherUIState.Success(
            data = WeatherData(
                coordinates = OneCallCoordinates(
                    name = "Tehran",
                    lat = null,
                    lon = null,
                    timezone = "tehran",
                    timezone_offset = null
                ),
                current = Current(
                    clouds = 27,
                    dew_point = 273.46,
                    dt = 1674649142,
                    feels_like = 286.08,
                    humidity = 38,
                    pressure = 1017,
                    sunrise = 1674617749,
                    sunset = 1674655697,
                    temp = 287.59,
                    uvi = 0.91,
                    visibility = 10000,
                    wind_deg = 246,
                    wind_gust = 1.71,
                    wind_speed = 2.64,
                    weather = null
                )
            )
        )
        WeatherForecastScreen(weatherUIState = data,
            onNavigateToManageLocations = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPreview() {
    WeatherTheme {
        CurrentTempAndCondition(temp = "5", feelsLikeTemp = "3")
    }
}

@Preview(showBackground = true)
@Composable
fun FourDayPreview() {
    WeatherTheme {
        DailyForecastItem()
    }
}




