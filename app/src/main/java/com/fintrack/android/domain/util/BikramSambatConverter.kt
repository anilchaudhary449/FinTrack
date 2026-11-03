package com.fintrack.android.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class BikramSambatDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val monthName: String
)

object BikramSambatConverter {

    private val NEPALI_MONTHS = listOf(
        "Baisakh", "Jestha", "Ashadh", "Shrawan", "Bhadra", "Ashwin",
        "Kartik", "Mangsir", "Poush", "Magh", "Falgun", "Chaitra"
    )

    private val NEPALI_MONTHS_NEP = listOf(
        "वैशाख", "जेठ", "असार", "श्रावण", "भाद्र", "आश्विन",
        "कार्तिक", "मंसिर", "पौष", "माघ", "फाल्गुन", "चैत्र"
    )

    // Accurate base reference point:
    // 2024-04-13 AD corresponds to 2081-01-01 BS (New Year in Nepal)
    private val BASE_GREGORIAN = LocalDate.of(2024, 4, 13)
    private const val BASE_BS_YEAR = 2081
    private const val BASE_BS_MONTH = 1
    private const val BASE_BS_DAY = 1

    private val DAYS_IN_BS_MONTH = intArrayOf(31, 31, 32, 31, 31, 30, 30, 29, 30, 29, 30, 30)
    private val DAYS_IN_BS_MONTH_BY_YEAR = mapOf(
        2080 to intArrayOf(31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
        2081 to intArrayOf(31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
        2082 to intArrayOf(31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
        2083 to intArrayOf(31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
        2084 to intArrayOf(31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30),
        2085 to intArrayOf(31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30),
        2086 to intArrayOf(30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30),
        2087 to intArrayOf(31, 31, 32, 31, 31, 31, 30, 29, 30, 30, 30, 30),
        2088 to intArrayOf(30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30),
        2089 to intArrayOf(30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30),
        2090 to intArrayOf(30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30)
    )

    fun toBikramSambat(epochMillis: Long, inNepaliScript: Boolean = false): String {
        val targetDate = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return format(toBikramSambatDate(targetDate), inNepaliScript)
    }

    fun toBikramSambatDate(targetDate: LocalDate): BikramSambatDate {
        val daysDiff = ChronoUnit.DAYS.between(BASE_GREGORIAN, targetDate).toInt()

        var bsYear = BASE_BS_YEAR
        var bsMonth = BASE_BS_MONTH
        var bsDay = BASE_BS_DAY + daysDiff

        while (bsDay > daysInMonth(bsYear, bsMonth)) {
            val daysInMonth = daysInMonth(bsYear, bsMonth)
            if (bsDay > daysInMonth) {
                bsDay -= daysInMonth
                bsMonth++
                if (bsMonth > 12) {
                    bsMonth = 1
                    bsYear++
                }
            } else {
                break
            }
        }

        while (bsDay <= 0) {
            bsMonth--
            if (bsMonth < 1) {
                bsMonth = 12
                bsYear--
            }
            val daysInPrevMonth = daysInMonth(bsYear, bsMonth)
            bsDay += daysInPrevMonth
        }

        return BikramSambatDate(
            year = bsYear,
            month = bsMonth,
            day = bsDay,
            monthName = NEPALI_MONTHS[bsMonth - 1]
        )
    }

    fun format(date: BikramSambatDate, inNepaliScript: Boolean = false): String {
        val monthName = if (inNepaliScript) NEPALI_MONTHS_NEP[date.month - 1] else date.monthName
        return if (inNepaliScript) {
            "${toNepaliNumerals(date.day)} $monthName ${toNepaliNumerals(date.year)}"
        } else {
            "${date.day} $monthName ${date.year} BS"
        }
    }

    fun toNepaliNumerals(number: Int): String {
        val devanagariDigits = charArrayOf('०', '१', '२', '३', '४', '५', '६', '७', '८', '९')
        return number.toString().map { char ->
            if (char.isDigit()) devanagariDigits[char - '0'] else char
        }.joinToString("")
    }

    private fun daysInMonth(year: Int, month: Int): Int =
        DAYS_IN_BS_MONTH_BY_YEAR[year]?.get(month - 1) ?: DAYS_IN_BS_MONTH[month - 1]
}

