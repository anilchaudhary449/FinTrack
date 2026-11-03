package com.fintrack.android.data.repository

import android.content.Context
import com.fintrack.android.domain.model.ExchangeRateSnapshot
import com.fintrack.android.domain.repository.ExchangeRateRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : ExchangeRateRepository {

    private val cache = context.getSharedPreferences("exchange_rates", Context.MODE_PRIVATE)
    private val _snapshot = MutableStateFlow(readCachedSnapshot())
    override val snapshot: StateFlow<ExchangeRateSnapshot?> = _snapshot.asStateFlow()

    override suspend fun refreshIfStale() = withContext(Dispatchers.IO) {
        val current = _snapshot.value
        val cacheAge = current?.let { System.currentTimeMillis() - it.updatedAtMillis } ?: Long.MAX_VALUE
        if (cacheAge < CACHE_MAX_AGE_MILLIS) return@withContext

        val connection = URL(RATES_URL).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 8_000
            connection.readTimeout = 8_000
            connection.requestMethod = "GET"
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val payload = JSONObject(response)
            check(payload.optString("result") == "success") { "Exchange rate service returned an error" }

            val baseCurrency = payload.optString("base_code", "USD").uppercase()
            val rateObject = payload.getJSONObject("rates")
            val rates = buildMap {
                val keys = rateObject.keys()
                while (keys.hasNext()) {
                    val code = keys.next().uppercase()
                    put(code, BigDecimal(rateObject.getDouble(code).toString()))
                }
            }
            val updatedAt = payload.optLong("time_last_update_unix", System.currentTimeMillis() / 1000) * 1000
            val freshSnapshot = ExchangeRateSnapshot(baseCurrency, rates, updatedAt)
            cache.edit()
                .putString(KEY_BASE, baseCurrency)
                .putString(KEY_RATES, rateObject.toString())
                .putLong(KEY_UPDATED_AT, updatedAt)
                .apply()
            _snapshot.value = freshSnapshot
        } finally {
            connection.disconnect()
        }
    }

    private fun readCachedSnapshot(): ExchangeRateSnapshot? {
        val base = cache.getString(KEY_BASE, null) ?: return null
        val rateText = cache.getString(KEY_RATES, null) ?: return null
        val updatedAt = cache.getLong(KEY_UPDATED_AT, 0L)
        return runCatching {
            val rateObject = JSONObject(rateText)
            val rates = buildMap {
                val keys = rateObject.keys()
                while (keys.hasNext()) {
                    val code = keys.next().uppercase()
                    put(code, BigDecimal(rateObject.getDouble(code).toString()))
                }
            }
            ExchangeRateSnapshot(base, rates, updatedAt)
        }.getOrNull()
    }

    private companion object {
        const val RATES_URL = "https://open.er-api.com/v6/latest/USD"
        const val CACHE_MAX_AGE_MILLIS = 6 * 60 * 60 * 1000L
        const val KEY_BASE = "base"
        const val KEY_RATES = "rates"
        const val KEY_UPDATED_AT = "updated_at"
    }
}