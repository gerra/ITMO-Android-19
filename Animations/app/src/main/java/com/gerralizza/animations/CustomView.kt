package com.gerralizza.animations

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var valueText = ""
    private var valueColor: Int = Color.BLACK
    private var intersected: Boolean = false

    private val linePaint = Paint().also {
        it.color = Color.BLUE
        it.strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics
        )
    }

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.CustomView, defStyleAttr, 0)
        try {
            valueText = a.getString(R.styleable.CustomView_valueText) ?: ""
            valueColor = a.getColor(R.styleable.CustomView_valueColor, Color.BLACK)
            intersected = a.getBoolean(R.styleable.CustomView_intersected, false)
        } finally {
            a.recycle()
        }

        text = valueText
        setTextColor(valueColor)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (intersected) {
            canvas.drawLine(
                0f,
                canvas.height / 2f,
                canvas.width.toFloat(),
                canvas.height / 2f,
                linePaint
            )
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = CustomState(super.onSaveInstanceState())
        state.someValue = 1
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as CustomState
        super.onRestoreInstanceState(state.superState)
        Log.d("CustomView", "someValue: ${state.someValue}")
    }

    private class CustomState : BaseSavedState {
        var someValue: Int = 0

        constructor(superState: Parcelable?) : super(superState)
        constructor(parcel: Parcel) : super(parcel) {
            someValue = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(someValue)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<CustomState> {
                override fun createFromParcel(source: Parcel): CustomState = CustomState(source)
                override fun newArray(size: Int): Array<CustomState?> = arrayOfNulls(size)
            }
        }
    }
}