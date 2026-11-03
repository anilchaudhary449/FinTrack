package com.fintrack.android.data.security

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.util.Base64

object PinSecurity {
    fun isValidPin(pin: String): Boolean = pin.matches(Regex("^\\d{4,8}$"))

    fun hash(pin: String): String {
        require(isValidPin(pin)) { "PIN must contain 4 to 8 digits" }
        val salt = ByteArray(16).also(SecureRandom()::nextBytes)
        val digest = derive(pin, salt)
        return "v2:$ITERATIONS:${Base64.getEncoder().encodeToString(salt)}:${Base64.getEncoder().encodeToString(digest)}"
    }

    fun matches(pin: String, storedHash: String): Boolean {
        val parts = storedHash.split(':')
        if (parts.size != 4 || parts[0] != "v2" || parts[1] != ITERATIONS.toString() ||
            !pin.matches(Regex("^\\d{4,8}$"))
        ) return false
        return runCatching {
            val salt = Base64.getDecoder().decode(parts[2])
            val expected = Base64.getDecoder().decode(parts[3])
            val actual = derive(pin, salt)
            MessageDigest.isEqual(expected, actual)
        }.getOrDefault(false)
    }

    private fun derive(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
}