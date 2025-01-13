package com.experiment.weather.core.common.extentions

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
	val localDateTime = LocalDateTime.parse(isoTime).toEpochSecond(ZoneOffset.UTC)
	val differenceInMinutes =
		Duration.ofSeconds(localDateTime.minus(Instant.now().epochSecond)).toMinutes()
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
	val localDateTime = LocalDateTime.parse(isoDateTime)
	val zoneOffset = ZoneOffset.ofTotalSeconds(offsetSeconds.toInt())
	val offset = OffsetDateTime.of(localDateTime, zoneOffset)
	val formatter = DateTimeFormatter.ofPattern(pattern)
	val timeString = formatter.format(offset)
	return timeString
}

