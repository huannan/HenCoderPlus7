package com.hencoder.layoutlayout.practice.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

/**
 * 自动换行的流式布局
 */
class FLowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    private val childBounds = mutableListOf<Rect>()

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 父View对自己的限制
        val specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        // 已用宽/高度
        var widthUsed = 0
        var heightUsed = 0
        // 当前行已使用的宽度
        var lineWidthUsed = 0
        // 当前行最大高度
        var lineMaxHeight = 0

        // 遍历每个⼦ View，测量⼦ View
        for ((index, child) in children.withIndex()) {
            // 可以查state,但是一般子View不支持这个特性,所以一般直接不限制子View的可用宽度,直接按照全宽来量,否则测量超过限制的条件用于无法触发
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            // 超过最大宽度,重新测量
            if (specWidthMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + child.measuredWidth > specWidthSize) {
                lineWidthUsed = 0
                heightUsed += lineMaxHeight
                lineMaxHeight = 0
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            }

            if (index >= childBounds.size) {
                childBounds.add(Rect())
            }
            val childBound = childBounds[index]
            childBound.set(lineWidthUsed, heightUsed, lineWidthUsed + child.measuredWidth, heightUsed + child.measuredHeight)

            lineWidthUsed += child.measuredWidth
            widthUsed = max(widthUsed, lineWidthUsed)
            lineMaxHeight = max(lineMaxHeight, child.measuredHeight)
        }

        // 测量出所有⼦ View 的位置和尺⼨后，计算出⾃⼰的尺⼨，并⽤ setMeasuredDimension(width, height) 保存
        val width = resolveSize(widthUsed, widthMeasureSpec)
        val height = resolveSize(heightUsed + lineMaxHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 遍历每个⼦ View，调⽤它们的 layout() ⽅法来将位置和尺⼨传给它们
        for ((index, child) in children.withIndex()) {
            val childBound = childBounds[index]
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}