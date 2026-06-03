package com.example.meerkat.core.calendar.domain.repository


interface CalendarRepo {
    suspend fun <T: Any> insert(entity : T) : Long
    suspend fun <T : Any> insert(entities : List<T>)

    suspend fun <T : Any> deleteEntity(entity : T)

    suspend fun <T : Any> updateEntity(entity : T)
    suspend fun <T : Any> upsert(entity: T) : Long
    suspend fun <T : Any> upsertAll(entities: List<T>)

}