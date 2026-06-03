package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.data.repository.FakeUserRepo
import com.example.meerkat.feature_auth.domain.model.User
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetUsersTest {
    private  lateinit var getUsers : GetUsers
    private lateinit var fakeUserRepo: FakeUserRepo

    @Before
    fun setUp(){
        fakeUserRepo = FakeUserRepo()
        getUsers = GetUsers(fakeUserRepo)
    }


    @Test
    fun `invoke returns all users`() = runTest {
        val user = User("1", "test@example.com")
        fakeUserRepo.saveUser(user)

        val result = getUsers()
        assertThat(result).contains(user)
    }
}