package com.gerralizza.networking.asynctask

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gerralizza.networking.R
import java.lang.ref.WeakReference
import java.net.URL

class AsyncTaskActivity : AppCompatActivity() {
    private lateinit var ipTextView: TextView

    private var asyncTask: TextLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_task)

        ipTextView = findViewById(R.id.ip_txt)

        findViewById<View>(R.id.clear_ip).setOnClickListener {
            ipTextView.text = ""
        }

        findViewById<View>(R.id.show_ip_async_task_btn).setOnClickListener {
            downloadUsingAsyncTask()

//            SimpleLoader("https://api.ipify.org/", this).execute()
        }

        findViewById<View>(R.id.show_ip_weak_async_task_btn).setOnClickListener {
            TextLoaderWeak(this).execute("https://api.ipify.org/")
        }

        asyncTask = lastCustomNonConfigurationInstance as? TextLoader
        asyncTask?.attachActivity(this)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return asyncTask
    }

    override fun onDestroy() {
        // Тут можно вызвать asyncTask?.cancel(true), но тогда не нужно его сохранять
        // в onRetainCustomNonConfigurationInstance() и если задача не завершилась до переворота
        // экрана, ее нужно будет запустить заново
        asyncTask?.activity = null
        super.onDestroy()
    }

    internal fun onLoadCompleted(result: String?) {
        ipTextView.text = result
        asyncTask = null
    }

    private fun downloadUsingAsyncTask() {
        asyncTask?.cancel(true)
        asyncTask?.activity = null
        asyncTask = TextLoader(this).apply { execute("https://api.ipify.org/") }
    }

    private class SimpleLoader(
        val url: String,
        activity: AsyncTaskActivity
    ) : AsyncTask<Unit, Unit, String>() {
        private val activityRef = WeakReference(activity)

        override fun doInBackground(vararg params: Unit?): String {
            SystemClock.sleep(20_000L)

            return "Hello, world!"

//            return URL(url)
//                .openConnection()
//                .getInputStream()
//                .reader()
//                .readText()
        }

        override fun onPostExecute(result: String?) {
            activityRef.get()?.ipTextView?.text = result
        }
    }

    private class TextLoader(var activity: AsyncTaskActivity?) : AsyncTask<String, Int, String>() {
        private var cachedResult: String? = null

        override fun onPreExecute() {
            // Вызовется сразу после execute() в том потоке, откуда вызвали AsyncTask
            Log.d("TextLoader", "onPreExecute(), ${Thread.currentThread().name}")
        }


        override fun onProgressUpdate(vararg values: Int?) {
            Log.d("TextLoader", "onProgressUpdate(${values[0]}), ${Thread.currentThread().name}")
        }

        override fun doInBackground(vararg params: String?): String? {
            SystemClock.sleep(5_000L)

            return "Hello, world!"
        }

        override fun onPostExecute(result: String?) {
            activity?.onLoadCompleted(result) ?: run {
                cachedResult = result
            }
        }

        override fun onCancelled(result: String?) {
            Log.d("TextLoader", "onCancelled($result), ${Thread.currentThread().name}")
        }

        fun attachActivity(activity: AsyncTaskActivity) {
            this.activity = activity

            cachedResult?.let {
                activity.onLoadCompleted(it)
                cachedResult = null
            }
        }
    }

    private class TextLoaderWeak(activity: AsyncTaskActivity) : AsyncTask<String, Unit, String>() {
        private val activityRef = WeakReference(activity)

        override fun doInBackground(vararg params: String?): String {
            SystemClock.sleep(1000L)

            return URL(params[0])
                .openConnection()
                .getInputStream()
                .reader()
                .readText()
        }

        override fun onPostExecute(result: String?) {
            val activity = activityRef.get()
            Log.d("TextLoaderWeak", "onPostExecute(), $activity")
            activity?.onLoadCompleted(result)
        }
    }
}