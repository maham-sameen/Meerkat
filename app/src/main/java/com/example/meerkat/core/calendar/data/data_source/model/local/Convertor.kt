package com.example.meerkat.core.calendar.data.data_source.model.local
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun listToJsonString(value: List<String>?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun jsonStringToList(value: String?): List<String>? {
        return value?.let {
            Gson().fromJson(it, Array<String>::class.java).toList()
        }
    }

    @TypeConverter
    fun mapToJson(map: Map<String, String>?): String? {
        return map?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun jsonToMap(json: String?): Map<String, String>? {
        return json?.let {
            Gson().fromJson(it, object : TypeToken<Map<String, String>>() {}.type)
        }
    }
}
