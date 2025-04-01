package com.experiment.weather.core.common.extentions

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

internal fun calculateUIHourlyTime(
	currentIsoTime: String,
	hourlyIsoTime: String,
): String {
	val databaseCurrentTime =
		LocalDateTime.parse(currentIsoTime).toEpochSecond(ZoneOffset.UTC)
	val givenDateTimeUTC = LocalDateTime.parse(hourlyIsoTime).toEpochSecond(ZoneOffset.UTC)
	val differenceInMinutes =
		Duration.ofSeconds(
			givenDateTimeUTC
				.minus(databaseCurrentTime)
		)
			.toMinutes()
	return if (differenceInMinutes in hourlyRangeThreshold)
		NOW
	else
		convertDateTimeToReadableHours(
			isoDateTime = hourlyIsoTime,
			pattern = HOURLY_PATTERN
		)
}

/**
 * Convert the given timestamp to be "now", "tomorrow" or day of week names
 * @param isoDate must be iso8601 standard
 */
internal fun calculateUIDailyTime(
	isoDate: String,
): String {
	val dayOfWeekOfDailyData =
		convertDateToReadableDate(isoDate = isoDate)
	val today = LocalDateTime.now().dayOfWeek.name
	val tomorrow = LocalDateTime.now().plusDays(1).dayOfWeek.name

	return when (dayOfWeekOfDailyData.uppercase()) {
		today -> TODAY
		tomorrow -> TOMORROW
		else -> dayOfWeekOfDailyData.slice(0..2)
	}
}

private fun convertDateToReadableDate(
	isoDate: String
): String {
	val dayOfWeek = LocalDate.parse(isoDate, DateTimeFormatter.ISO_DATE).dayOfWeek
	val formattedDay =
		dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
	return formattedDay
}

/**
 * Timestamp must be in iso8601 standard, use to calculate daily time
 */
private fun convertDateTimeToReadableHours(
	isoDateTime: String,
	offsetSeconds: Long = 0,
	pattern: String = HOURLY_PATTERN,
): String {
	val localDateTime = LocalDateTime.parse(isoDateTime).plusSeconds(offsetSeconds)
	val formatter = DateTimeFormatter.ofPattern(pattern)
	val timeString = localDateTime.format(formatter)
	return timeString
}

