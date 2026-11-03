package com.fintrack.android

import com.fintrack.android.data.security.PinSecurity
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PinSecurityTest {
    @Test
    fun `hashed pin verifies without storing the pin text`() {
        val hash = PinSecurity.hash("4826")

        assertTrue(PinSecurity.matches("4826", hash))
        assertFalse(PinSecurity.matches("4827", hash))
        assertFalse(hash.contains("4826"))
    }

    @Test
    fun `pin hashes use unique salts`() {
        assertNotEquals(PinSecurity.hash("4826"), PinSecurity.hash("4826"))
    }

    @Test
    fun `only four to eight digit pins are accepted`() {
        assertFalse(PinSecurity.isValidPin("123"))
        assertFalse(PinSecurity.isValidPin("123456789"))
        assertFalse(PinSecurity.isValidPin("12a4"))
        assertTrue(PinSecurity.isValidPin("1234"))
    }
}