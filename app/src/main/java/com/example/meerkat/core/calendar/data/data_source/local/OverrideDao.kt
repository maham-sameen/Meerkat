package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.meerkat.core.calendar.data.data_source.model.local.OverrideEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface OverrideDao : BaseDao<OverrideEntity> {
}