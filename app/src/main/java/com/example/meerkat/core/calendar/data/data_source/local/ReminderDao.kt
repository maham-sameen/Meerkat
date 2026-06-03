package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.model.local.ReminderEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.ReminderWithData
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao : BaseDao<ReminderEntity> {

}