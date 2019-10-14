package com.gerralizza.networking.rates

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gerralizza.networking.R

class ExchangeRatesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExchangeRatesAdapter

    private val loadListener: (ExchangeRates?) -> Unit = {
        adapter.setRates(it?.rates ?: emptyList())
        loader = null
    }

    private var loader: ExchangeRatesLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rates)

        recyclerView = findViewById(R.id.recycler)
        adapter = ExchangeRatesAdapter()

        (savedInstanceState?.getSerializable(KEY_RATES) as? ArrayList<ExchangeRate>)?.let {
            adapter.setRates(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        findViewById<View>(R.id.reload_btn).setOnClickListener {
            load()
        }

        if (savedInstanceState == null) {
            load()
        } else {
            loader = (lastCustomNonConfigurationInstance as? ExchangeRatesLoader)
            loader?.listener = loadListener
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_RATES, adapter.rates)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return loader
    }

    private fun load() {
        loader?.cancel(true)
        loader = ExchangeRatesLoader("https://api.ratesapi.io/api/latest?base=RUB").apply {
            listener = loadListener
            execute()
        }
    }

    companion object {
        private const val KEY_RATES = "rates"
    }
}