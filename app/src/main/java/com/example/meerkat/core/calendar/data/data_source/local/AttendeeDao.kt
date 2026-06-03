package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity

@Dao
interface AttendeeDao  : BaseDao<AttendeeEntity> {
}