package com.hencoder.layoutsize.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.hencoder.layoutsize.dp

private val RADIUS = 100.dp
private val PADDING = 20.dp

/**
 * 完全⾃定义 View 的尺⼨
 */
class CircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // 计算出⾃⼰的尺⼨
    val size = ((PADDING + RADIUS) * 2).toInt()
    // ⽤ resolveSize() 或者 resolveSizeAndState() 修正结果
    val width = resolveSize(size, widthMeasureSpec)
    val height = resolveSize(size, heightMeasureSpec)
    // 使⽤ setMeasuredDimension(width, height) 保存结果
    setMeasuredDimension(width, height)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(PADDING + RADIUS, PADDING + RADIUS, RADIUS, paint)
  }
}