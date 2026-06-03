package com.example.meerkat.feature_meetings.data.data_source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.meerkat.feature_meetings.data.data_source.util.ConstantsMeeting
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MeetingLocalDataSource(
    val context: Context
) {
    private val Context.dataStore by preferencesDataStore(ConstantsMeeting.MEETING_PREF)
    private val HOST_KEY = stringPreferencesKey(ConstantsMeeting.HOST_KEY)

    suspend fun saveHost(host : String){
        context.dataStore.edit {
            it[HOST_KEY] = host
        }
    }

    suspend fun getHost(): String?{
        return context.dataStore.data.map {
            it[HOST_KEY]
        }.first()
    }
}