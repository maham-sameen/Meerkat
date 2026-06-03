package com.example.meerkat.feature_auth.domain.use_case

import com.example.meerkat.feature_auth.domain.util.PasswordStrengthLevels
import org.junit.Before
import org.junit.Test

class CheckPasswordStrengthTest {
    private lateinit var checkPasswordStrength: CheckPasswordStrength


    @Before
    fun setUp(){
        checkPasswordStrength = CheckPasswordStrength()
    }

    @Test
    fun `empty password return LOW`(){
        assert(checkPasswordStrength("")== PasswordStrengthLevels.LOW)
    }

    @Test
    fun `null password return LOW`(){
        assert(checkPasswordStrength(null) == PasswordStrengthLevels.LOW)
    }

    @Test
    fun `weak password return LOW`(){
        assert(checkPasswordStrength("abc")== PasswordStrengthLevels.LOW)
    }

    @Test
    fun `password  length 8 and 2 category return FAIR`(){
        assert(checkPasswordStrength("abc12345") == PasswordStrengthLevels.FAIR)
    }

    @Test
    fun `password  length 12 and 3 category return GOOD`(){
        assert(checkPasswordStrength("Abcdef123456")== PasswordStrengthLevels.GOOD)
    }

    @Test
    fun `password  length 16 and 4 category return EXCELLENT`(){
        assert(checkPasswordStrength("Abcdef123456!@#//")== PasswordStrengthLevels.EXCELLENT)
    }

    @Test
    fun `password  1 category return LOW`(){
        assert(checkPasswordStrength("!!!!!!!!!!") == PasswordStrengthLevels.LOW)
    }

    @Test
    fun `password  length 8 but only 1 category return LOW`(){
        assert(checkPasswordStrength("abcdefgh")== PasswordStrengthLevels.LOW)
    }

    @Test
    fun `password length 12 and only 2 category return FAIR`(){
        assert(checkPasswordStrength("abcdefABCDEF")== PasswordStrengthLevels.FAIR)
    }

    @Test
    fun `password length 16 and only 3 category return GOOD`(){
        assert(checkPasswordStrength("abcdefABCDEF1234")== PasswordStrengthLevels.GOOD)
    }
}