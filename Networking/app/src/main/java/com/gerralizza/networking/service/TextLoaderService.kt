package com.gerralizza.networking.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.annotation.UiThread

open class TextLoaderService : Service() {
    private var textLoader: TextLoader? = null

    override fun onCreate() {
        Log.d(TAG, "onCreate()")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(KEY_URL)

        Log.d(TAG, "onStartCommand, startId=$startId, url=$url")

        if (url == null) {
            stopSelf(startId)
            return START_NOT_STICKY
        }

        textLoader = TextLoader(url, startId).apply {
            execute()
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy()")
        textLoader?.cancel(true)
        textLoader = null
        super.onDestroy()
    }

    @UiThread
    open fun onTextLoaded(text: String?) {
        Log.d(TAG, "text: $text")
    }

    private inner class TextLoader(val url: String, val startId: Int) : AsyncTask<Unit, Unit, String>() {
        override fun doInBackground(vararg params: Unit?): String {
            SystemClock.sleep(1000L)
            return "Hello from service"
//            return URL(url)
//                .openConnection()
//                .getInputStream()
//                .reader()
//                .readText()
        }

        override fun onPostExecute(result: String?) {
            onTextLoaded(result)
//            stopSelf(startId)
        }
    }

    companion object {
        private const val TAG = "TextLoaderService"

        private const val KEY_URL = "url"

        fun createLoadIntent(context: Context, url: String): Intent {
            return Intent(context, TextLoaderService::class.java)
                .putExtra(KEY_URL, url)
        }
    }
}