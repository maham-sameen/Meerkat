package com.example.meerkat.core.calendar.data.data_source.mapper

import com.example.meerkat.core.calendar.data.data_source.model.local.AttachmentsEntity
import com.example.meerkat.core.calendar.data.data_source.model.remote.AttachmentsDto

class AttachmentDtoToAttachmentEntityMapper :
    Mapper<Pair<AttachmentsDto, Long>, AttachmentsEntity> {
    override fun map(from: Pair<AttachmentsDto, Long>): AttachmentsEntity {
        val (attachment, ownerId) = from
        return AttachmentsEntity(
            attachmentOwnerId = ownerId,
            fileUrl = attachment.fileUrl,
            title = attachment.title,
            mimeType = attachment.mimeType,
            iconLink = attachment.iconLink,
            fileId = attachment.fileId
        )
    }
}