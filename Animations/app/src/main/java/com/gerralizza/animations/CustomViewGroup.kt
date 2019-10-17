package com.gerralizza.animations

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.forEach

class CustomViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        forEach { child ->
            val width = r - l
            val height = b - t

            val lp = child.layoutParams as LayoutParams

            val cx = (width * lp.xPercent / 100f).toInt()
            val cy = (height * lp.yPercent / 100f).toInt()

            val left = cx - child.measuredWidth / 2
            val top = cy - child.measuredHeight / 2

            child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private class LayoutParams(c: Context, attrs: AttributeSet)
        : ViewGroup.LayoutParams(c, attrs) {
        val xPercent: Float
        val yPercent: Float

        init {
            val ta = c.obtainStyledAttributes(attrs, R.styleable.CustomViewGroup_Layout)
            try {
                xPercent = ta.getFloat(R.styleable.CustomViewGroup_Layout_xPercent, 0f)
                yPercent = ta.getFloat(R.styleable.CustomViewGroup_Layout_yPercent, 0f)
            } finally {
                ta.recycle()
            }
        }
    }


}