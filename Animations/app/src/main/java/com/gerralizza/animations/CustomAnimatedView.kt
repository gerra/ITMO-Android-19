package com.gerralizza.animations

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class CustomAnimatedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val image: ImageView
    private val text: TextView

    private var animator: Animator? = null

    init {
        val content = LayoutInflater.from(context).inflate(R.layout.layout_animated_view, this, true)
        image = content.findViewById(R.id.image)
        text = content.findViewById(R.id.text)
    }

    fun fadeIn() {
        image.animate().alpha(1f).setDuration(500L)
        text.animate().alpha(1f).setDuration(1000L).setInterpolator(FastOutLinearInInterpolator())
    }

    fun fadeOut() {
        image.animate().alpha(0f).setDuration(1000L).setInterpolator(FastOutSlowInInterpolator())
        text.animate().alpha(0f).setDuration(500L)
    }

    fun rotate() {
        image.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_left))
        text.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_right))
    }

    fun animateColor() {
        animator?.cancel()
        animator = ValueAnimator.ofArgb(Color.WHITE, Color.RED).apply {
            duration = 1000L
            addUpdateListener {
                text.setTextColor(it.animatedValue as Int)
            }

            start()
        }


    }

    fun fadeInOwn() {
        animator?.cancel()
        animator = AnimatorSet().apply {
            val imageAnimator = ObjectAnimator.ofFloat(image, View.ALPHA, 1f).apply {
                duration = 500L
            }
            val textAnimator = ObjectAnimator.ofFloat(text, View.ALPHA, 1f).apply {
                duration = 500L
            }

            playSequentially(imageAnimator, textAnimator)

            start()
        }
    }
}