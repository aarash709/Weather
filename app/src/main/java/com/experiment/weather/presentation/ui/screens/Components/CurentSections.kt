package com.experiment.weather.presentation.ui.screens.Components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entities.onecall.OneCallCurrentEntity
import com.example.entities.onecall.OneCallDailyEntity
import com.example.entities.onecall.OneCallEntity
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherTemp(oneCall: OneCallEntity?, current: OneCallCurrentEntity?) {
    Column(
        Modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = oneCall?.cityName + "",
            fontSize = 18.sp
        )
        Text(
            text = "${current?.temp?.minus(273.15)?.roundToInt()}°C",
            fontSize = 48.sp,
        )
//        Text(
//            text = current?.weather?.description.toString(),
//            fontSize = 20.sp
//        )
    }
}

@Composable
fun WeatherDetails(modifier: Modifier, current: OneCallCurrentEntity?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "FeelsLike ${
                    current?.feels_like?.minus(273.15)
                        ?.roundToInt()
                }",
                fontSize = 14.sp
            )
            Text(
                text = "Humidity ${current?.humidity}",
                fontSize = 14.sp
            )
            //Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "Wind ${
                    current?.wind_speed?.times(3.6f)?.roundToInt().toString()
                }",
                fontSize = 14.sp
            )

        }
        Spacer(modifier = Modifier.size(6.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "DewPoint ${
                    current?.dew_point?.minus(273.15)
                        ?.roundToInt().toString()
                }",
                fontSize = 14.sp
            )
            Text(
                text = "Visibility ${current?.visibility?.div(1000)} km",
                fontSize = 14.sp
            )
            Text(
                text = "Barometer ${current?.pressure}",
                fontSize = 14.sp
            )

        }
    }
}

@Composable
fun FourDayForecast(modifier: Modifier = Modifier, dailyData: List<OneCallDailyEntity>?) {
    Row(
        modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        dailyData?.forEachIndexed { index, it ->
            if (index <= 3) {
                DayDetail(it)
            }
        }
    }
}

@Composable
fun DayDetail(dailyDetail: OneCallDailyEntity?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(text = dailyDetail?.weather?.description.toString())
        Text(
            text = "day: " + dailyDetail?.temp?.max?.minus(273.15)
                ?.roundToInt().toString()
        )
        Text(
            text = "night: " + dailyDetail?.temp?.night?.minus(273.15)
                ?.roundToInt().toString()
        )
    }
}

@Composable
fun addOrUpdateSection(
    context: Context,
    firstButtonName: String,
    secondButtonName: String,
    lat: String,
    lon: String,
    databaseLocation: String,
    insertCallback: (lat: String, lon: String) -> Unit,
    updateCallback: (lat: String, lon: String) -> Unit,
    TextFieldEmptyCallback: (isEmpty: Boolean) -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "SearchLocation: $databaseLocation")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (lat.isEmpty() || lon.isEmpty()) {
                        TextFieldEmptyCallback(true)
                        Toast.makeText(
                            context,
                            "Coordinates Cannot be empty",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        insertCallback(lat, lon)
                        TextFieldEmptyCallback(false)
                    }
                },
            ) {
                Text(text = firstButtonName)
            }
            Button(
                onClick = {
                    if (lat.isEmpty() || lon.isEmpty()) {
                        TextFieldEmptyCallback(true)
                        Toast.makeText(
                            context,
                            "Coordinates Cannot be empty",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        updateCallback(lat, lon)
                        TextFieldEmptyCallback(false)
                    }
                },
            ) {
                Text(text = secondButtonName)
            }
        }

    }
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {

    }
}

