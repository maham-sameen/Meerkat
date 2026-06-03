package com.example.meerkat.core.time_zone.domain.util

import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class TimeZoneInfo(
    val name : String,
    val zoneName : String,
){
    fun doesMatchSearchQuery(query: String): Boolean{
        val lowerCaseQuery = query.lowercase(Locale.ENGLISH)
        return name.lowercase(Locale.ENGLISH).contains(lowerCaseQuery)
    }
}


data class TimeZoneCategory (
    val name : TimeZoneInfo ,
    val items: List<TimeZoneInfo>
){
    fun doesMatchSearchQuery(query: String): TimeZoneCategory? {
        val newItems = items.filter { it.doesMatchSearchQuery(query) }
        return if (newItems.isNotEmpty()) TimeZoneCategory(name, newItems) else null
    }
}