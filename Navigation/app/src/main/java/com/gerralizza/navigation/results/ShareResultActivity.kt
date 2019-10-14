package com.gerralizza.navigation.results

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gerralizza.navigation.R

class ShareResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_share_result)

        findViewById<View>(R.id.ok).setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra(KEY_RESULT_TEXT, "OK"))
            finish()
        }

        findViewById<View>(R.id.cancel).setOnClickListener {
            setResult(Activity.RESULT_CANCELED, Intent().putExtra(KEY_RESULT_TEXT, "CANCEL"))
            finish()
        }
    }

    companion object {
        const val KEY_RESULT_TEXT = "resultText"
    }
}