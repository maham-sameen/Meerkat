package com.example.meerkat.feature_auth.domain.use_case.auth

import com.example.meerkat.feature_auth.data.repository.FakeUserRepo
import com.example.meerkat.feature_auth.domain.model.AuthResult
import com.example.meerkat.feature_auth.domain.model.User
import com.example.meerkat.feature_auth.domain.repository.UserRepo
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MakeUniqueAccountTest {

    private lateinit var repository: UserRepo
    private lateinit var makeUniqueAccount: MakeUniqueAccount

    @Before
    fun setup() {
        repository = FakeUserRepo()
        makeUniqueAccount = MakeUniqueAccount(repository)
    }

    @Test
    fun `invoke new users inserts correctly`() = runTest {
        val user1 = User("1", "Alice", "alice@example.com", "img1")
        val user2 = User("2", "", "faris@example.com", "img2")
        val newUser = User("3", "Maham", "maham@gmail.com", "")

        repository.saveUser(user1)
        repository.saveUser(user2)

        val authResult = makeUniqueAccount(
            users = repository.getAllUsers(),
            authResult = AuthResult(data = newUser, errorMessage = null)
        )

        // Assert returned AuthResult is correct
        assertEquals(newUser.id, authResult.data?.id)
        assertEquals(newUser.name, authResult.data?.name)
        assertEquals(newUser.email, authResult.data?.email)
        assertEquals(newUser.image, authResult.data?.image)
        assertEquals(null, authResult.errorMessage)

        // Assert all users exist in repo
        val allUsers = repository.getAllUsers()
        assertTrue(allUsers.containsAll(listOf(user1, user2, newUser)))
    }

    @Test
    fun `duplicate user login merges correctly`() = runTest {
        val user1 = User("1", "Alice", "alice@example.com", "img1")
        val user2 = User("2", "", "maham@gmail.com", "")
        val newUser = User("3", "Maham", "maham@gmail.com", "image2")

        repository.saveUser(user1)
        repository.saveUser(user2)

        val authResult = makeUniqueAccount(
            users = repository.getAllUsers(),
            authResult = AuthResult(data = newUser, errorMessage = null)
        )

        // The merge logic picks the latest non-empty fields
        val expectedMergedUser = User(
            id = "3",
            name = "Maham",
            email = "maham@gmail.com",
            image = "image2"
        )

        // Assert returned AuthResult
        assertEquals(expectedMergedUser.id, authResult.data?.id)
        assertEquals(expectedMergedUser.name, authResult.data?.name)
        assertEquals(expectedMergedUser.email, authResult.data?.email)
        assertEquals(expectedMergedUser.image, authResult.data?.image)
        assertEquals(null, authResult.errorMessage)

        // Assert repository contains the unique users (user1 + merged)
        val allUsers = repository.getAllUsers()
        assertTrue(allUsers.contains(user1))
        assertTrue(allUsers.contains(expectedMergedUser))
        assertEquals(2, allUsers.size) // duplicate was removed
    }

    @Test
    fun `invoke with null authResult data returns same authResult`() = runTest {
        val authResult = AuthResult(data = null, errorMessage = null)
        val result = makeUniqueAccount(users = repository.getAllUsers(), authResult = authResult)
        assertEquals(authResult, result)
    }
}
