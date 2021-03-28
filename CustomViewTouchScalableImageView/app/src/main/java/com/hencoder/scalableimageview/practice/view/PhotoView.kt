package com.hencoder.scalableimageview.practice.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.hencoder.scalableimageview.dp
import com.hencoder.scalableimageview.getAvatar
import kotlin.math.max
import kotlin.math.min

private val IMAGE_SIZE = 300.dp.toInt()
private const val EXTRA_SCALE_FACTOR = 1.5f
private var OVER_SCROLL_OFFSET = 40.dp.toInt()

/**
 * 支持双击缩放
 * 支持双向滑动,支持OverScroll
 */
class PhotoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, Runnable {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(resources, IMAGE_SIZE)
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var smallScale = 0f
    private var bigScale = 0f
    private val gestureDetector = GestureDetectorCompat(context, this)
    private var isBig = false
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }
    private val scroller = OverScroller(context)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - IMAGE_SIZE) / 2f
        originalOffsetY = (height - IMAGE_SIZE) / 2f

        // 判断bitmap的宽高比和View的宽高比,以确定以宽还是高作为最小缩放比
        if (bitmap.width.toFloat() / bitmap.height.toFloat() > width.toFloat() / height.toFloat()) {
            smallScale = width.toFloat() / bitmap.width.toFloat()
            bigScale = height.toFloat() / bitmap.height.toFloat() * EXTRA_SCALE_FACTOR
        } else {
            smallScale = height.toFloat() / bitmap.height.toFloat()
            bigScale = width.toFloat() / bitmap.width.toFloat() * EXTRA_SCALE_FACTOR
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(offsetX, offsetY)
        val scale = smallScale + (bigScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    /**
     * 手指跟随的滑动
     */
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        // 只有在放到最大的时候才可以滑动
        if (!isBig) {
            return false
        }
        // 注意这里的distanceX和distanceY是起始坐标-终点坐标,所以应该取相反数
        offsetX += -distanceX
        offsetY += -distanceY
        // 修正偏移,使得不能滑出边界
        val maxOffsetX = (bitmap.width * bigScale - width) / 2f
        val minOffsetX = -maxOffsetX
        val maxOffsetY = (bitmap.height * bigScale - height) / 2f
        val minOffsetY = -maxOffsetY
        offsetX = min(offsetX, maxOffsetX)
        offsetX = max(offsetX, minOffsetX)
        offsetY = min(offsetY, maxOffsetY)
        offsetY = max(offsetY, minOffsetY)
        // 触发动画
        invalidate()
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    /**
     * 惯性滑动
     */
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        // 只有在放到最大的时候才可以滑动
        if (!isBig) {
            return false
        }
        val maxOffsetX = (bitmap.width * bigScale - width) / 2f
        val minOffsetX = -maxOffsetX
        val maxOffsetY = (bitmap.height * bigScale - height) / 2f
        val minOffsetY = -maxOffsetY
        scroller.fling(
            offsetX.toInt(), offsetY.toInt(),
            velocityX.toInt(), velocityY.toInt(),
            minOffsetX.toInt(), maxOffsetX.toInt(),
            minOffsetY.toInt(), maxOffsetY.toInt(),
            OVER_SCROLL_OFFSET, OVER_SCROLL_OFFSET
        )
        // postOnAnimation可以使得Runnable在下一帧到来的时候执行,而普通的post不会等下一帧到来
        postOnAnimation(this)
        return true
    }

    override fun run() {
        refresh()
    }

    private fun refresh() {
        // 计算Scroller运动模型的结果,返回值为模型的运动是否finish
        if (scroller.computeScrollOffset()) {
            offsetX = scroller.currX.toFloat()
            offsetY = scroller.currY.toFloat()
            invalidate()
            ViewCompat.postOnAnimation(this, this)
        }
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        return false
    }

    /**
     * 双击缩放
     */
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        isBig = !isBig
        if (isBig) {
            scaleAnimator.start()
        } else {
            scaleAnimator.reverse()
        }
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

}