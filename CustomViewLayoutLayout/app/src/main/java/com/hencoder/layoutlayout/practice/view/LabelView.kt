package com.hencoder.layoutlayout.practice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.hencoder.layoutlayout.dp
import java.util.*

private val TEXT_COLORS = intArrayOf(
    Color.parseColor("#E91E63"),
    Color.parseColor("#673AB7"),
    Color.parseColor("#3F51B5"),
    Color.parseColor("#2196F3"),
    Color.parseColor("#009688"),
    Color.parseColor("#FF9800"),
    Color.parseColor("#FF5722"),
    Color.parseColor("#795548")
)
private val TEXT_SIZES = floatArrayOf(16f, 22f, 28f)
private val CORNER_RADIUS = 4.dp
private val X_PADDING = 16.dp.toInt()
private val Y_PADDING = 8.dp.toInt()

/**
 * 标签
 */
class LabelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val random = Random()

    init {
        paint.color = TEXT_COLORS[random.nextInt(TEXT_COLORS.size)]
        textSize = TEXT_SIZES[random.nextInt(TEXT_SIZES.size)]
        setTextColor(Color.WHITE)
        setPadding(X_PADDING, Y_PADDING, X_PADDING, Y_PADDING)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), CORNER_RADIUS, CORNER_RADIUS, paint)
        super.onDraw(canvas)
    }

}