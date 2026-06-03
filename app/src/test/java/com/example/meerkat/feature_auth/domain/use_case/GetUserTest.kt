package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.data.repository.FakeUserRepo
import com.example.meerkat.feature_auth.domain.model.User
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetUserTest {

    private lateinit var getUser : GetUser
    private lateinit var fakeUserRepo: FakeUserRepo

    @Before
    fun setUp(){
        fakeUserRepo  = FakeUserRepo()
        getUser = GetUser(fakeUserRepo)
    }


    @Test
    fun `Get  existed user by id return non null user`() = runTest{
        val user = User(
            id = "123",
            name = "user",
            email = "user@gmail.com",
            image = "image"
        )
        fakeUserRepo.saveUser(user)
        assert(getUser(user.id) != null)
    }

    @Test
    fun `Get non existed user by id return null`()=runTest{
        assert(getUser("123")==null)
    }
}