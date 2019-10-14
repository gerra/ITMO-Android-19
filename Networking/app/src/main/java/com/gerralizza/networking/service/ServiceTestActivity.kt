package com.gerralizza.networking.service

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gerralizza.networking.R

class ServiceTestActivity : AppCompatActivity() {
    private lateinit var ipTextView: TextView

    private val serviceConnection =
        ServiceConnectionImpl {
            ipTextView.text = it
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        ipTextView = findViewById(R.id.ip_txt)

        findViewById<View>(R.id.clear_ip).setOnClickListener {
            ipTextView.text = ""
        }

        findViewById<View>(R.id.start_service_btn).setOnClickListener {
            startService()
        }

        findViewById<View>(R.id.start_intent_service_btn).setOnClickListener {
            startIntentService()
        }

        findViewById<View>(R.id.bind_service_btn).setOnClickListener {
            startBoundService()
        }

        findViewById<View>(R.id.clear_ip).setOnClickListener {
            ipTextView.text = ""
        }

        bindService(
            TextLoaderBoundService.createBindIntent(this),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceConnection.unbind(this)
    }

    private fun startService() {
        startService(TextLoaderService.createLoadIntent(this, "https://api.ipify.org/"))
    }

    private fun startIntentService() {
        startService(TextLoaderIntentService.createLoadIntent(this, "https://api.ipify.org/"))
    }

    private fun startBoundService() {
        startService(TextLoaderBoundService.createLoadIntent(this, "https://api.ipify.org/"))
    }

    private class ServiceConnectionImpl(val listener: (String?) -> Unit)
        : ServiceConnection {
        private var boundService: TextLoaderBoundService? = null

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("ServiceTestActivity", "onServiceConnected")
            boundService = (service as? TextLoaderBoundService.BinderImpl)?.service
            boundService?.listener = listener
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("ServiceTestActivity", "onServiceDisconnected")
            boundService?.listener = null
            boundService = null
        }

        fun unbind(context: Context) {
            context.unbindService(this)
            boundService?.listener = null
            boundService = null
        }
    }
}