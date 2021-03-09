package com.hencoder.xfermode.practice.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hencoder.xfermode.R
import com.hencoder.xfermode.practice.dp

private val IMAGE_WIDTH = 200F.dp
private val IMAGE_PADDING = 30F.dp
private val XFETMODE = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
private val XFETMODE_RECTF = RectF(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + IMAGE_WIDTH, IMAGE_PADDING + IMAGE_WIDTH)

class AvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        // 开启离屏缓冲
        val count = canvas.saveLayer(XFETMODE_RECTF, null)

        // 绘制dest图层
        canvas.drawOval(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING + IMAGE_WIDTH, IMAGE_PADDING + IMAGE_WIDTH, paint)

        // 应用Xfermode
        paint.xfermode = XFETMODE

        // 绘制srx图层
        canvas.drawBitmap(getAvatar(IMAGE_WIDTH.toInt()), IMAGE_PADDING, IMAGE_PADDING, paint)

        // 恢复默认
        paint.xfermode = null
        canvas.restoreToCount(count)
    }

    /**
     * 获取头像Bitmap，并且优化加载大小
     */
    fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }

}