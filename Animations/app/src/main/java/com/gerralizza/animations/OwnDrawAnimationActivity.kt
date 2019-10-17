package com.gerralizza.animations

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class OwnDrawAnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = FrameLayout(this)
        val lp = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        content.addView(OwnDrawAnimationView(this), lp)

        setContentView(content)
    }
}