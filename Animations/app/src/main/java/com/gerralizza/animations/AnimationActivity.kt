package com.gerralizza.animations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_animation.*

class AnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_animation)

        fade_in.setOnClickListener { animation_view.fadeIn() }
        fade_out.setOnClickListener { animation_view.fadeOut() }
        rotate.setOnClickListener { animation_view.rotate() }
        animate_color.setOnClickListener { animation_view.animateColor() }
        own_fade_in.setOnClickListener { animation_view.fadeInOwn() }
    }
}