package com.example.meerkat.feature_meetings.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Compares two lists element by element using equals,
 * and only emits when the list actually changes in content.
 */
fun <T> Flow<List<T>>.distinctUntilListChanged(): Flow<List<T>> {
    return distinctUntilChanged { oldList, newList ->
        if (oldList.size != newList.size) return@distinctUntilChanged false
        oldList.zip(newList).all { (old, new) -> old == new }
    }
}
