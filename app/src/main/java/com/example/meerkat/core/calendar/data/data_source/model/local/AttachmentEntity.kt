package com.example.meerkat.core.calendar.data.data_source.model.local


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.meerkat.core.calendar.data.data_source.utils.ConstantCalendar

@Entity(
    tableName = ConstantCalendar.ATTACHMENTS_ENTITY_TABLE ,
    foreignKeys =[
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [ConstantCalendar.EVENT_ENTITY_PRIMARY_KEY],
            childColumns = [ConstantCalendar.ATTACHMENT_ENTITY_TABLE_FOREIGN_KEY],
            onDelete = ForeignKey.CASCADE ,
            onUpdate = ForeignKey.CASCADE
        )
    ] ,
    indices = [Index(value = [ConstantCalendar.ATTACHMENT_ENTITY_TABLE_FOREIGN_KEY])]
)
data class AttachmentsEntity(
    @PrimaryKey(autoGenerate = true)  val attachId : Long = 0 ,
    val attachmentOwnerId  : Long ,
    val fileUrl : String?,
    val title : String?,
    val mimeType: String?,
    val iconLink : String?,
    val fileId : String?
)