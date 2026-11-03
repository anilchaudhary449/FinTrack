package com.fintrack.android

import com.fintrack.android.domain.util.BikramSambatConverter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class BikramSambatConverterTest {

    @Test
    fun `nepali numerals convert digits 0 through 9 correctly`() {
        val converted = BikramSambatConverter.toNepaliNumerals(2081)
        assertEquals("२०८१", converted)
    }

    @Test
    fun `nepali numerals convert 1234567890 correctly`() {
        val converted = BikramSambatConverter.toNepaliNumerals(12345)
        assertEquals("१२३४५", converted)
    }

    @Test
    fun `gregorian base date converts to 2081 BS`() {
        val zone = ZoneId.systemDefault()
        // 2024-04-13 is base date 2081-01-01 BS
        val millis = LocalDate.of(2024, 4, 13).atStartOfDay(zone).toInstant().toEpochMilli()
        val bsString = BikramSambatConverter.toBikramSambat(millis)

        assertTrue(bsString.contains("2081 BS"))
        assertTrue(bsString.contains("Baisakh"))
    }

    @Test
    fun `date conversion exposes day and month for dual calendar display`() {
        val converted = BikramSambatConverter.toBikramSambatDate(LocalDate.of(2024, 4, 13))

        assertEquals(2081, converted.year)
        assertEquals(1, converted.month)
        assertEquals(1, converted.day)
        assertEquals("1 Baisakh 2081 BS", BikramSambatConverter.format(converted))
    }

    @Test
    fun `modern calendar conversion uses year-specific month lengths`() {
        val converted = BikramSambatConverter.toBikramSambatDate(LocalDate.of(2026, 11, 26))

        assertEquals(2083, converted.year)
        assertEquals(8, converted.month)
        assertEquals(10, converted.day)
        assertEquals("10 Mangsir 2083 BS", BikramSambatConverter.format(converted))
    }
}

