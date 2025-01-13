package com.experiment.weather.core.common.extentions

import android.util.Log
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


/**
 * @param isoTime is in iso8601
 */
internal fun calculateUIHourlyTime(isoTime: String, offsetSeconds: Long): String {
	val givenDateTimeUTC = LocalDateTime.parse(isoTime).toEpochSecond(ZoneOffset.UTC)
	val differenceInMinutes =
		Duration.ofSeconds(givenDateTimeUTC
			.minus(Instant.now().epochSecond))
			.toMinutes()
	return if (differenceInMinutes in hourlyRangeThreshold)
		NOW
	else
		convertDateTimeToReadableHours(
			isoDateTime = isoTime,
			offsetSeconds = offsetSeconds,
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

/**
 * Timestamp must be in iso8601 standard, use to calculate hourly time
 */
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
	offsetSeconds: Long,
	pattern: String = HOURLY_PATTERN,
): String {
	//todo needs some work zone offset wont work, added offset to localdatetime for now
	val localDateTime = LocalDateTime.parse(isoDateTime).plusSeconds(12600)
	val zoneOffset = ZoneOffset.ofTotalSeconds(offsetSeconds.toInt())
	val offsetDateTime = OffsetDateTime.of(localDateTime, zoneOffset)
	val formatter = DateTimeFormatter.ofPattern(pattern)
	val timeString = localDateTime.format(formatter)
	Log.e("converter","localdatetime :$localDateTime")
//	Log.e("converter","zoneOfset: $zoneOffset")
	Log.e("converter","offset: $offsetDateTime")
	Log.e("converter","time: $timeString")
	return timeString
}

