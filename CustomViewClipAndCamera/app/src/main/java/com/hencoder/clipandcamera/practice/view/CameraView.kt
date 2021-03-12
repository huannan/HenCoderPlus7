package com.hencoder.clipandcamera.practice.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hencoder.clipandcamera.R
import com.hencoder.clipandcamera.dp
import com.hencoder.clipandcamera.half

private val BITMAP_SIZE = 200.dp

class CameraView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvatar(BITMAP_SIZE.toInt())
    private val camera = Camera()

    init {
        camera.rotateX(30f)
        // 如果绘制的内容过大，当它翻转起来的时候，就有可能出现图像投影过大的「糊脸」效果。而且由于换算单位被写死成了 72 像素，而不是和设备 dpi 相关的，所以在像素越大的手机上，这种「糊脸」效果会越明显。
        // 使用 setLocation() 方法来把相机往后移动，就可以修复这种问题
        camera.setLocation(0f, 0f, -6 * resources.displayMetrics.density)
    }

    override fun onDraw(canvas: Canvas) {
        // 上半部分
        canvas.save()
        canvas.clipRect(width.half - BITMAP_SIZE.half, height.half - BITMAP_SIZE.half, width.half + BITMAP_SIZE.half, height.half.toFloat())
        canvas.drawBitmap(bitmap, width.half - BITMAP_SIZE.half, height.half - BITMAP_SIZE.half, paint)
        canvas.restore()

        // 下半部分
        canvas.save()
        canvas.translate(width.half.toFloat(), height.half.toFloat())
        camera.applyToCanvas(canvas)
        canvas.clipRect(-BITMAP_SIZE, 0F, BITMAP_SIZE, BITMAP_SIZE)
        canvas.translate(-width.half.toFloat(), -height.half.toFloat())
        canvas.drawBitmap(bitmap, width.half - BITMAP_SIZE.half, height.half - BITMAP_SIZE.half, paint)
        canvas.restore()
    }

    private fun getAvatar(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.avatar_rengwuxian, options)
    }

}