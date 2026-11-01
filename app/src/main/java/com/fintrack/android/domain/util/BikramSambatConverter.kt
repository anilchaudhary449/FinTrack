package com.fintrack.android.domain.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

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

    // Approximate days in months for standard BS reference years
    private val DAYS_IN_BS_MONTH = intArrayOf(31, 31, 32, 31, 31, 30, 30, 29, 30, 29, 30, 30)

    fun toBikramSambat(epochMillis: Long, inNepaliScript: Boolean = false): String {
        val targetDate = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val daysDiff = ChronoUnit.DAYS.between(BASE_GREGORIAN, targetDate).toInt()

        var bsYear = BASE_BS_YEAR
        var bsMonth = BASE_BS_MONTH
        var bsDay = BASE_BS_DAY + daysDiff

        while (bsDay > 30) {
            val daysInMonth = DAYS_IN_BS_MONTH[(bsMonth - 1) % 12]
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
            val daysInPrevMonth = DAYS_IN_BS_MONTH[(bsMonth - 1) % 12]
            bsDay += daysInPrevMonth
        }

        val monthName = if (inNepaliScript) NEPALI_MONTHS_NEP[bsMonth - 1] else NEPALI_MONTHS[bsMonth - 1]

        return if (inNepaliScript) {
            "${toNepaliNumerals(bsDay)} $monthName ${toNepaliNumerals(bsYear)}"
        } else {
            "$bsDay $monthName $bsYear BS"
        }
    }

    fun toNepaliNumerals(number: Int): String {
        val devanagariDigits = charArrayOf('०', '१', '२', '३', '४', '५', '६', '७', '८', '९')
        return number.toString().map { char ->
            if (char.isDigit()) devanagariDigits[char - '0'] else char
        }.joinToString("")
    }
}

