package com.example.meerkat.core.time_zone.domain.use_case

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.meerkat.core.time_zone.domain.util.TimeZoneCategory
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class GetTimeZonesUseCase {
    private  fun getTimeZoneMap(): Map<String, MutableList<TimeZoneInfo>>{
        val categories = mapOf(
            "Asia" to mutableListOf<TimeZoneInfo>(),
            "Atlantic" to mutableListOf<TimeZoneInfo>(),
            "UTC" to mutableListOf<TimeZoneInfo>(),
            "Europe" to mutableListOf<TimeZoneInfo>(),
            "Pacific" to mutableListOf<TimeZoneInfo>(),
            "America" to mutableListOf<TimeZoneInfo>(),
            "Australia" to mutableListOf<TimeZoneInfo>(),
            "Africa" to mutableListOf<TimeZoneInfo>(),
            "Indian" to mutableListOf<TimeZoneInfo>(),
            "Antarctica" to mutableListOf<TimeZoneInfo>()
        )

        val tzs = ZoneId.getAvailableZoneIds()

        val keywords = mapOf(
            "UTC" to setOf("Greenwich", "Universal", "Zulu", "GMT", "UCT", "UTC"),
            "Europe" to setOf("Iceland", "WET", "MET", "Poland", "Eire", "CET", "EET", "W-SU", "GB", "Portugal", "Arctic"),
            "Pacific" to setOf("Chile/EasterIsland", "Kwajalein"),
            "America" to setOf("CST6CDT", "PST8PDT", "MST7MDT", "EST5EDT", "Mexico", "Brazil", "Canada", "US", "Cuba", "Navajo", "Jamaica", "Chile/Continental"),
            "Africa" to setOf("Libya", "Egypt"),
            "Asia" to setOf("Hongkong", "PRC", "Iran", "Turkey", "ROK", "Japan", "Singapore", "Israel"),
            "Australia" to setOf("NZ","Australia/LHI","Australia/NSW" , "Australia/ACT" , "NZ-CHAT")
        )

        val specialCases = mapOf(
            "PRC" to "People's Republic of China (China)",
            "ROK" to "Republic of Korea (South Korea)",
            "CST6CDT" to "Central Standard Time / Central Daylight Time (USA, Canada)",
            "PST8PDT" to "Pacific Standard Time / Pacific Daylight Time (USA, Canada)",
            "MST7MDT" to "Mountain Standard Time / Mountain Daylight Time (USA, Canada)",
            "EST5EDT" to "Eastern Standard Time / Eastern Daylight Time (USA, Canada)",
            "Chile/Continental" to "Chile Standard Time (Chile mainland)",
            "Greenwich" to "Greenwich Time",
            "Universal" to "Universal Time Coordinated",
            "Zulu" to "Zulu Time",
            "GMT" to "Greenwich Mean Time",
            "UCT" to "Universal Coordinated Time",
            "UTC" to "Coordinated Universal Time",
            "WET" to "Western European Time (Portugal, Canary Islands, Iceland)",
            "MET" to "Middle European Time (Similar to CET, used historically)",
            "Eire" to "Ireland Standard Time (Same as GMT in winter, IST in summer)",
            "CET" to "Central European Time (Germany, France, Spain, etc.)",
            "EET" to "Eastern European Time (Greece, Finland, Romania, etc.)",
            "W-SU" to "Western Soviet Union Time (Legacy, now mostly MSK - Moscow Time)",
            "GB" to "Great Britain Time (Usually GMT in winter, BST in summer)",
            "Chile/EasterIsland" to "Easter Island, Chile (Pacific Time Zone)",
            "Kwajalein" to "Kwajalein Atoll, Marshall Islands (Pacific Time)",
            "NZ" to "New Zealand Time",
            "Australia/LHI" to "Lord Howe Island Time - Australia",
            "NZ-CHAT" to "Chatham Island Time - New Zealand",
            "Australia/NSW" to "New South Wales Time - Australia",
            "Australia/ACT" to "Australian Capital Territory Time - Australia"
        )
        for (zoneId in tzs) {
            if (zoneId.contains("Etc") || zoneId.contains("SystemV")) continue

            val category = when {
                zoneId.startsWith("Asia") -> "Asia"
                zoneId.startsWith("Atlantic") -> "Atlantic"
                zoneId.startsWith("America") -> "America"
                zoneId.startsWith("UTC") -> "UTC"
                zoneId.startsWith("Africa") -> "Africa"
                zoneId.startsWith("Pacific") -> "Pacific"
                zoneId.startsWith("Europe") -> "Europe"
                zoneId.startsWith("Australia")-> "Australia"
                zoneId.startsWith("Antarctica") -> "Antarctica"
                zoneId.startsWith("Indian") -> "Indian"
                else -> keywords.entries.firstOrNull { (_, words) -> words.contains(zoneId) }?.key
            }

            category?.let {
                val name = specialCases[zoneId] ?: extractZoneName(zoneId)
                categories[it]?.add(TimeZoneInfo(name, zoneId))
            }
        }
        return categories
    }

    operator fun invoke(): List<TimeZoneCategory>{
        val timeZonesMap = getTimeZoneMap()
        val timeZoneCategories = mutableListOf<TimeZoneCategory>()
        timeZonesMap.forEach { (key, values) ->
            timeZoneCategories.add(
                TimeZoneCategory(
                    name = TimeZoneInfo(name = key, zoneName = key),
                    items = values
                )
            )
        }
        return timeZoneCategories
    }
    private fun extractZoneName(zoneId: String): String {
        val parts = zoneId.split("/")
        return if (parts.size == 2) parts[1] else zoneId
    }
}