package com.example.meerkat.feature_contacts.domain.use_case

import com.example.meerkat.feature_contacts.domain.model.ContactEntity
import com.example.meerkat.feature_contacts.domain.repository.ContactRepo

class InsertNewContact(
    private val contactRepo: ContactRepo ,
) {
    suspend  operator fun  invoke(
        contactEntity: ContactEntity
    ){
        println("\uD83C\uDF82 [InsertNewContact] , contactEntity :  ${contactEntity.toString()} ")
        contactRepo.insert(
            contactEntity
        )
    }
}