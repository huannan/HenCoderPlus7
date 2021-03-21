package com.hencoder.layoutsize.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

/**
 * 简单改写已有 View 的尺⼨
 * 例子:方形ImageView,以短边为标准
 */
class SquareImageView(context: Context?, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // ⽤ getMeasuredWidth() 和 getMeasuredHeight() 获取到测量出的尺⼨
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    // 计算出最终要的尺⼨
    val size = min(measuredWidth, measuredHeight)
    // ⽤ setMeasuredDimension(width, height) 把结果保存
    setMeasuredDimension(size, size)
  }
}