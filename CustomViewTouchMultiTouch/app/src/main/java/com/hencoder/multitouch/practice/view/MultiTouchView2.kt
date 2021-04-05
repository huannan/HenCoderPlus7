package com.hencoder.multitouch.practice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.hencoder.multitouch.dp
import com.hencoder.multitouch.getAvatar

/**
 * 配合型 / 协作型 所有触摸到 View 的 pointer 共同起作⽤。
 * 典型：ScaleGestureDetector，以及 GestureDetector 的 onScroll() ⽅法判断。
 * 实现⽅式：在每个 DOWN、POINTER_DOWN、POINTER_UP、UP 事件中使⽤所有 pointer 的坐标来共同更新焦点坐标，并在 MOVE 事件中使⽤所有 pointer 的坐标来判断位置。
 */
class MultiTouchView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, 200.dp.toInt())
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var downX = 0f
    private var downY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val focusX: Float
        val focusY: Float
        var sumX = 0f
        var sumY = 0f
        // 如果是ACTION_POINTER_UP需要排除抬起的手指
        val isPointerUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP
        for (index in 0 until event.pointerCount) {
            if (isPointerUp && index == event.actionIndex) {
                continue
            }
            sumX += event.getX(index)
            sumY += event.getY(index)
        }
        val pointerCount = if (isPointerUp) event.pointerCount - 1 else event.pointerCount
        focusX = sumX / pointerCount
        focusY = sumY / pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downX = focusX
                downY = focusY
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + originalOffsetX
                offsetY = focusY - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}