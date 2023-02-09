package com.weather.feature.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weather.model.DailyPreview
import kotlin.math.roundToInt

@Composable
fun Daily(
    modifier: Modifier = Modifier,
    dailyList: List<DailyPreview>,
) {
    Column( modifier = modifier
        .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        dailyList.forEach {daily->
            DailyItem(modifier = Modifier, daily)
        }
    }
}

@Composable
fun DailyItem(modifier: Modifier = Modifier, daily: DailyPreview) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(5f),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.WbSunny, contentDescription = "Weather Icon")
            Text(text = daily.time)
//            Text(
//                text = daily.time, fontSize = 10.sp,
//                color = Color.Gray
//            )
        }
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = daily.temp.toFloat().minus(273.15).roundToInt().toString(),
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = daily.temp.toFloat().minus(273.15).roundToInt().toString(), fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyItemPreview() {
    DailyItem(daily = DailyData[0])
}

@Preview(showBackground = true)
@Composable
private fun DailyListPreview() {
    Daily(dailyList = DailyData)
}

val DailyData = listOf(
    DailyPreview(
        temp = "283",
        time = "Tomorrow",
        icon = ""
    ),
    DailyPreview(
        temp = "280",
        time = "Wed",
        icon = ""
    ),
    DailyPreview(
        temp = "284",
        time = "Thur",
        icon = ""
    ),
    DailyPreview(
        temp = "278",
        time = "fri",
        icon = ""
    ),
    DailyPreview(
        temp = "281",
        time = "Fri",
        icon = ""
    )
)