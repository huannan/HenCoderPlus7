package com.hencoder.drawing.practice.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hencoder.drawing.practice.dp
import com.hencoder.drawing.practice.half
import com.hencoder.drawing.practice.radians
import kotlin.math.cos
import kotlin.math.sin

/**
 * 弧形的半径
 */
private val RADIUS = 150F.dp

/**
 * 开口角度
 */
private const val OPEN_ANGLE = 120F

/**
 * 线条粗细
 */
private val STROKE_WIDTH = 3F.dp

/**
 * 刻度的宽度
 */
private val DASH_WIDTH = 2F.dp

/**
 * 刻度的高度
 */
private val DASH_HEIGHT = 10F.dp

/**
 * 切分成多少份
 */
private const val COUNT = 20

/**
 * 指针长度
 */
private val LENGTH = 120F.dp

class DashboardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dash = Path()
    private val pathMeasure = PathMeasure()
    private lateinit var pathEffect: PathEffect

    init {
        paint.strokeWidth = STROKE_WIDTH
        paint.style = Paint.Style.STROKE

        dash.addRect(0F, 0F, DASH_WIDTH, DASH_HEIGHT, Path.Direction.CCW)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // 添加弧形
        path.reset()
        path.addArc(width / 2F - RADIUS, height / 2F - RADIUS, width / 2F + RADIUS, height / 2F + RADIUS, 90F + OPEN_ANGLE.half, 360F - OPEN_ANGLE)

        // 使用PathMeasure测量弧度长度，计算PathDashPathEffect的间距
        pathMeasure.setPath(path, false)
        // 使用PathDashPathEffect来绘制刻度
        pathEffect = PathDashPathEffect(dash, (pathMeasure.length - DASH_WIDTH) / COUNT, 0F, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        // 画弧线
        canvas.drawPath(path, paint)

        // 画刻度
        paint.pathEffect = pathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        // 画指针
        val mark = 5
        val radians = getRadians(mark)
        canvas.drawLine(width / 2F, height / 2F, width / 2F + LENGTH * cos(radians), height / 2F + LENGTH * sin(radians), paint)
    }

    /**
     * 计算指针终点位置的角的弧度值，然后通过三角函数计算指针终点位置
     */
    private fun getRadians(mark: Int): Float {
        return (90F + OPEN_ANGLE.half + (360F - OPEN_ANGLE) / COUNT * mark).radians
    }

}