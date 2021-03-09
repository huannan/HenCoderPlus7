package com.hencoder.xfermode.practice.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hencoder.xfermode.practice.dp

private val RADIUS = 50F.dp
private val IMAGE_WIDTH = 200F.dp
private val IMAGE_PADDING = 30F.dp
private val XFETMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
private val XFETMODE_RECTF = RectF(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + IMAGE_WIDTH, IMAGE_PADDING + IMAGE_WIDTH)

/**
 * Xfermode只对有效区域生效，包括Bitmap的透明区域
 */
class XfermodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var srcBitmap: Bitmap = Bitmap.createBitmap(IMAGE_WIDTH.toInt(), IMAGE_WIDTH.toInt(), Bitmap.Config.ARGB_8888)
    private var destBitmap: Bitmap = Bitmap.createBitmap(IMAGE_WIDTH.toInt(), IMAGE_WIDTH.toInt(), Bitmap.Config.ARGB_8888)

    init {
        val canvas = Canvas()

        canvas.setBitmap(srcBitmap)
        paint.color = Color.parseColor("#2196F3")
        canvas.drawRect(IMAGE_PADDING, srcBitmap.height - IMAGE_PADDING - 2 * RADIUS, IMAGE_PADDING + 2 * RADIUS, srcBitmap.height - IMAGE_PADDING, paint)

        canvas.setBitmap(destBitmap)
        paint.color = Color.parseColor("#D81B60")
        canvas.drawOval(destBitmap.width - IMAGE_PADDING - 2 * RADIUS, IMAGE_PADDING, destBitmap.width - IMAGE_PADDING, IMAGE_PADDING + 2 * RADIUS, paint)
    }

    override fun onDraw(canvas: Canvas) {
        // 开启离屏缓冲
        val count = canvas.saveLayer(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + 3 * RADIUS, IMAGE_PADDING + 3 * RADIUS, null)
        // val count = canvas.saveLayer(XFETMODE_RECTF, null)

        canvas.drawCircle(IMAGE_PADDING + 2 * RADIUS, IMAGE_PADDING + RADIUS, RADIUS, paint)
        // canvas.drawBitmap(destBitmap, 0F, 0F, paint)

        // 应用Xfermode
        paint.xfermode = XFETMODE

        paint.color = Color.parseColor("#2196F3")
        canvas.drawRect(IMAGE_PADDING, IMAGE_PADDING + RADIUS, IMAGE_PADDING + 2 * RADIUS, IMAGE_PADDING + 3 * RADIUS, paint)
        // canvas.drawBitmap(srcBitmap, 0F, 0F, paint)

        // 恢复默认
        paint.xfermode = null
        canvas.restoreToCount(count)
    }

}