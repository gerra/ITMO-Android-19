package com.gerralizza.networking

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gerralizza.networking.asynctask.AsyncTaskActivity
import com.gerralizza.networking.rates.ExchangeRatesActivity
import com.gerralizza.networking.service.ServiceTestActivity
import java.net.URL
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.freeze_ui_btn).setOnClickListener {
            SystemClock.sleep(TimeUnit.SECONDS.toMillis(2))
        }

        findViewById<View>(R.id.download_in_ui).setOnClickListener {
            URL("https://api.ipify.org/").openConnection().connect()
        }

        findViewById<View>(R.id.show_ip_thread_btn).setOnClickListener {
            downloadUsingThread()
        }

        findViewById<View>(R.id.services_btn).setOnClickListener {
            startActivity(Intent(this, ServiceTestActivity::class.java))
        }

        findViewById<View>(R.id.async_task_btn).setOnClickListener {
            startActivity(Intent(this, AsyncTaskActivity::class.java))
        }

        findViewById<View>(R.id.rates_btn).setOnClickListener {
            startActivity(Intent(this, ExchangeRatesActivity::class.java))
        }
    }

    private fun downloadUsingThread() {
        Thread(Runnable {
            val ip = URL("https://api.ipify.org/")
                .openConnection()
                .getInputStream()
                .reader()
                .readText()

            Log.d("MainActivity", "Thread: ${Thread.currentThread().name}, ip: $ip")

            runOnUiThread {
                Log.d("MainActivity", "Thread: ${Thread.currentThread().name}, ip: $ip")
            }
        }).start()
    }
}
