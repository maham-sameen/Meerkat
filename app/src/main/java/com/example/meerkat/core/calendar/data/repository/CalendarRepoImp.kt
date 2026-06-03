package com.example.meerkat.core.calendar.data.repository

import com.example.meerkat.core.calendar.data.data_source.local.AttachmentDao
import com.example.meerkat.core.calendar.data.data_source.local.AttendeeDao
import com.example.meerkat.core.calendar.data.data_source.local.BaseDao
import com.example.meerkat.core.calendar.data.data_source.local.CalendarDao
import com.example.meerkat.core.calendar.data.data_source.local.ConferenceDataDao
import com.example.meerkat.core.calendar.data.data_source.local.EntryPointDao
import com.example.meerkat.core.calendar.data.data_source.local.EventDao
import com.example.meerkat.core.calendar.data.data_source.local.OverrideDao
import com.example.meerkat.core.calendar.data.data_source.local.ReminderDao
import com.example.meerkat.core.calendar.data.data_source.model.local.AttachmentsEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.AttendeeEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.CalendarEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.ConferenceDataEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EntryPointEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.EventEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.OverrideEntity
import com.example.meerkat.core.calendar.data.data_source.model.local.ReminderEntity
import com.example.meerkat.core.calendar.domain.repository.CalendarRepo
import kotlin.reflect.KClass

class CalendarRepoImp(
    private val attachmentDao: AttachmentDao,
    private val attendeeDao: AttendeeDao,
    private val conferenceDataDao: ConferenceDataDao,
    private val entryPointDao: EntryPointDao,
    private val eventDao: EventDao,
    private val calendarDao: CalendarDao,
    private val overrideDao: OverrideDao,
    private val reminderDao: ReminderDao,
) : CalendarRepo {

    // Maps entity class → DAO
    private val daoMap: Map<KClass<*>, BaseDao<*>> = mapOf(
        AttachmentsEntity::class to attachmentDao,
        AttendeeEntity::class to attendeeDao,
        ConferenceDataEntity::class to conferenceDataDao,
        OverrideEntity::class to overrideDao,
        CalendarEntity::class to calendarDao,
        EventEntity::class to eventDao,
        EntryPointEntity::class to entryPointDao,
        ReminderEntity::class to reminderDao ,
    )

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getDaoForClass(clazz: KClass<out T>): BaseDao<T>? {
        return daoMap[clazz] as? BaseDao<T> ?: error("No DAO found for type $clazz")
    }

    override suspend fun <T : Any> insert(entity: T) : Long {
        val dao = getDaoForClass(entity::class)
            ?: error("No DAO found for type ${entity::class}")
        return dao.insert(entity)
    }

    override suspend fun <T : Any> insert(entities: List<T>) {
        if (entities.isEmpty()) return

        val firstClass = entities.first()::class
        require(entities.all { it::class == firstClass }) {
            "All entities must be of the same type for bulk insert"
        }

        val dao = getDaoForClass(firstClass)
            ?: error("No DAO found for type $firstClass")

        dao.insertAll(entities)
    }


    override suspend fun <T : Any> deleteEntity(entity: T) {
        val dao = getDaoForClass(entity::class)
            ?: error("No DAO found for type ${entity::class}")
        dao.delete(entity)
    }

    override suspend fun <T : Any> updateEntity(entity: T) {
        val dao = getDaoForClass(entity::class)
            ?: error("No DAO found for type ${entity::class}")
        dao.update(entity)
    }

    override suspend fun <T : Any> upsert(entity: T) : Long{
        val dao = getDaoForClass(entity::class)
            ?: error("No DAO found for type ${entity::class}")
       return dao.upsert(entity)
    }

    override suspend fun <T : Any> upsertAll(entities: List<T>) {
        if (entities.isEmpty()) return

        val firstClass = entities.first()::class
        require(entities.all { it::class == firstClass }) {
            "All entities must be of the same type for bulk upsert"
        }

        val dao = getDaoForClass(firstClass)
            ?: error("No DAO found for type $firstClass")
        dao.upsertAll(entities)
    }

}