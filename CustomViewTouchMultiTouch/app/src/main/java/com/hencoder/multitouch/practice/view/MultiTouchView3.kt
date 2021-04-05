package com.hencoder.multitouch.practice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.hencoder.multitouch.dp

/**
 * 各⾃为战型 各个 pointer 做不同的事，互不影响。
 * 典型：⽀持多画笔的画板应⽤。
 * 实现⽅式：在每个 DOWN、POINTER_DOWN 事件中记录下每个 pointer 的 id，在 MOVE 事件中使⽤ id 对它们进⾏跟踪。
 */
class MultiTouchView3 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paths = SparseArray<Path>()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4.dp
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (index in 0 until paths.size()) {
            val path = paths.valueAt(index)
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                val path = Path()
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                paths.append(event.getPointerId(actionIndex), path)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until paths.size()) {
                    val pointerId = event.getPointerId(index)
                    val path = paths.get(pointerId)
                    val pointerIndex = event.findPointerIndex(pointerId)
                    path.lineTo(event.getX(pointerIndex), event.getY(pointerIndex))
                }
                invalidate()
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                paths.remove(pointerId)
                invalidate()
            }
        }
        return true
    }
}