package com.weather.feature.forecast.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import coil.compose.AsyncImage
import com.weather.core.design.theme.WeatherTheme
import com.weather.feature.forecast.components.hourlydata.HourlyStaticData
import com.weather.model.Hourly
import kotlin.math.roundToInt


@Composable
fun HourlyForecast(
    modifier: Modifier = Modifier,
    data: List<Hourly>,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            Text(
                text = "Today",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                color = LocalContentColor.current.copy(alpha = 0.5f)
            )
            LazyRow(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(data) { hourly ->
                    HourlyItem(modifier = Modifier, hourly)
                }
            }
        }
    }
}

@Composable
fun HourlyItem(
    modifier: Modifier = Modifier,
    item: Hourly,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.dt)
        }
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${item.icon}@2x.png",
            contentDescription = "Weather Icon",
            modifier = Modifier
        )
        Text(
            text = "${item.temp.toFloat().roundToInt()}°",
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HourlyCustomLayoutPreview() {
    WeatherTheme {
        val scrollState = rememberScrollState()
        HourlyWidgetWithGraph(
            modifier = Modifier
                .aspectRatio(21 / 9f)
                .border(width = 2.dp, Color.Red)
                .background(MaterialTheme.colorScheme.surface)
                .horizontalScroll(scrollState),
            itemCount = HourlyStaticData.size,
            graphHeight = 50.dp,
            hourlyGraph = { HourlyGraph(data = HourlyStaticData) },
            hourlyTimeStamps = { index ->
                val timeStamp = HourlyStaticData[index].dt
                Text(text = timeStamp)
            },
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Ychart() {
    val sampleData = List(6) { it }
    val points = HourlyStaticData.mapIndexed { index, hourly ->
        Point(index.toFloat(), hourly.temp.toFloat())
    }
//    val points = sampleData.mapIndexed { index, number ->
//        Point(number.toFloat(), index.toFloat())
//    }
    val steps = HourlyStaticData.size
    WeatherTheme {
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .steps(HourlyStaticData.size - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .backgroundColor(MaterialTheme.colorScheme.surface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(HourlyStaticData.maxOf { it.temp }.toInt())
            .labelAndAxisLinePadding(20.dp)
            .axisLineColor(MaterialTheme.colorScheme.onSurface)
            .axisLabelColor(MaterialTheme.colorScheme.onSurface)
            .backgroundColor(MaterialTheme.colorScheme.surface)
//            .labelData { i ->
//                (i).toString()
//            }
            .build()
        val linechart = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = points,
                        lineStyle = LineStyle(color = MaterialTheme.colorScheme.onSurface),
                        intersectionPoint = IntersectionPoint(color = MaterialTheme.colorScheme.onSurface),
                        selectionHighlightPoint = SelectionHighlightPoint(),
                        shadowUnderLine = ShadowUnderLine(),
                        selectionHighlightPopUp = SelectionHighlightPopUp()
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
//        gridLines = GridLines(),
            backgroundColor = MaterialTheme.colorScheme.surface
        )
        LineChart(modifier = Modifier.aspectRatio(16 / 9f), lineChartData = linechart)
    }
}

@Preview
@Composable
private fun HourlyGraphPreview() {
    HourlyGraph(
        modifier = Modifier,
        data = HourlyStaticData
    )
}

@Preview
@Composable
private fun HourlyWithGraphTestPreview() {
    WeatherTheme {
        HourlyForecast(data = HourlyStaticData)
    }
}

@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HourlyPreview() {
    WeatherTheme {
        HourlyForecast(data = HourlyStaticData)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HourlyItemPreview() {
    WeatherTheme {
        Surface {
//            HourlyItem(item = HourlyStaticData[0])
        }
    }
}