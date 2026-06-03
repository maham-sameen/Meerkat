package com.example.meerkat.core.calendar.data.data_source.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun currentTimeInRfc3339(timeZoneId: String): String {
    val zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZoneId))
    return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}


