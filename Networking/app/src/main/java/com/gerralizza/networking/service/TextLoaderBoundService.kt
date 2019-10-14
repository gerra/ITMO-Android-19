package com.gerralizza.networking.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.UiThread

class TextLoaderBoundService : TextLoaderService() {
    var listener: ((String?) -> Unit)? = null

    override fun onBind(intent: Intent?): IBinder? {
        return BinderImpl(this)
    }

    @UiThread
    override fun onTextLoaded(text: String?) {
        listener?.invoke(text)
    }

    class BinderImpl(val service: TextLoaderBoundService) : Binder()

    companion object {
        private const val KEY_URL = "url"

        fun createLoadIntent(context: Context, url: String): Intent {
            return Intent(context, TextLoaderBoundService::class.java)
                .putExtra(KEY_URL, url)
        }

        fun createBindIntent(context: Context): Intent {
            return Intent(context, TextLoaderBoundService::class.java)
        }
    }
}