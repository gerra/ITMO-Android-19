package com.gerralizza.navigation.results

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gerralizza.navigation.R

class WaitResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wait_result)

        findViewById<View>(R.id.start).setOnClickListener {
            startActivityForResult(Intent(this, ShareResultActivity::class.java), REQUEST_CODE_WAIT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_WAIT -> {
                val txt = data?.getStringExtra(ShareResultActivity.KEY_RESULT_TEXT)
                Toast.makeText(this, "Result: $resultCode, text: $txt", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val REQUEST_CODE_WAIT = 23
    }
}