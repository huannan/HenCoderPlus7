package com.hencoder.text.practice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.hencoder.text.R
import com.hencoder.text.color
import com.hencoder.text.dp
import com.hencoder.text.half

/**
 * 弧形的半径
 */
private val RADIUS = 150.dp

/**
 * 圆环颜色
 */
private val CIRCLE_COLOR = "#90A4AE".color

/**
 * 进度颜色
 */
private val HIGHLIGHT_COLOR = "#FF4081".color

/**
 * 线条粗细
 */
private val RING_WIDTH = 20.dp

private const val TAG = "SportView"

class SportView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = RING_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = HIGHLIGHT_COLOR
        typeface = ResourcesCompat.getFont(context, R.font.font)
    }
    private val textBounds = Rect()
    val text = "abab"

    override fun onDraw(canvas: Canvas) {
        // 绘制背景
        ringPaint.color = CIRCLE_COLOR
        canvas.drawCircle(width.half, height.half, RADIUS, ringPaint)

        // 绘制进度
        ringPaint.color = HIGHLIGHT_COLOR
        canvas.drawArc(width.half - RADIUS, height.half - RADIUS, width.half + RADIUS, height.half + RADIUS, -90F, 225F, false, ringPaint)

        // 绘制两条辅助线
        canvas.drawLine(width.half, 0F, width.half, height.toFloat(), linePaint)
        canvas.drawLine(0F, height.half, width.toFloat(), height.half, linePaint)

        // 绘制文字-水平垂直严格居中-适用于静态文字
        textPaint.textSize = 100.dp
        // 水平居中
        textPaint.textAlign = Paint.Align.CENTER
        // 垂直居中
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        Log.d(TAG, "onDraw: getTextBounds bottom=${textBounds.bottom}, top=${textBounds.top}")
        canvas.drawText(text, width.half, height.half - (textBounds.bottom + textBounds.top).half, textPaint)

        // 绘制文字-水平垂直严格居中-适用于动态文字
        textPaint.textSize = 100.dp
        // 水平居中
        textPaint.textAlign = Paint.Align.CENTER
        // 垂直居中
        val fontMetrics = textPaint.fontMetrics
        Log.d(TAG, "onDraw: getFontMetrics descent=${fontMetrics.descent}, top=${fontMetrics.ascent}")
        canvas.drawText(text, width.half, height.half - (fontMetrics.descent + fontMetrics.ascent).half, textPaint)

        // 绘制文字-文字贴边(对齐)
        textPaint.textSize = 150.dp
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        // 一般段落阅读型的不用太贴边,推荐用fontMetrics.top/fontMetrics.ascent
        canvas.drawText(text, -textBounds.left.toFloat(), -fontMetrics.top, textPaint)
        // 如果需要严格贴边的话,就用textBounds.top
        // canvas.drawText(text, -textBounds.left.toFloat(), -textBounds.top.toFloat(), textPaint)
    }

}