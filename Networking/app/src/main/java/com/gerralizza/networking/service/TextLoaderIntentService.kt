package com.gerralizza.networking.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log

class TextLoaderIntentService : IntentService("TextLoader") {
    init {
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra(KEY_URL) ?: return

//        val text = URL(url)
//            .openConnection()
//            .getInputStream()
//            .reader()
//            .readText()

        // TextLoaderIntentService: thread: IntentService[TextLoader]

        Log.d(TAG, "thread: ${Thread.currentThread().name}")
    }

    companion object {
        private const val TAG = "TextLoaderIntentService"

        private const val KEY_URL = "url"

        fun createLoadIntent(context: Context, url: String): Intent {
            return Intent(context, TextLoaderIntentService::class.java)
                .putExtra(KEY_URL, url)
        }
    }
}