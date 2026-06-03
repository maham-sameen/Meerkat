package com.example.meerkat.core.calendar.data.data_source.local

import androidx.room.Dao
import androidx.room.Query
import com.example.meerkat.core.calendar.data.data_source.model.local.AttachmentsEntity
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

@Dao
interface AttachmentDao : BaseDao<AttachmentsEntity> {
}