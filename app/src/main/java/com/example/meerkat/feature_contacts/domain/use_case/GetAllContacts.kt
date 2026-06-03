package com.example.meerkat.feature_contacts.domain.use_case

import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged


class GetAllContacts (
    private val contactRepo: ContactRepo
) {

    /**
     *
     *     false means emit , true means not  emit
     *
     *    if list size are same then we take the pair of the list and iterate over all
     *
     * 1️⃣ zip(): Pairs up elements from two lists
     * kotlin
     * Copy
     * Edit
     * val oldList = [A, B, C]
     * val newList = [A, B, C]
     * oldList.zip(newList) becomes:
     *
     * kotlin
     * Copy
     * Edit
     * [(A, A), (B, B), (C, C)]
     * ➡️ It makes a list of pairs. Each pair is from the same position in both lists.
     *
     * So:
     *
     * First pair → (old = A, new = A)
     *
     * Second pair → (old = B, new = B)
     *
     * Third pair → (old = C, new = C)
     *
     * 2️⃣ .all { (old, new) -> old == new }
     * Now we go through each pair and ask:
     *
     * Are all the old and new items equal?
     *
     * If every single pair matches → returns true.
     * If even one pair is different → returns false.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
     operator fun invoke(result: String): Flow<List<ContactEntity>> {
         println("\uD83C\uDF82 [GetAllContacts] , result : ${result.toString()}")
        return contactRepo
            .getAllContact(result)
            .distinctUntilChanged { oldList, newList ->
                if (oldList.size != newList.size) return@distinctUntilChanged false
                oldList.zip(newList).all { (old, new) -> old == new }
            }
    }
}



