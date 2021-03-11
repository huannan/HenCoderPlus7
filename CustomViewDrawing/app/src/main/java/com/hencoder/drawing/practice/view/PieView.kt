package com.hencoder.drawing.practice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hencoder.drawing.dp
import com.hencoder.drawing.half
import com.hencoder.drawing.radians
import kotlin.math.cos
import kotlin.math.sin

/**
 * 弧形的半径
 */
private val RADIUS = 150F.dp

/**
 * 角度信息
 */
private val ANGLES = floatArrayOf(60F, 90F, 150F, 60F)

/**
 * 颜色信息
 */
private val COLORS = intArrayOf(Color.parseColor("#C2185B"), Color.parseColor("#00ACC1"), Color.parseColor("#558B2F"), Color.parseColor("#5D4037"))

/**
 * 第几个需要偏移
 */
private const val TRANSLATE_INDEX = 1

/**
 * 偏移多少距离
 */
private val TRANSLATE_OFFSET = 20F.dp

class PiedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        var startAngle = 0F
        // 遍历角度信息，开始绘制扇形
        for ((index, angle) in ANGLES.withIndex()) {
            // 判断哪个扇形需要偏移，需要的话就通过画报来进行偏移
            if (index == TRANSLATE_INDEX) {
                canvas.save()
                val radians = getRadians(startAngle, angle)
                canvas.translate(TRANSLATE_OFFSET * cos(radians), TRANSLATE_OFFSET * sin(radians))
            }
            paint.color = COLORS[index]
            canvas.drawArc(width / 2F - RADIUS, height / 2F - RADIUS, width / 2F + RADIUS, height / 2F + RADIUS, startAngle, angle, true, paint)
            startAngle += angle
            // 恢复画报
            if (index == TRANSLATE_INDEX) {
                canvas.restore()
            }
        }
    }

    /**
     * 计算弧度，用于计算偏移的横纵坐标
     */
    private fun getRadians(startAngle: Float, angle: Float): Float {
        return (startAngle + angle.half).radians
    }

}