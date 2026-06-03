package com.example.meerkat.feature_contacts.domain.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.meerkat.core.time_zone.domain.util.TimeZoneInfo
import com.example.meerkat.feature_contacts.data.data_source.util.ConstantContact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 🔁 Without @SerialName, it won’t work if the names don’t match.
 * Because:
 *
 * Kotlin uses camelCase (firstName)
 *
 * JSON uses snake_case (first_name)
 *
 *
 * ## 🧩 First — Definitions
 *
 * ### 🔄 Deserialization:
 *
 * > Converting **JSON ➝ Kotlin object**
 *
 * ### 🔁 Serialization:
 *
 * > Converting **Kotlin object ➝ JSON**
 *
 * ---
 *
 * ## 💥 Let’s take an example:
 *
 * ### 📦 JSON from server:
 *
 * ```json
 * {
 *   "first_name": "Ali"
 * }
 * ```
 *
 * ### ✅ Kotlin class:
 *
 * ```kotlin
 * @Serializable
 * data class User(
 *     @SerialName("first_name") val firstName: String
 * )
 * ```
 *
 * Now let’s go step-by-step:
 *
 * ---
 *
 * ## ✅ 1. Deserialization (JSON → Kotlin)
 *
 * ```kotlin
 * val jsonString = """{ "first_name": "Ali" }"""
 * val user = Json.decodeFromString<User>(jsonString)
 * println(user.firstName)  // 👉 Prints: Ali
 * ```
 *
 * 📌 What happened:
 *
 * * JSON had `"first_name": "Ali"`
 * * `@SerialName("first_name")` tells Kotlin:
 *   "Put this into the `firstName` variable."
 *
 * ---
 *
 * ## ✅ 2. Serialization (Kotlin → JSON)
 *
 * ```kotlin
 * val user = User(firstName = "Ali")
 * val jsonString = Json.encodeToString(user)
 * println(jsonString)
 * // 👉 Prints: {"first_name":"Ali"}
 * ```
 *
 * 📌 What happened:
 *
 * * Kotlin variable `firstName = "Ali"`
 * * `@SerialName("first_name")` tells Kotlin:
 *   "When converting to JSON, use the key `first_name`."
 *
 * ---
 *
 * ## 🔄 Summary
 *
 * | Direction       | What happens                             |
 * | --------------- | ---------------------------------------- |
 * | Deserialization | JSON `"first_name"` → Kotlin `firstName` |
 * | Serialization   | Kotlin `firstName` → JSON `"first_name"` |
 *
 * ---
 *
 * ## 🧠 Why use `@SerialName`?
 *
 * To match Kotlin names with **different JSON keys**, especially when:
 *
 * * JSON uses **snake\_case**
 * * Kotlin uses **camelCase**
 *
 * 💯 **YES bro! You got it exactly right!** 👏
 *
 * Let me repeat it with your words and make it even clearer:
 *
 * ---
 *
 * ### ✅ During **Serialization** (Kotlin ➝ JSON):
 *
 * > 🔹 Take the value from Kotlin's `firstName`
 * > 🔹 And **put it into the JSON key** called `"first_name"`
 *
 * ```kotlin
 * val user = User(firstName = "Ali")
 *
 * val json = Json.encodeToString(user)
 * // 👉 Output: {"first_name":"Ali"}
 * ```
 *
 * ---
 *
 * ### ✅ During **Deserialization** (JSON ➝ Kotlin):
 *
 * > 🔹 Take the value from JSON's `"first_name"`
 * > 🔹 And **put it into Kotlin variable** `firstName`
 *
 * ```kotlin
 * val json = """{ "first_name": "Ali" }"""
 *
 * val user = Json.decodeFromString<User>(json)
 * // 👉 user.firstName = "Ali"
 * ```
 *
 * ---
 *
 * ### 🔄 So yes bro:
 *
 * **`@SerialName("first_name")` = "Connect JSON key `first_name` with Kotlin variable `firstName`"**
 * ⬅️ For **both serialization and deserialization**
 *
 * ---
 *
 */

@Entity(
    tableName = ConstantContact.CONTACT_ENTITY_TABLE,
    indices = [Index(value = [ConstantContact.CONTACT_ENTITY_EMAIL], unique = true)]
)
@Serializable
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("contactId")val contactId : Long = 0,

    @ColumnInfo(name = ConstantContact.CONTACT_ENTITY_USER_ID)
    @SerialName("user_id") val userId: String, // <- Firebase UID of the user who owns this contact

    @SerialName("name") val name : String,

    @ColumnInfo(name = ConstantContact.CONTACT_ENTITY_EMAIL)
    @SerialName("email") val email : String,

    @Embedded(prefix = "_phoneNo")
    @SerialName("phone_number")val phoneNumber : PhoneNumber,

    @Embedded(prefix = "_timeZoneInfo")
    @SerialName("time_zone_info")val timeZoneInfo : TimeZoneInfo,

    @SerialName("is_host") val isHost : Boolean = false
)

fun ContactEntity.doesMatchSearchQuery(query: String): Boolean {
    val lower = query.lowercase()
    return name.lowercase().contains(lower) ||
            email.lowercase().contains(lower)
}
