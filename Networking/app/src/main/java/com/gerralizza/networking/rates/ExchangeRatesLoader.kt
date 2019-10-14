package com.gerralizza.networking.rates

import android.os.AsyncTask
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class ExchangeRatesLoader(private val url: String)
    : AsyncTask<Unit, Unit, ExchangeRates>() {
    var notPublishedResult: ExchangeRates? = null
    var listener: ((ExchangeRates?) -> Unit)? = null
        set(value) {
            if (value != null) {
                notPublishedResult?.let {
                    value.invoke(it)
                    notPublishedResult = null
                }
            }

            field = value
        }

    override fun doInBackground(vararg params: Unit?): ExchangeRates? {
        val connection = URL(url)
            .openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val response = connection.inputStream.reader().readText()
        val json = JSONObject(response)

        val ratesJson = json.getJSONObject("rates")
        val rates = ArrayList<ExchangeRate>(ratesJson.length())
        ratesJson.keys().forEach {
            rates.add(ExchangeRate(it, 1f / ratesJson.getDouble(it).toFloat()))
        }

        val date = DATE_FORMAT.parse(json.getString("date"))
        return ExchangeRates(
            json.getString("base"),
            date,
            rates
        )
    }

    override fun onPostExecute(result: ExchangeRates) {
        listener?.invoke(result) ?: run {
            notPublishedResult = result
        }
    }

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")
    }
}