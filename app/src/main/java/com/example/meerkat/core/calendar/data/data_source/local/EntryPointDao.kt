package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import com.example.meerkat.core.calendar.data.data_source.model.local.EntryPointEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryPointDao : BaseDao<EntryPointEntity> {


}