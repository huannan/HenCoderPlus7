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
import androidx.core.animation.doOnEnd
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import com.hencoder.scalableimageview.dp
import com.hencoder.scalableimageview.getAvatar
import kotlin.math.max
import kotlin.math.min

/**
 * Bitmap原始尺寸
 */
private val IMAGE_SIZE = 300.dp.toInt()

/**
 * 放大的额外倍数
 */
private const val EXTRA_SCALE_FACTOR = 1.5f

/**
 * OverScroll距离
 */
private var OVER_SCROLL_OFFSET = 40.dp.toInt()

/**
 * 支持双击缩放
 * 支持双向滑动,支持OverScroll
 */
class ScalableImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 画笔
     */
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * Bitmap
     */
    private val bitmap = getAvatar(resources, IMAGE_SIZE)

    /**
     * 原始的偏移，为了将bitmap居中绘制
     */
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    /**
     * 用于滚动相关的偏移
     */
    private var offsetX = 0f
    private var offsetY = 0f

    /**
     * 缩放比例
     */
    private var smallScale = 0f
    private var bigScale = 0f

    /**
     * 手势识别
     */
    private val gestureDetector = GestureDetectorCompat(context, OnGestureListener())

    /**
     * 当前的缩放比例
     */
    private var isBig = false

    /**
     * 动画完成度
     */
    private var scaleFraction = 0f
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 缩放动画
     */
    private val scaleAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }

    /**
     * 滚动
     */
    private val scroller = OverScroller(context)

    /**
     * 处理惯性滑动的Runnable
     */
    private val flingRunnable = FlingRunnable()

    /**
     * 计算缩放比和原始偏移，在onSizeChanged里面计算会更加准确
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 计算原始偏移，目的是将Bitmap居中绘制
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

    /**
     * 滑动、缩放是通过Canvas的几何变换实现
     * 注意由于几何变换的时候坐标会变化，因此需要逆序理解
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 处理偏移
        // 缩小的时候需要还原偏移，但是需要注意不能一下子改变，需要在动画过程中慢慢缩小，与缩放动画关联起来，即乘以动画完成度，缩小过程中影响力越来越小
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction)
        // 处理缩放，计算当前的缩放比
        val scale = smallScale + (bigScale - smallScale) * scaleFraction
        // 缩放中心是View的中心点
        canvas.scale(scale, scale, width / 2f, height / 2f)
        // 画原始Bitmap，加上原始偏移的目的是居中绘制
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 将触摸事件代理给GestureDetector处理
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * 修正偏移，使得Bitmap超过View的边界
     */
    private fun fixOffset() {
        val maxOffsetX = (bitmap.width * bigScale - width) / 2f
        val minOffsetX = -maxOffsetX
        val maxOffsetY = (bitmap.height * bigScale - height) / 2f
        val minOffsetY = -maxOffsetY
        offsetX = min(offsetX, maxOffsetX)
        offsetX = max(offsetX, minOffsetX)
        offsetY = min(offsetY, maxOffsetY)
        offsetY = max(offsetY, minOffsetY)
    }

    private inner class OnGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            // onDown必须返回true
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
            // 获取滑动位移，注意这里的distanceX和distanceY是起始坐标-终点坐标,所以应该取相反数
            offsetX += -distanceX
            offsetY += -distanceY
            fixOffset()
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
            // 计算最大滑动位移，使得Bitmap不能滑出View的边界
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
            postOnAnimation(flingRunnable)
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return false
        }

        /**
         * 双击缩放
         */
        override fun onDoubleTap(e: MotionEvent): Boolean {
            isBig = !isBig
            // 触发缩放动画
            if (isBig) {
                // 放大的时候是以手指触摸点为中心放出去
                // 这里有两种方案：直接改缩放中心，但是这个改起来比较麻烦，中心点最好还是不要动；缩放中心还是View的中心，但是处理偏移
                offsetX = -((e.x - width / 2f) * (bigScale / smallScale - 1f))
                offsetY = -((e.y - height / 2f) * (bigScale / smallScale - 1f))
                fixOffset()
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

    private inner class FlingRunnable : Runnable {
        override fun run() {
            // 获取Scroller运动模型的结果,返回值为模型的运动是否finish
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.currX.toFloat()
                offsetY = scroller.currY.toFloat()
                invalidate()
                // 继续在下一帧到来的时候执行刷新
                ViewCompat.postOnAnimation(this@ScalableImageView, this)
            }
        }
    }
}