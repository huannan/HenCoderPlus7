package com.hencoder.viewgroup.view

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import androidx.core.view.children
import kotlin.math.abs

class TwoPager(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    // 按下时的位置以及偏移
    private var downX = 0f
    private var downY = 0f
    private var downScrollX = 0f
    // 用于判断是否滑动
    private var scrolling = false
    private val overScroller: OverScroller = OverScroller(context)
    private val velocityTracker = VelocityTracker.obtain()
    private val viewConfiguration: ViewConfiguration = ViewConfiguration.get(context)
    private var minVelocity = viewConfiguration.scaledMinimumFlingVelocity
    private var maxVelocity = viewConfiguration.scaledMaximumFlingVelocity
    private var pagingSlop = viewConfiguration.scaledPagingTouchSlop

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 直接用自己的测量限制去测量子View
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 测量自己
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        var childRight = width
        val childTop = 0
        val childBottom = height
        // 布局子View：一个一个横向排开
        for (child in children) {
            child.layout(childLeft, childTop, childRight, childBottom)
            childLeft += width
            childRight += width
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        var result = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrolling = false
                // 记录按下时的信息
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> if (!scrolling) {
                // 判断是否以及产生滑动
                val dx = downX - event.x
                if (abs(dx) > pagingSlop) {
                    scrolling = true
                    // 拦截子View的触摸事件的同事最好也请求父View不要拦截我自己的触摸事件
                    parent.requestDisallowInterceptTouchEvent(true)
                    // 拦截子View的触摸事件,此时子View会收到ACTION_CANCEL
                    result = true
                }
            }
        }
        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear()
        }
        velocityTracker.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 记录按下时的信息
                downX = event.x
                downY = event.y
                downScrollX = scrollX.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                // 计算滑动距离,注意这个是反的
                val dx = (downX - event.x + downScrollX).toInt()
                        .coerceAtLeast(0)
                        .coerceAtMost(width)
                // 滑动
                scrollTo(dx, 0)
            }
            MotionEvent.ACTION_UP -> {
                // 掐秒表计算当前速度
                velocityTracker.computeCurrentVelocity(1000, maxVelocity.toFloat()) // v = xxx个像素点 / 1000ms
                val vx = velocityTracker.xVelocity
                val scrollX = scrollX
                val targetPage = if (abs(vx) < minVelocity) {
                    // 如果速度小于minVelocity,以滑动当前距离来判断目标位置
                    if (scrollX > width / 2) 1 else 0
                } else {
                    // 如果速度比较快,以滑动方向来判断目标位置
                    if (vx < 0) 1 else 0
                }
                // 计算滑动距离,开始滑动
                val scrollDistance = if (targetPage == 1) width - scrollX else -scrollX
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0)
                // 使绘制失效->onDraw()->computeScroll()
                postInvalidateOnAnimation()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.currX, overScroller.currY)
            postInvalidateOnAnimation()
        }
    }
}
