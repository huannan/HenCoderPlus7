package com.hencoder.bitmapanddrawable.practice

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class DrawableView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val drawable = MeshDrawable()

    override fun onDraw(canvas: Canvas) {
        drawable.setBounds(left, top, right, bottom)
        drawable.draw(canvas)
    }

}